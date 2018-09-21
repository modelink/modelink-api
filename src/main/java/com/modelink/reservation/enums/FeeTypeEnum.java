package com.modelink.reservation.enums;

public enum FeeTypeEnum {

    FEE_TYPE_RESERVE(0, "预约"),
    FEE_TYPE_ESTIMATE(1, "测保");

    private int value;
    private String text;
    private FeeTypeEnum(int value, String text){
        this.text = text;
        this.value = value;
    }

    public static String getTextByValue(int value){
        for(FeeTypeEnum enumItem : FeeTypeEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
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
