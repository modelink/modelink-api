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
    private String merchantName;
    /**
     * 渠道归属
     **/
    private String platformName;
    /**
     * 网站来源
     */
    private String website;
    /**
     * 来源类型
     **/
    private String source;
    /**
     * 浏览量
     **/
    private Integer browseCount;
    /**
     * 访问量
     **/
    private Integer inflowCount;
    /**
     * 用户数
     **/
    private Integer userCount;
    /**
     * 二跳量
     **/
    private Integer againClickCount;
    /**
     * 二跳率
     **/
    private String againClickRate;
    /**
     * 平均停留时间
     **/
    private String averageStayTime;
    /**
     * 平均浏览页面数
     **/
    private String averageBrowsePageCount;

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

    public Integer getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Integer browseCount) {
        this.browseCount = browseCount;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getInflowCount() {
        return inflowCount;
    }

    public void setInflowCount(Integer inflowCount) {
        this.inflowCount = inflowCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getAgainClickCount() {
        return againClickCount;
    }

    public void setAgainClickCount(Integer againClickCount) {
        this.againClickCount = againClickCount;
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

    public String getAverageBrowsePageCount() {
        return averageBrowsePageCount;
    }

    public void setAverageBrowsePageCount(String averageBrowsePageCount) {
        this.averageBrowsePageCount = averageBrowsePageCount;
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
