package com.modelink.reservation.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/05/25 0025.
 */
public class ReserveHeaderVo implements Serializable {

    private Long appKey;
    private String sign;
    private String nonceStr;
    private Long timestamp;
    private Long channel;

    public Long getAppKey() {
        return appKey;
    }

    public void setAppKey(Long appKey) {
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getChannel() {
        return channel;
    }

    public void setChannel(Long channel) {
        this.channel = channel;
    }
}
