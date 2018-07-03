package com.modelink.reservation.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class AdvertiseAnalyse {

    @Id
    private Long id;
    /** 统计时间 **/
    private Date statTime;
    /** 展现量 **/
    private Integer viewCount;
    /** 点击量 **/
    private Integer clickCount;
    /** 浏览量 **/
    private Integer browseCount;
    /** 到达量 **/
    private Integer arriveCount;
    /** 到达用户量 **/
    private Integer arriveUserCount;
    /** 到达率 **/
    private Integer arriveRate;
    /** 二跳量 **/
    private Integer againCount;
    /** 二跳率 **/
    private Integer againRate;
    /** 平均停留时间 **/
    private String averageStayTime;
    /** 转化量 **/
    private Integer transformCount;
    /** 直接转化量 **/
    private Integer directTransformCount;
    /** 回归转化量 **/
    private Integer backTransformCount;
    /** 转化成本 **/
    private BigDecimal transformCost;
    /** 保险费用 **/
    private BigDecimal insuranceFee;
    /** 备注信息 **/
    private String remark;
    /** 创建时间 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 更新时间 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Integer browseCount) {
        this.browseCount = browseCount;
    }

    public Integer getArriveCount() {
        return arriveCount;
    }

    public void setArriveCount(Integer arriveCount) {
        this.arriveCount = arriveCount;
    }

    public Integer getArriveUserCount() {
        return arriveUserCount;
    }

    public void setArriveUserCount(Integer arriveUserCount) {
        this.arriveUserCount = arriveUserCount;
    }

    public Integer getArriveRate() {
        return arriveRate;
    }

    public void setArriveRate(Integer arriveRate) {
        this.arriveRate = arriveRate;
    }

    public Integer getAgainCount() {
        return againCount;
    }

    public void setAgainCount(Integer againCount) {
        this.againCount = againCount;
    }

    public Integer getAgainRate() {
        return againRate;
    }

    public void setAgainRate(Integer againRate) {
        this.againRate = againRate;
    }

    public String getAverageStayTime() {
        return averageStayTime;
    }

    public void setAverageStayTime(String averageStayTime) {
        this.averageStayTime = averageStayTime;
    }

    public Integer getTransformCount() {
        return transformCount;
    }

    public void setTransformCount(Integer transformCount) {
        this.transformCount = transformCount;
    }

    public Integer getDirectTransformCount() {
        return directTransformCount;
    }

    public void setDirectTransformCount(Integer directTransformCount) {
        this.directTransformCount = directTransformCount;
    }

    public Integer getBackTransformCount() {
        return backTransformCount;
    }

    public void setBackTransformCount(Integer backTransformCount) {
        this.backTransformCount = backTransformCount;
    }

    public BigDecimal getTransformCost() {
        return transformCost;
    }

    public void setTransformCost(BigDecimal transformCost) {
        this.transformCost = transformCost;
    }

    public BigDecimal getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(BigDecimal insuranceFee) {
        this.insuranceFee = insuranceFee;
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
