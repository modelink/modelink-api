package com.modelink.reservation.vo;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 承保效果
 */
public class MediaItemVo implements Serializable {

    @Id
    private Long id;
    /** 日期 **/
    private String date;
    /** 合作商户 **/
    private String merchantName;
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
    /** 广告描述 **/
    private String advertiseDesc;
    /** 关键词 **/
    private String keyWord;
    /** 推广单元 **/
    private String popularizeCell;
    /** 推广计划 **/
    private String popularizePlan;
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
    /** 费用归属 **/
    private String feeType;
    /** 创建时间 **/
    private String createTime;
    /** 修改时间 **/
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

    public String getAdvertiseDesc() {
        return advertiseDesc;
    }

    public void setAdvertiseDesc(String advertiseDesc) {
        this.advertiseDesc = advertiseDesc;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getPopularizeCell() {
        return popularizeCell;
    }

    public void setPopularizeCell(String popularizeCell) {
        this.popularizeCell = popularizeCell;
    }

    public String getPopularizePlan() {
        return popularizePlan;
    }

    public void setPopularizePlan(String popularizePlan) {
        this.popularizePlan = popularizePlan;
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

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
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
