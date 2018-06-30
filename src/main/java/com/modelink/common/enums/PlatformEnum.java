package com.modelink.common.enums;

/**
 * 平台枚举
 */
public enum PlatformEnum {

    PC(200, "请求成功"),
    WAP(404, "请求失败"),
    Exception(500, "请求异常");

    private int value;
    private String text;

    private PlatformEnum(int value, String text){
        this.value = value;
        this.text = text;
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
