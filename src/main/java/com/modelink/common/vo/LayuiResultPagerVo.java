package com.modelink.common.vo;

import com.modelink.common.enums.RetStatus;

import java.util.List;

public class LayuiResultPagerVo<T> {

    private int rtnCode = RetStatus.Ok.getValue();
    private String rtnMsg = RetStatus.Ok.getText();
    private int totalCount;
    private List<T> rtnList;

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getRtnList() {
        return rtnList;
    }

    public void setRtnList(List<T> rtnList) {
        this.rtnList = rtnList;
    }
}
