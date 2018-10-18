package com.modelink.reservation.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class HuaxiaDataReportVo implements Serializable {

    private Long id;
    private String date;
    private String dataSource;
    private Integer pcCount;
    private Integer wapCount;
    /** 微信端（去重后） **/
    private Integer weixinCount;
    /** 小米 **/
    private Integer xiaomiCount;
    /** 有效 **/
    private Integer validCount;
    /** 营销标记电话 **/
    private Integer flagCount;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getPcCount() {
        return pcCount;
    }

    public void setPcCount(Integer pcCount) {
        this.pcCount = pcCount;
    }

    public Integer getWapCount() {
        return wapCount;
    }

    public void setWapCount(Integer wapCount) {
        this.wapCount = wapCount;
    }

    public Integer getWeixinCount() {
        return weixinCount;
    }

    public void setWeixinCount(Integer weixinCount) {
        this.weixinCount = weixinCount;
    }

    public Integer getXiaomiCount() {
        return xiaomiCount;
    }

    public void setXiaomiCount(Integer xiaomiCount) {
        this.xiaomiCount = xiaomiCount;
    }

    public Integer getValidCount() {
        return validCount;
    }

    public void setValidCount(Integer validCount) {
        this.validCount = validCount;
    }

    public Integer getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(Integer flagCount) {
        this.flagCount = flagCount;
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
