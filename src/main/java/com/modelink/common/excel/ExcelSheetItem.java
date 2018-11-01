package com.modelink.common.excel;

import java.util.List;

public class ExcelSheetItem {

    private List<ColumnDetail> columnDetailList;
    private List<List<CellDetail>> cellValueList;

    public List<ColumnDetail> getColumnDetailList() {
        return columnDetailList;
    }

    public void setColumnDetailList(List<ColumnDetail> columnDetailList) {
        this.columnDetailList = columnDetailList;
    }

    public List<List<CellDetail>> getCellValueList() {
        return cellValueList;
    }

    public void setCellValueList(List<List<CellDetail>> cellValueList) {
        this.cellValueList = cellValueList;
    }
}

