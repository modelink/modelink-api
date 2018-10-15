package com.modelink.common.enums;

/**
 * 平台枚举
 */
public enum HXEstimateGoodsEnum {

    其他产品(0, "其他产品"),
    常青树智慧版(1, "常青树智慧版");

    private int value;
    private String text;

    private HXEstimateGoodsEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(HXEstimateGoodsEnum enumItem : HXEstimateGoodsEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
    }

    public static int getValueByText(String text) {
        for(HXEstimateGoodsEnum enumItem : HXEstimateGoodsEnum.values()){
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
