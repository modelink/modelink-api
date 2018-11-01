package com.modelink.common.excel;

public class ColumnDetail {
    private int width;
    private String name;
    private String dataType;
    private boolean autoColumnSize = false;

    public ColumnDetail(){

    }

    public ColumnDetail(String name, int width, String dataType){
        this.name = name;
        this.width = width;
        this.dataType = dataType;
        this.autoColumnSize = false;
        if (width == 0) {
            this.autoColumnSize = true;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isAutoColumnSize() {
        return autoColumnSize;
    }

    public void setAutoColumnSize(boolean autoColumnSize) {
        this.autoColumnSize = autoColumnSize;
    }
}
