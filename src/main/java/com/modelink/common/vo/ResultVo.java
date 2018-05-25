package com.modelink.common.vo;

import com.modelink.common.enums.RetStatus;

import java.io.Serializable;

public class ResultVo<T> implements Serializable {

    private int rtnCode = RetStatus.Ok.getValue();
    private String rtnMsg = RetStatus.Ok.getText();
    private T data;

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
