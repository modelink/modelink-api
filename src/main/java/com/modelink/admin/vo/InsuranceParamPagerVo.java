package com.modelink.admin.vo;

import com.modelink.common.vo.PagerVo;

public class InsuranceParamPagerVo extends PagerVo {

    private String chooseDate;
    private String mobile;

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
}
