package com.modelink.common.enums;

/**
 * 缴费类型枚举
 */
public enum InsurancePayTypeEnum {

    月缴(1, "月交"),
    年缴(2, "年交");

    private int value;
    private String text;

    private InsurancePayTypeEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(InsurancePayTypeEnum enumItem : InsurancePayTypeEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
    }

    public static int getValueByText(String text) {
        for(InsurancePayTypeEnum enumItem : InsurancePayTypeEnum.values()){
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
