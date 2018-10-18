package com.modelink.common.excel;

import java.util.List;

public class ExcelSheetItem {

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
