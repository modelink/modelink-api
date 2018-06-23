package com.modelink.common.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 简单工具类
 */
public class ExcelConfigation {

    public static final int DEFAULT_INT = -1;

    /** 实际数据开始的行数 **/
    private int startRowNum = DEFAULT_INT;
    /** 实际数据开始的列数 **/
    private int startColNum = DEFAULT_INT;

    /** 实际数据占用的行数 **/
    private int totalRowNum = DEFAULT_INT;
    /** 实际数据占用的列数 **/
    private int totalColNum = DEFAULT_INT;

    private String fileName;
    private List<String> sheetNameList;
    private Map<String, SheetItem> sheetContentMap;

    public static ExcelConfigation newInstance(String fileName, List<String> columnNameList, List<List<String>> dataList){
        ExcelConfigation excelConfigation = new ExcelConfigation();
        excelConfigation.setFileName(fileName);

        List<String> sheetNameList = new ArrayList<>();
        sheetNameList.add("sheet_1");
        excelConfigation.setSheetNameList(sheetNameList);


        SheetItem sheetItem = new SheetItem();
        sheetItem.setColumnNameList(columnNameList);
        sheetItem.setCellValueList(dataList);
        Map<String, SheetItem> dataMap = new HashMap<>();
        dataMap.put("sheet_1", sheetItem);
        excelConfigation.setSheetContentMap(dataMap);

        return excelConfigation;
    }

    public int getStartRowNum() {
        return startRowNum;
    }
    public void setStartRowNum(int startRowNum) {
        this.startRowNum = startRowNum;
    }
    public int getStartColNum() {
        return startColNum;
    }
    public void setStartColNum(int startColNum) {
        this.startColNum = startColNum;
    }
    public int getTotalRowNum() {
        return totalRowNum;
    }
    public void setTotalRowNum(int totalRowNum) {
        this.totalRowNum = totalRowNum;
    }
    public int getTotalColNum() {
        return totalColNum;
    }
    public void setTotalColNum(int totalColNum) {
        this.totalColNum = totalColNum;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getSheetNameList() {
        return sheetNameList;
    }

    public void setSheetNameList(List<String> sheetNameList) {
        this.sheetNameList = sheetNameList;
    }

    public Map<String, SheetItem> getSheetContentMap() {
        return sheetContentMap;
    }

    public void setSheetContentMap(Map<String, SheetItem> sheetContentMap) {
        this.sheetContentMap = sheetContentMap;
    }
}

class SheetItem {

    private List<String> columnNameList;
    private List<List<String>> cellValueList;

    public List<String> getColumnNameList() {
        return columnNameList;
    }

    public void setColumnNameList(List<String> columnNameList) {
        this.columnNameList = columnNameList;
    }

    public List<List<String>> getCellValueList() {
        return cellValueList;
    }

    public void setCellValueList(List<List<String>> cellValueList) {
        this.cellValueList = cellValueList;
    }
}

