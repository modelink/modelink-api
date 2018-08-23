package com.modelink.reservation.vo;

import com.modelink.common.vo.PagerVo;

public class FlowReserveParamPagerVo extends PagerVo {

    private String dateField;
    private String chooseDate;
    private String mobile;
    private Long merchantId;
    private String platformName;
    private String advertiseActive;
    private String columnFieldIds;

    public String getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(String chooseDate) {
        this.chooseDate = chooseDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getColumnFieldIds() {
        return columnFieldIds;
    }

    public void setColumnFieldIds(String columnFieldIds) {
        this.columnFieldIds = columnFieldIds;
    }

    public String getDateField() {
        return dateField;
    }

    public void setDateField(String dateField) {
        this.dateField = dateField;
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
}
