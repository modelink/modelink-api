package com.modelink.reservation.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/05/25 0025.
 */
public class ReserveHeaderVo implements Serializable {

    private String appKey;
    private String sign;

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
}
