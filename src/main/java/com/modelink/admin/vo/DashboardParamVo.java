package com.modelink.admin.vo;

import java.io.Serializable;

public class DashboardParamVo implements Serializable {

    /** 合作商户ID **/
    private Long merchantId;
    /** 日期类型 **/
    private Integer dateType;
    /** 日期值 **/
    private String chooseDate;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    public String getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(String chooseDate) {
        this.chooseDate = chooseDate;
    }
}
