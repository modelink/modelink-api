package com.modelink.common.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 简单工具类
 */
public class ExcelExportConfigation {

    private String fileName;
    private List<String> sheetNameList;
    private Map<String, ExcelSheetItem> sheetContentMap;

    public static ExcelExportConfigation newInstance(String fileName, List<String> columnNameList, List<List<String>> dataList){
        ExcelExportConfigation excelConfigation = new ExcelExportConfigation();
        excelConfigation.setFileName(fileName);

        List<String> sheetNameList = new ArrayList<>();
        sheetNameList.add("sheet_1");
        excelConfigation.setSheetNameList(sheetNameList);


        ExcelSheetItem excelSheetItem = new ExcelSheetItem();
        excelSheetItem.setColumnNameList(columnNameList);
        excelSheetItem.setCellValueList(dataList);
        Map<String, ExcelSheetItem> dataMap = new HashMap<>();
        dataMap.put("sheet_1", excelSheetItem);
        excelConfigation.setSheetContentMap(dataMap);

        return excelConfigation;
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

    public Map<String, ExcelSheetItem> getSheetContentMap() {
        return sheetContentMap;
    }

    public void setSheetContentMap(Map<String, ExcelSheetItem> sheetContentMap) {
        this.sheetContentMap = sheetContentMap;
    }
}

