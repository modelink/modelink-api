package com.modelink.admin.vo.huaxiaReport;

import java.io.Serializable;

public class HuaxiaReportDetailItemVo implements Serializable {

    private String date;
    private String dataSource;
    private String platformName;
    private String advertiseActive;
    private Integer directTransformCount;

    private Integer browseCount;
    private Integer clickCount;
    private Integer arriveCount;
    private Integer arriveUserCount;
    private Integer againCount;
    private Integer averageStayTime;
    private String againRate;
    private String arriveRate;

    private Integer mediaShowCount;
    private Integer mediaClickCount;
    private String mediaClickRate;
    private String cpc;
    private String cpm;
    private String consumeAmount;

    private String directTransformCost;


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

    public Integer getDirectTransformCount() {
        return directTransformCount;
    }

    public void setDirectTransformCount(Integer directTransformCount) {
        this.directTransformCount = directTransformCount;
    }

    public Integer getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Integer browseCount) {
        this.browseCount = browseCount;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
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

    public Integer getAgainCount() {
        return againCount;
    }

    public void setAgainCount(Integer againCount) {
        this.againCount = againCount;
    }

    public Integer getAverageStayTime() {
        return averageStayTime;
    }

    public void setAverageStayTime(Integer averageStayTime) {
        this.averageStayTime = averageStayTime;
    }

    public String getAgainRate() {
        return againRate;
    }

    public void setAgainRate(String againRate) {
        this.againRate = againRate;
    }

    public String getArriveRate() {
        return arriveRate;
    }

    public void setArriveRate(String arriveRate) {
        this.arriveRate = arriveRate;
    }

    public Integer getMediaShowCount() {
        return mediaShowCount;
    }

    public void setMediaShowCount(Integer mediaShowCount) {
        this.mediaShowCount = mediaShowCount;
    }

    public Integer getMediaClickCount() {
        return mediaClickCount;
    }

    public void setMediaClickCount(Integer mediaClickCount) {
        this.mediaClickCount = mediaClickCount;
    }

    public String getMediaClickRate() {
        return mediaClickRate;
    }

    public void setMediaClickRate(String mediaClickRate) {
        this.mediaClickRate = mediaClickRate;
    }

    public String getCpc() {
        return cpc;
    }

    public void setCpc(String cpc) {
        this.cpc = cpc;
    }

    public String getCpm() {
        return cpm;
    }

    public void setCpm(String cpm) {
        this.cpm = cpm;
    }

    public String getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(String consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public String getDirectTransformCost() {
        return directTransformCost;
    }

    public void setDirectTransformCost(String directTransformCost) {
        this.directTransformCost = directTransformCost;
    }
}
