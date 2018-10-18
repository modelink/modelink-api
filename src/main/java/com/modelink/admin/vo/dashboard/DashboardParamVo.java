package com.modelink.admin.vo.dashboard;

import java.io.Serializable;

public class DashboardParamVo implements Serializable {

    /** 合作商户ID **/
    private Long merchantId;
    /** 日期类型 **/
    private Integer dateType;
    /** 渠道归属 **/
    private String platformName;
    /** 广告活动 **/
    private String advertiseActive;
    /** 日期值 **/
    private String chooseDate;
    /** 来源 **/
    private String source;

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

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAdvertiseActive() {
        return advertiseActive;
    }

    public void setAdvertiseActive(String advertiseActive) {
        this.advertiseActive = advertiseActive;
    }

    public String getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(String chooseDate) {
        this.chooseDate = chooseDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
