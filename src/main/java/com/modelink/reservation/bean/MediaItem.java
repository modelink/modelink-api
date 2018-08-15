package com.modelink.reservation.bean;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 承保效果
 */
public class MediaItem implements Serializable {

    @Id
    private Long id;
    /** 日期 **/
    private String date;
    /** 合作商户 **/
    private Long merchantId;
    /** 渠道归属 **/
    private String platformName;
    /** 广告活动 **/
    private String advertiseActive;
    /** 广告媒体 **/
    private String advertiseMedia;
    /** 广告系列 **/
    private String advertiseSeries;
    /** 关键词组 **/
    private String keyWordGroup;
    /** 关键词 **/
    private String keyWord;
    /** 展现量 **/
    private Integer showCount;
    /** 点击量 **/
    private Integer clickCount;
    /** 消费 **/
    private String speedCost;
    /** 点击率 **/
    private String clickRate;
    /** 平均点击价格 **/
    private String averageClickPrice;
    /** 平均排名 **/
    private String averageRank;
    /** 创建时间 **/
    private Date createTime;
    /** 修改时间 **/
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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAdvertiseActive() {
        return advertiseActive;
    }

    public void setAdvertiseActive(String advertiseActive) {
        this.advertiseActive = advertiseActive;
    }

    public String getAdvertiseMedia() {
        return advertiseMedia;
    }

    public void setAdvertiseMedia(String advertiseMedia) {
        this.advertiseMedia = advertiseMedia;
    }

    public String getAdvertiseSeries() {
        return advertiseSeries;
    }

    public void setAdvertiseSeries(String advertiseSeries) {
        this.advertiseSeries = advertiseSeries;
    }

    public String getKeyWordGroup() {
        return keyWordGroup;
    }

    public void setKeyWordGroup(String keyWordGroup) {
        this.keyWordGroup = keyWordGroup;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Integer getShowCount() {
        return showCount;
    }

    public void setShowCount(Integer showCount) {
        this.showCount = showCount;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public String getSpeedCost() {
        return speedCost;
    }

    public void setSpeedCost(String speedCost) {
        this.speedCost = speedCost;
    }

    public String getClickRate() {
        return clickRate;
    }

    public void setClickRate(String clickRate) {
        this.clickRate = clickRate;
    }

    public String getAverageClickPrice() {
        return averageClickPrice;
    }

    public void setAverageClickPrice(String averageClickPrice) {
        this.averageClickPrice = averageClickPrice;
    }

    public String getAverageRank() {
        return averageRank;
    }

    public void setAverageRank(String averageRank) {
        this.averageRank = averageRank;
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
