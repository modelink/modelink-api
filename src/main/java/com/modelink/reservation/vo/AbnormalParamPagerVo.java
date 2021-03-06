package com.modelink.reservation.vo;

import com.modelink.common.vo.PagerVo;

public class AbnormalParamPagerVo extends PagerVo {

    private Long merchantId;
    private String problemData;
    private String dateField;
    private String chooseDate;
    private String columnFieldIds;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getProblemData() {
        return problemData;
    }

    public void setProblemData(String problemData) {
        this.problemData = problemData;
    }

    public String getDateField() {
        return dateField;
    }

    public void setDateField(String dateField) {
        this.dateField = dateField;
    }

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
}
