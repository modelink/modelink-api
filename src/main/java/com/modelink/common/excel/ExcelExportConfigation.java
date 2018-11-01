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
    private List<SheetDetail> sheetDetailList;
    private Map<String, List<ExcelSheetItem>> sheetContentMap;

    public static ExcelExportConfigation newInstance(String fileName, List<String> columnNameList, List<List<String>> dataList){
        ExcelExportConfigation excelConfigation = new ExcelExportConfigation();
        excelConfigation.setFileName(fileName);

        SheetDetail sheetDetail;
        List<SheetDetail> sheetDetailList = new ArrayList<>();
        sheetDetail = new SheetDetail();
        sheetDetail.setName("sheet_1");
        sheetDetail.setFreezePane("");
        sheetDetailList.add(sheetDetail);
        excelConfigation.setSheetDetailList(sheetDetailList);

        ColumnDetail columnDetail;
        List<ColumnDetail> columnDetailList = new ArrayList<>();
        for (String columnName : columnNameList) {
            columnDetail = new ColumnDetail();
            columnDetail.setWidth(6000);
            columnDetail.setName(columnName);
            columnDetail.setDataType("string");
            columnDetailList.add(columnDetail);
        }

        CellDetail cellDetail;
        List<CellDetail> detailList;
        List<List<CellDetail>> cellDetailList = new ArrayList<>();
        for (List<String> dataItem : dataList) {
            detailList = new ArrayList<>();
            for (String value : dataItem) {
                cellDetail = new CellDetail();
                cellDetail.setValue(value);
                detailList.add(cellDetail);
            }
            cellDetailList.add(detailList);
        }


        ExcelSheetItem excelSheetItem = new ExcelSheetItem();
        excelSheetItem.setColumnDetailList(columnDetailList);
        excelSheetItem.setCellValueList(cellDetailList);
        Map<String, List<ExcelSheetItem>> dataMap = new HashMap<>();
        List<ExcelSheetItem> sheetItemList = new ArrayList<>();
        sheetItemList.add(excelSheetItem);
        dataMap.put("sheet_1", sheetItemList);
        excelConfigation.setSheetContentMap(dataMap);

        return excelConfigation;
    }
    public static ExcelExportConfigation newInstanceWithColumn(String fileName, List<ColumnDetail> columnDetailList, List<List<String>> dataList){
        ExcelExportConfigation excelConfigation = new ExcelExportConfigation();
        excelConfigation.setFileName(fileName);

        SheetDetail sheetDetail;
        List<SheetDetail> sheetDetailList = new ArrayList<>();
        sheetDetail = new SheetDetail();
        sheetDetail.setName("sheet_1");
        sheetDetail.setFreezePane("");
        sheetDetailList.add(sheetDetail);
        excelConfigation.setSheetDetailList(sheetDetailList);

        CellDetail cellDetail;
        List<CellDetail> detailList;
        List<List<CellDetail>> cellDetailList = new ArrayList<>();
        for (List<String> dataItem : dataList) {
            detailList = new ArrayList<>();
            for (String value : dataItem) {
                cellDetail = new CellDetail();
                cellDetail.setValue(value);
                detailList.add(cellDetail);
            }
            cellDetailList.add(detailList);
        }

        ExcelSheetItem excelSheetItem = new ExcelSheetItem();
        excelSheetItem.setColumnDetailList(columnDetailList);
        excelSheetItem.setCellValueList(cellDetailList);
        Map<String, List<ExcelSheetItem>> dataMap = new HashMap<>();
        List<ExcelSheetItem> sheetItemList = new ArrayList<>();
        sheetItemList.add(excelSheetItem);
        dataMap.put("sheet_1", sheetItemList);
        excelConfigation.setSheetContentMap(dataMap);

        return excelConfigation;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<SheetDetail> getSheetDetailList() {
        return sheetDetailList;
    }

    public void setSheetDetailList(List<SheetDetail> sheetDetailList) {
        this.sheetDetailList = sheetDetailList;
    }

    public Map<String, List<ExcelSheetItem>> getSheetContentMap() {
        return sheetContentMap;
    }

    public void setSheetContentMap(Map<String, List<ExcelSheetItem>> sheetContentMap) {
        this.sheetContentMap = sheetContentMap;
    }
}

