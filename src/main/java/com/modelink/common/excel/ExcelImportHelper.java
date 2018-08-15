package com.modelink.common.excel;

import com.modelink.common.utils.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel 导入文件助手
 */
public class ExcelImportHelper {

    public static Logger logger = LoggerFactory.getLogger(ExcelImportHelper.class);

    public static List<List<String>> importExcel(ExcelImportConfigation configation, InputStream inputStream) throws Exception {
        Sheet sheet;
        Workbook workBook;
        List<List<String>> rtnList = new ArrayList<List<String>>();
        if (!inputStream.markSupported()) {
            inputStream = new PushbackInputStream(inputStream, 8);
        }
        logger.info("[excelImportHelper|importExcel]开始读取Excel中的数据");
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

        // 数据行长度
        int rowLength, colLength;
        Row row = null;
        Cell cell = null;
        String cellContent;
        StringBuilder sb = null;
        List<String> rowData = null;

        int startRowNum = (configation.getStartRowNum() == ExcelImportConfigation.DEFAULT_INT ? 0 : configation.getStartRowNum());
        int startColNum = (configation.getStartColNum() == ExcelImportConfigation.DEFAULT_INT ? 0 : configation.getStartColNum());
        sheet = workBook.getSheetAt(0);// 获取第一个工作簿(Sheet)的内容【注意根据实际需要进行修改】
        rowLength = sheet.getPhysicalNumberOfRows(); // 总行数
        rowLength = (configation.getTotalRowNum() == ExcelImportConfigation.DEFAULT_INT ? rowLength : configation.getTotalRowNum() + startRowNum);
        Map<Integer, String> fieldFormatMap;
        for (int i = startRowNum; i < rowLength; i++) {
            logger.info("[excelImportHelper|importExcel]正在读取第" + i + "行数据");
            rowData = new ArrayList<String>();
            row = sheet.getRow(i);
            colLength = row.getLastCellNum();
            colLength = (configation.getTotalColNum() == ExcelImportConfigation.DEFAULT_INT ? colLength : configation.getTotalColNum() + startColNum);
            for (int j = startColNum; j < colLength; j++) {
                cell = row.getCell(j);
                if(cell == null){
                    cellContent = "";
                }else if(cell.getCellTypeEnum() == CellType.NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)){
                    fieldFormatMap = configation.getFieldFormatMap();
                    cellContent = DateUtils.formatDate(cell.getDateCellValue(),
                            StringUtils.hasText(fieldFormatMap.get(j)) ? fieldFormatMap.get(j) : "yyyy-MM-dd");
                }else if(cell.getCellTypeEnum() == CellType.NUMERIC){
                    cellContent = String.valueOf(cell.getNumericCellValue());
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    cellContent = decimalFormat.format(Double.valueOf(cellContent));
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
