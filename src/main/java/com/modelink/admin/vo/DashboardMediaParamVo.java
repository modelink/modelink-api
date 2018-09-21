package com.modelink.admin.vo;

import java.io.Serializable;

public class DashboardMediaParamVo extends DashboardParamVo {

    /** 来源 **/
    private String feeType;

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
}
