package com.modelink.common.enums;

/**
 * 平台枚举
 */
public enum HXReserveGoodsEnum {

    其他产品(0, "其他产品"),
    专题首页(1, "专题首页"),
    常青树(2, "常青树"),
    华夏福(3, "华夏福"),
    常青藤(4, "常青藤"),
    福临门(5, "福临门");

    private int value;
    private String text;

    private HXReserveGoodsEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(HXReserveGoodsEnum enumItem : HXReserveGoodsEnum.values()){
            if(enumItem.getValue() == value){
                return enumItem.getText();
            }
        }
        return "";
    }

    public static int getValueByText(String text) {
        for(HXReserveGoodsEnum enumItem : HXReserveGoodsEnum.values()){
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
