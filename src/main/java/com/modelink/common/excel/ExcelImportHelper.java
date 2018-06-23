package com.modelink.common.excel;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 导入文件助手
 */
public class ExcelImportHelper {

    public static Logger logger = LoggerFactory.getLogger(ExcelImportHelper.class);

    public static List<List<String>> importExcel(ExcelConfigation configation, InputStream inputStream) throws Exception {
        Sheet sheet;
        Workbook workBook;
        List<List<String>> rtnList = new ArrayList<List<String>>();
        if (!inputStream.markSupported()) {
            inputStream = new PushbackInputStream(inputStream, 8);
        }

        workBook = WorkbookFactory.create(inputStream);
        if (workBook == null) {
            logger.error("[excelImportHelper|importExcel]目标表格工作空间为空");
            throw new Exception("读取Excel文件发生异常");
        }
        int numberSheet = workBook.getNumberOfSheets();
        if (numberSheet <= 0) {
            logger.error("[excelImportHelper|importExcel]目标表格工作簿数目为零");
            throw new Exception("读取Excel文件发生异常");
        }
        sheet = workBook.getSheetAt(0);

        // 数据行长度
        int rowLength, colLength;
        Row row = null;
        Cell cell = null;
        String cellContent;
        StringBuilder sb = null;
        List<String> rowData = null;

        int startRowNum = (configation.getStartRowNum() == ExcelConfigation.DEFAULT_INT ? 0 : configation.getStartRowNum());
        int startColNum = (configation.getStartColNum() == ExcelConfigation.DEFAULT_INT ? 0 : configation.getStartColNum());
        sheet = workBook.getSheetAt(0);// 获取第一个工作簿(Sheet)的内容【注意根据实际需要进行修改】
        rowLength = sheet.getPhysicalNumberOfRows(); // 总行数
        rowLength = (configation.getTotalRowNum() == ExcelConfigation.DEFAULT_INT ? rowLength : configation.getTotalRowNum() + startRowNum);
        for (int i = startRowNum; i < rowLength; i++) {
            rowData = new ArrayList<String>();
            row = sheet.getRow(i);
            colLength = row.getLastCellNum();
            colLength = (configation.getTotalColNum() == ExcelConfigation.DEFAULT_INT ? colLength : configation.getTotalColNum() + startColNum);
            for (int j = startColNum; j < colLength; j++) {
                cell = row.getCell(j);
                if(cell == null){
                    cellContent = "";
                }else{
                    cell.setCellType(CellType.STRING);
                    cellContent = cell.getStringCellValue();
                }
                rowData.add(cellContent);
            }
            // 判断改行是否为空
            sb = new StringBuilder();
            for (String rowStr : rowData) {
                sb.append(rowStr.trim());
            }
            if("".equals(sb.toString())){
                continue;
            }
            rtnList.add(rowData);
        }

        inputStream.close();
        return rtnList;
    }
}
