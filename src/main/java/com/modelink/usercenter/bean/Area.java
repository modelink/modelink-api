package com.modelink.usercenter.bean;

import javax.persistence.Id;
import java.util.Date;

public class Area {

    @Id
    private Integer areaId;
    /** 父级地区ID **/
    private Integer parentId;
    /** 地区名称 **/
    private String areaName;
    /** 地区类型（1-省；2-市；3-区） **/
    private Integer areaType;
    /** 数据状态（0=正常；1-禁用） **/
    private Integer status = 0;
    /** 数据备注 **/
    private String remark;
    /** 创建时间 **/
    private Date createTime;
    /** 更新时间 **/
    private Date updateTime;

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getAreaType() {
        return areaType;
    }

    public void setAreaType(Integer areaType) {
        this.areaType = areaType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
