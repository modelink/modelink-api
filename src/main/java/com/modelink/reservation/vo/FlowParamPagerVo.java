package com.modelink.reservation.vo;

import com.modelink.common.vo.PagerVo;

public class FlowParamPagerVo extends PagerVo {

    private String dateField;
    private String chooseDate;
    private Long merchantId;
    private String platformName;
    private String columnFieldIds;
    private String source;

    public String getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(String chooseDate) {
        this.chooseDate = chooseDate;
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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
