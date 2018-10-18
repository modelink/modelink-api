package com.modelink.admin.vo.basedata;

import com.modelink.common.vo.PagerVo;

public class InsuranceParamPagerVo extends PagerVo {

    private String chooseDate;
    private String mobile;
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

    public String getColumnFieldIds() {
        return columnFieldIds;
    }

    public void setColumnFieldIds(String columnFieldIds) {
        this.columnFieldIds = columnFieldIds;
    }
}
