package com.modelink.common.enums;

/**
 * 保单状态类型枚举
 */
public enum InsuranceStatusEnum {

    保单取消(1, "保单取消"),
    保单终止(2, "保单终止");

    private int value;
    private String text;

    private InsuranceStatusEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(InsuranceStatusEnum enumItem : InsuranceStatusEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
    }

    public static int getValueByText(String text) {
        for(InsuranceStatusEnum enumItem : InsuranceStatusEnum.values()){
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
