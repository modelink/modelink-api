package com.modelink.common.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static void exportExcel2Response(ExcelExportConfigation excelConfigation, HttpServletResponse response) throws Exception {

        SXSSFWorkbook workbook = buildExcelWorkbook(excelConfigation);
        // 将Excel放入响应头里面
        String fileName;
        ServletOutputStream outStream = null;
        try {
            fileName = new String(excelConfigation.getFileName().getBytes("UTF-8"), "ISO-8859-1");
            response.reset();
            response.setContentType("application/x-msdownload");
            response.setHeader("content-disposition", "attachment; filename=" + fileName + ".xlsx");
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
    public static void exportExcel2File(ExcelExportConfigation excelConfigation, String fileName) throws Exception {
        SXSSFWorkbook workbook = buildExcelWorkbook(excelConfigation);
        /*** 这里是问题的关键，将这个工作簿写入到一个流中就可以输出相应的名字，这里需要写路径就ok了。 **/
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    private static SXSSFWorkbook buildExcelWorkbook (ExcelExportConfigation excelConfigation) throws Exception {
        // 创建Excel的工作书册 Workbook,对应到一个excel文档
        SXSSFWorkbook workbook = new SXSSFWorkbook(10000);

        if(excelConfigation == null || excelConfigation.getSheetDetailList() == null
                || excelConfigation.getSheetDetailList().size() <= 0){
            throw new Exception("Excel数据不全");
        }
        // 创建Excel的工作sheet,对应到一个excel文档的tab
        SXSSFSheet sheet;
        ColumnDetail columnInfo;
        Row headerRow, contentRow;
        Font headerFont;
        CellStyle headerStyle;
        int columnIndex, rowIndex;
        List<ColumnDetail> columnDetailList;
        List<ExcelSheetItem> sheetItemList;


        CellStyle contentCellStyle;
        String[] freezePaneArray;
        Map<String, CellStyle> cellStyleMap = new HashMap<>();
        for (SheetDetail sheetDetail : excelConfigation.getSheetDetailList()) {
            sheetItemList = excelConfigation.getSheetContentMap().get(sheetDetail.getName());
            if (sheetItemList == null || sheetItemList.size() <= 0) {
                continue;
            }

            sheet = workbook.createSheet(sheetDetail.getName());

            // 创建字体样式
            headerFont = workbook.createFont();
            headerFont.setFontName("微软雅黑");
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setColor(IndexedColors.BLACK.index);
            // 创建单元格样式
            headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
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
            Cell cell;
            rowIndex = 0;
            for (ExcelSheetItem excelSheetItem : sheetItemList) {
                columnIndex = 0;
                headerRow = sheet.createRow(rowIndex);
                headerRow.setHeight((short) 500);// 设定行的高度
                for (ColumnDetail columnDetail : excelSheetItem.getColumnDetailList()) {
                    // 创建一个Excel的单元格
                    cell = headerRow.createCell(columnIndex);
                    // 给Excel的单元格设置样式和赋值
                    cell.setCellStyle(headerStyle);
                    cell.setCellValue(columnDetail.getName());

                    columnIndex ++;
                }
                rowIndex ++;

                columnDetailList = excelSheetItem.getColumnDetailList();
                for (List<CellDetail> rowItem : excelSheetItem.getCellValueList()) {
                    columnIndex = 0;
                    contentRow = sheet.createRow(rowIndex);
                    for (CellDetail cellValue : rowItem) {
                        try {
                            // 创建一个Excel的单元格
                            cell = contentRow.createCell(columnIndex);
                            // 给Excel的单元格设置样式和赋值
                            columnInfo = columnDetailList.get(columnIndex);

                            cellValue.setDataType(columnInfo.getDataType());
                            contentCellStyle = getCellStyleByParam(cellStyleMap, workbook, cellValue);
                            if ("string".equals(columnInfo.getDataType())) {
                                cell.setCellStyle(contentCellStyle);
                                cell.setCellValue(cellValue.getValue());
                            } else if ("integer".equals(columnInfo.getDataType()) && StringUtils.hasText(cellValue.getValue())) {
                                cell.setCellStyle(contentCellStyle);
                                cell.setCellValue(Integer.parseInt(cellValue.getValue()));
                            } else if ("double".equals(columnInfo.getDataType()) && StringUtils.hasText(cellValue.getValue())) {
                                cell.setCellStyle(contentCellStyle);
                                cell.setCellValue(Double.parseDouble(cellValue.getValue()));
                            } else if ("percent".equals(columnInfo.getDataType()) && StringUtils.hasText(cellValue.getValue())) {
                                cell.setCellStyle(contentCellStyle);
                                cell.setCellValue(Double.parseDouble(cellValue.getValue()));
                            } else {
                                cell.setCellStyle(contentCellStyle);
                                cell.setCellValue(cellValue.getValue());
                            }

                            columnIndex++;
                        } catch (Exception e) {
                            logger.error("sheetName={}, columnIndex={}, rowIndex={}", sheetDetail.getName(), columnIndex, rowIndex, e);
                        }
                    }
                    rowIndex++;
                }

                // 设置excel每列宽度
                columnIndex = 0;
                sheet.trackAllColumnsForAutoSizing();
                for (ColumnDetail columnDetail : excelSheetItem.getColumnDetailList()) {
                    if (columnDetail.isAutoColumnSize()) {
                        sheet.autoSizeColumn(columnIndex);
                    } else {
                        sheet.setColumnWidth(columnIndex, columnDetail.getWidth());
                    }
                    columnIndex ++;
                }

                rowIndex = rowIndex + 4;
            }

            // 设置冻结单元格
            if (StringUtils.hasText(sheetDetail.getFreezePane())) {
                freezePaneArray = sheetDetail.getFreezePane().split(",");
                sheet.createFreezePane(Integer.parseInt(freezePaneArray[0]),
                        Integer.parseInt(freezePaneArray[1]),
                        Integer.parseInt(freezePaneArray[2]),
                        Integer.parseInt(freezePaneArray[3]));
            }
        }
        return workbook;
    }

    private static CellStyle getCellStyleByParam(Map<String, CellStyle> cellStyleMap, SXSSFWorkbook workbook, CellDetail cellDetail){
        StringBuilder cellKey = new StringBuilder();
        cellKey.append(cellDetail.getDataType());
        cellKey.append(cellDetail.getFontColor());
        cellKey.append(cellDetail.getFontName());
        cellKey.append(cellDetail.getFontSize());

        Font fontStyle;
        DataFormat dataFormat = workbook.createDataFormat(); // 此处设置数据格式
        CellStyle cellStyle = cellStyleMap.get(cellKey.toString());
        if (cellStyle == null) {
            // 创建字体样式
            fontStyle = workbook.createFont();
            fontStyle.setFontName(cellDetail.getFontName());
            fontStyle.setBold(false);
            fontStyle.setFontHeightInPoints(cellDetail.getFontSize());
            fontStyle.setColor(cellDetail.getFontColor());

            // 创建单元格样式
            cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // 设置边框
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setFont(fontStyle);
            if ("integer".equals(cellDetail.getDataType())) {
                cellStyle.setDataFormat(dataFormat.getFormat("#,##0"));
            } else if ("double".equals(cellDetail.getDataType())) {
                cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
            } else if ("percent".equals(cellDetail.getDataType())) {
                cellStyle.setDataFormat(dataFormat.getFormat("0.00%"));
            }

            cellStyleMap.put(cellKey.toString(), cellStyle);
        }
        return cellStyle;
    }

    private static Map<String, CellStyle> formCellStyleMap(SXSSFWorkbook workbook){
        Font fontStyle;
        CellStyle cellStyle;
        DataFormat dataFormat = workbook.createDataFormat(); // 此处设置数据格式

        // 创建字体样式
        fontStyle = workbook.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setBold(false);
        fontStyle.setFontHeightInPoints((short) 9);
        fontStyle.setColor(IndexedColors.BLACK.index);

        // 创建单元格样式的缓存
        Map<String, CellStyle> cellStyleMap = new HashMap<>();
        // 创建单元格样式
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setFont(fontStyle);
        cellStyleMap.put("string", cellStyle);

        // 创建单元格样式
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setFont(fontStyle);
        cellStyle.setDataFormat(dataFormat.getFormat("#,##0"));
        cellStyleMap.put("integer", cellStyle);

        // 创建单元格样式
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setFont(fontStyle);
        cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
        cellStyleMap.put("double", cellStyle);

        // 创建单元格样式
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setFont(fontStyle);
        cellStyle.setDataFormat(dataFormat.getFormat("0.00%"));
        cellStyleMap.put("percent", cellStyle);
        return cellStyleMap;
    }
}
