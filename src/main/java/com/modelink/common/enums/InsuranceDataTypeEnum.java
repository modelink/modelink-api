package com.modelink.common.enums;

/**
 * 缴费类型枚举
 */
public enum InsuranceDataTypeEnum {

    SEM(1, "SEM"),
    自然流量(2, "自然流量");

    private int value;
    private String text;

    private InsuranceDataTypeEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(InsuranceDataTypeEnum enumItem : InsuranceDataTypeEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
    }

    public static int getValueByText(String text) {
        for(PlatformEnum platformEnum : PlatformEnum.values()){
            if(platformEnum.getText().equals(text)){
                return platformEnum.getValue();
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
