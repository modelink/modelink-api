package com.modelink.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class AdvertiseAnalyseVo implements Serializable{

    private Long id;
    /** 预约商户（如小米、华夏） **/
    private String merchantName;
    /** 平台类型（微信、PC、WAP、转介绍） **/
    private String platformName;
    /** 数据类型（自然流量、SEM） **/
    private String dataTypeName;
    /** 统计时间 **/
    private String statTime;
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
    private String arriveRate;
    /** 二跳量 **/
    private Integer againCount;
    /** 二跳率 **/
    private String againRate;
    /** 平均停留时间 **/
    private String averageStayTime;
    /** 转化量 **/
    private Integer transformCount;
    /** 直接转化量 **/
    private Integer directTransformCount;
    /** 回归转化量 **/
    private Integer backTransformCount;
    /** 转化成本 **/
    private String transformCost;
    /** 保险费用 **/
    private String insuranceFee;
    /** 状态 **/
    private Integer status;
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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
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

    public String getStatTime() {
        return statTime;
    }

    public void setStatTime(String statTime) {
        this.statTime = statTime;
    }

    public String getArriveRate() {
        return arriveRate;
    }

    public void setArriveRate(String arriveRate) {
        this.arriveRate = arriveRate;
    }

    public String getAgainRate() {
        return againRate;
    }

    public void setAgainRate(String againRate) {
        this.againRate = againRate;
    }

    public void setTransformCost(String transformCost) {
        this.transformCost = transformCost;
    }

    public void setInsuranceFee(String insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public Integer getAgainCount() {
        return againCount;
    }

    public void setAgainCount(Integer againCount) {
        this.againCount = againCount;
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

    public String getTransformCost() {
        return transformCost;
    }

    public String getInsuranceFee() {
        return insuranceFee;
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
