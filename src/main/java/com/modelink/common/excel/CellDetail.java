package com.modelink.common.excel;

import org.apache.poi.ss.usermodel.IndexedColors;

public class CellDetail {

    private String dataType = "string";
    private short fontSize = 9;
    private String fontName = "微软雅黑";
    private short fontColor = IndexedColors.BLACK.index;
    private String value;

    public CellDetail() {

    }

    public CellDetail(String value) {
        this.value = value;
    }

    public CellDetail(short fontColor, String value) {
        this.fontColor = fontColor;
        this.value = value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public short getFontSize() {
        return fontSize;
    }

    public void setFontSize(short fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public short getFontColor() {
        return fontColor;
    }

    public void setFontColor(short fontColor) {
        this.fontColor = fontColor;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
