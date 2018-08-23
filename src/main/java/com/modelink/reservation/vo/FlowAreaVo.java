package com.modelink.reservation.vo;

import java.io.Serializable;

/**
 * 承保效果
 */
public class FlowAreaVo implements Serializable {

    private Long id;
    /**
     * 日期
     **/
    private String date;
    /**
     * 合作商户
     **/
    private String merchantName;
    /**
     * 渠道归属
     **/
    private String platformName;
    /**
     * 省份名称
     **/
    private String provinceName;
    /**
     * 城市名称
     **/
    private String cityName;
    /**
     * 来源类型
     **/
    private String source;
    /**
     * 流入量
     **/
    private Integer inflowCount;
    /**
     * 浏览量
     **/
    private Integer browseCount;
    /**
     * 用户数
     **/
    private Integer userCount;
    /**
     * 二跳率
     **/
    private String againClickRate;
    /**
     * 平均停留时间
     **/
    private String averageStayTime;
    /**
     * 创建时间
     **/
    private String createTime;
    /**
     * 修改时间
     **/
    private String updateTime;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Integer browseCount) {
        this.browseCount = browseCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getAgainClickRate() {
        return againClickRate;
    }

    public void setAgainClickRate(String againClickRate) {
        this.againClickRate = againClickRate;
    }

    public String getAverageStayTime() {
        return averageStayTime;
    }

    public void setAverageStayTime(String averageStayTime) {
        this.averageStayTime = averageStayTime;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getInflowCount() {
        return inflowCount;
    }

    public void setInflowCount(Integer inflowCount) {
        this.inflowCount = inflowCount;
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
