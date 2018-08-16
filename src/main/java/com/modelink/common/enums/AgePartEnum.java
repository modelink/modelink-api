package com.modelink.common.enums;

/**
 * 平台枚举
 */
public enum AgePartEnum {

    from_0_5(0, "0-5岁"),
    from_5_18(5, "5-18岁"),
    from_18_25(18, "18-25岁"),
    from_25_30(25, "25-30岁"),
    from_30_35(30, "30-35岁"),
    from_35_40(35, "35-40岁"),
    from_40_50(40, "40-50岁"),
    from_50_55(50, "50-55岁"),
    from_55_100(55, "55以上");

    private int value;
    private String text;

    private AgePartEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(AgePartEnum enumItem : AgePartEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
    }

    public static int getValueByText(String text) {
        for(AgePartEnum enumItem : AgePartEnum.values()){
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
