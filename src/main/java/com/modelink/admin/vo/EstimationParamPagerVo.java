package com.modelink.admin.vo;

import com.modelink.common.vo.PagerVo;

public class EstimationParamPagerVo extends PagerVo {

    private String chooseDate;
    private String contactMobile;

    public String getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(String chooseDate) {
        this.chooseDate = chooseDate;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }
}
