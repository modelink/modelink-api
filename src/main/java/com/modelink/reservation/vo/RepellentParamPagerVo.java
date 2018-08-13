package com.modelink.reservation.vo;

import com.modelink.common.vo.PagerVo;

public class RepellentParamPagerVo extends PagerVo {

    private String chooseDate;
    private String columnFieldIds;

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
