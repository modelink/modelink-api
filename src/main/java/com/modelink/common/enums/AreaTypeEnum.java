package com.modelink.common.enums;

/**
 * 平台枚举
 */
public enum AreaTypeEnum {

    省(1, "省"),
    市(2, "市"),
    区(3, "区");

    private int value;
    private String text;

    private AreaTypeEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(AreaTypeEnum enumItem : AreaTypeEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
    }

    public static int getValueByText(String text) {
        for(AreaTypeEnum enumItem : AreaTypeEnum.values()){
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
