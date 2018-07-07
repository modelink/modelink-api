package com.modelink.admin.vo;

import java.io.Serializable;

public class AreaVo implements Serializable {

    private Integer areaId;
    /** 父级地区ID **/
    private String parentName;
    /** 地区名称 **/
    private String areaName;
    /** 地区类型（1-省；2-市；3-区） **/
    private String areaType;
    /** 数据状态（0=正常；1-禁用） **/
    private String status;
    /** 数据备注 **/
    private String remark;
    /** 创建时间 **/
    private String createTime;
    /** 更新时间 **/
    private String updateTime;

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
