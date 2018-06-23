package com.modelink.common.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Excel 导出文件助手
 */
public class ExcelExportHelper {

    public static Logger logger = LoggerFactory.getLogger(ExcelExportHelper.class);

    /**
     * 生成 Excel 数据流至网页下载流 Response
     * @param excelConfigation
     * @param response
     * @throws Exception
     */
    public static void exportExcel2Response(ExcelConfigation excelConfigation, HttpServletResponse response) throws Exception {

        XSSFWorkbook workbook = buildExcelWorkbook(excelConfigation);
        // 将Excel放入响应头里面
        String fileName;
        ServletOutputStream outStream = null;
        try {
            fileName = new String(excelConfigation.getFileName().getBytes("UTF-8"), "ISO-8859-1");
            response.reset();
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            outStream = response.getOutputStream();
            workbook.write(outStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outStream.close();
        }
    }

    /**
     * 生成 Excel 本地文件
     * @param excelConfigation
     * @param fileName
     * @throws Exception
     */
    public static void exportExcel2File(ExcelConfigation excelConfigation, String fileName) throws Exception {
        XSSFWorkbook workbook = buildExcelWorkbook(excelConfigation);
        /*** 这里是问题的关键，将这个工作簿写入到一个流中就可以输出相应的名字，这里需要写路径就ok了。 **/
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    private static XSSFWorkbook buildExcelWorkbook (ExcelConfigation excelConfigation) throws Exception {
        // 创建Excel的工作书册 Workbook,对应到一个excel文档
        XSSFWorkbook workbook = new XSSFWorkbook();

        if(excelConfigation == null || excelConfigation.getSheetNameList() == null
                || excelConfigation.getSheetNameList().size() <= 0){
            throw new Exception("Excel数据不全");
        }
        // 创建Excel的工作sheet,对应到一个excel文档的tab
        Sheet sheet;
        SheetItem sheetItem;
        Row headerRow, contentRow;
        XSSFFont headerFont, contentFont;
        XSSFCellStyle headerStyle, contentStyle;
        int columnIndex, rowIndex;
        for (String sheetName : excelConfigation.getSheetNameList()) {

            sheetItem = excelConfigation.getSheetContentMap().get(sheetName);
            if (sheetItem == null) {
                continue;
            }

            sheet = workbook.createSheet(sheetName);
            // 设置excel每列宽度
            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 6000);
            sheet.setColumnWidth(2, 4000);

            // 创建字体样式
            headerFont = workbook.createFont();
            headerFont.setFontName("Verdana");
            headerFont.setBold(true);
            headerFont.setFontHeight((short) 300);
            headerFont.setColor(IndexedColors.BLACK.index);
            // 创建单元格样式
            headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.WHITE.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // 设置边框
            headerStyle.setBottomBorderColor(IndexedColors.BLACK.index);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            // 设置字体
            headerStyle.setFont(headerFont);

            // 创建Excel的sheet的一行
            rowIndex = 0;
            headerRow = sheet.createRow(rowIndex);
            headerRow.setHeight((short) 500);// 设定行的高度

            Cell cell;
            columnIndex = 0;
            for (String columnName : sheetItem.getColumnNameList()) {
                // 创建一个Excel的单元格
                cell = headerRow.createCell(columnIndex);
                // 给Excel的单元格设置样式和赋值
                cell.setCellStyle(headerStyle);
                cell.setCellValue(columnName);

                columnIndex++;
            }
            rowIndex ++;


            // 创建字体样式
            contentFont = workbook.createFont();
            contentFont.setFontName("Verdana");
            contentFont.setBold(false);
            contentFont.setFontHeight((short) 200);
            contentFont.setColor(IndexedColors.BLACK.index);
            // 创建单元格样式
            contentStyle = workbook.createCellStyle();
            contentStyle.setAlignment(HorizontalAlignment.CENTER);
            contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentStyle.setFillForegroundColor(IndexedColors.WHITE.index);
            contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // 设置边框
            contentStyle.setBottomBorderColor(IndexedColors.BLACK.index);
            contentStyle.setBorderBottom(BorderStyle.THIN);
            contentStyle.setBorderLeft(BorderStyle.THIN);
            contentStyle.setBorderRight(BorderStyle.THIN);
            contentStyle.setBorderTop(BorderStyle.THIN);
            // 设置字体
            contentStyle.setFont(contentFont);
            for (List<String> rowItem : sheetItem.getCellValueList()) {
                columnIndex = 0;
                contentRow = sheet.createRow(rowIndex);
                for (String cellValue : rowItem) {
                    // 创建一个Excel的单元格
                    cell = contentRow.createCell(columnIndex);
                    // 给Excel的单元格设置样式和赋值
                    cell.setCellStyle(contentStyle);
                    cell.setCellValue(cellValue);

                    columnIndex++;
                }
                rowIndex++;
            }
        }
        return workbook;
    }

}
