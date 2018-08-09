package com.modelink.reservation.vo;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 承保效果
 */
public class FlowVo implements Serializable {

    private Long id;
    /**
     * 日期
     **/
    private String date;
    /**
     * 合作商户
     **/
    private Long merchantName;
    /**
     * 渠道归属
     **/
    private String platformName;

    /**
     * 浏览量
     **/
    private Integer browseCount;
    /**
     * 访问量
     **/
    private Integer accessCount;
    /**
     * 用户数
     **/
    private Integer userCount;
    /**
     * 点击量
     **/
    private Integer clickCount;
    /**
     * 二跳量
     **/
    private Integer againClickCount;
    /**
     * 二跳率
     **/
    private Integer againClickRate;
    /**
     * 平均停留时间
     **/
    private String averageStayTime;
    /**
     * 平均浏览页面数
     **/
    private Integer averageBrowsePageCount;

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

    public Long getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(Long merchantName) {
        this.merchantName = merchantName;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public Integer getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Integer browseCount) {
        this.browseCount = browseCount;
    }

    public Integer getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getAgainClickCount() {
        return againClickCount;
    }

    public void setAgainClickCount(Integer againClickCount) {
        this.againClickCount = againClickCount;
    }

    public Integer getAgainClickRate() {
        return againClickRate;
    }

    public void setAgainClickRate(Integer againClickRate) {
        this.againClickRate = againClickRate;
    }

    public String getAverageStayTime() {
        return averageStayTime;
    }

    public void setAverageStayTime(String averageStayTime) {
        this.averageStayTime = averageStayTime;
    }

    public Integer getAverageBrowsePageCount() {
        return averageBrowsePageCount;
    }

    public void setAverageBrowsePageCount(Integer averageBrowsePageCount) {
        this.averageBrowsePageCount = averageBrowsePageCount;
    }

}
