package com.modelink.common.excel;

import java.util.HashMap;
import java.util.Map;

public class ExcelImportConfigation {

    public static final int DEFAULT_INT = -1;

    /** 实际数据开始的行数 **/
    private int startRowNum = DEFAULT_INT;
    /** 实际数据开始的列数 **/
    private int startColNum = DEFAULT_INT;

    /** 实际数据占用的行数 **/
    private int totalRowNum = DEFAULT_INT;
    /** 实际数据占用的列数 **/
    private int totalColNum = DEFAULT_INT;

    private Map<Integer, String> fieldFormatMap = new HashMap<>();

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

    public Map<Integer, String> getFieldFormatMap() {
        return fieldFormatMap;
    }

    public void setFieldFormatMap(Map<Integer, String> fieldFormatMap) {
        this.fieldFormatMap = fieldFormatMap;
    }
}
