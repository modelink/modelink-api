package com.modelink.common.enums;

/**
 * 平台枚举
 */
public enum DateTypeEnum {

    年(1, "年"),
    季度(2, "季度"),
    月(3, "月"),
    周(4, "周"),
    日(5, "日");

    private int value;
    private String text;

    private DateTypeEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(DateTypeEnum enumItem : DateTypeEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
    }

    public static int getValueByText(String text) {
        for(DateTypeEnum enumItem : DateTypeEnum.values()){
            if(enumItem.getText().equals(text)){
                return enumItem.getValue();
            }
        }
        return 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
