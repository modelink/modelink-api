package com.modelink.admin.vo;

import com.modelink.common.vo.PagerVo;

public class AdvertiseParamPagerVo extends PagerVo {

    private String chooseDate;
    private String columnFieldIds;

    public String getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(String chooseDate) {
        this.chooseDate = chooseDate;
    }

}
