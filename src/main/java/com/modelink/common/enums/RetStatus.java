package com.modelink.common.enums;

/**
 * Created by Administrator on 2018/05/24 0024.
 */
public enum RetStatus {

    Ok(200, "请求成功"),
    Fail(404, "请求失败"),
    Exception(500, "请求异常");

    private int value;
    private String text;

    private RetStatus(int value, String text){
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
