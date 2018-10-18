package com.modelink.admin.vo.common;

import com.modelink.common.vo.PagerVo;

public class AreaParamPagerVo extends PagerVo {

    private String areaId;
    private String areaName;
    private String areaType;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }
}
