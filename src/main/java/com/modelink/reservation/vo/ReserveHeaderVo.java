package com.modelink.reservation.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/05/25 0025.
 */
public class ReserveHeaderVo implements Serializable {

    private String appKey;
    private String sign;
    private String nonceStr;
    private int timestamp;
    private int channel;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
