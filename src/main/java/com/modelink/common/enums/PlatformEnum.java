package com.modelink.common.enums;

/**
 * 平台枚举
 */
public enum PlatformEnum {

    PC(100, "PC"),
    WAP(200, "WAP"),
    WEIXIN(300, "微信"),
    XIAOMI(400, "小米"),
    RECOMMEND(900, "介绍");

    private int value;
    private String text;

    private PlatformEnum(int value, String text){
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(int value) {
        for(PlatformEnum platformEnum : PlatformEnum.values()){
            if(platformEnum.getValue() == value){
                return platformEnum.getText();
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
