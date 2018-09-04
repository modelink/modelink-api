package com.modelink.reservation.bean;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 测保数据
 */
public class Estimate implements Serializable {

    @Id
    private Long id;
    /** 反馈日期 **/
    private String date;
    /** 渠道归属 **/
    private String platformName;
    /** 广告活动 **/
    private String advertiseActive;
    /** 测保转化数 **/
    private int transformCount;
    /** 浏览量（网站） **/
    private int webBrowseCount;
    /** 点击量（网站） **/
    private int webClickCount;
    /** 到达量 **/
    private int arriveCount;
    /** 到达用户 **/
    private int arriveUserCount;
    /** 到达率 **/
    private String arriveRate;
    /** 二跳量 **/
    private int againCount;
    /** 二跳率 **/
    private String againRate;
    /** 平均停留时间 **/
    private String averageStayTime;
    /** 展示数（媒体） **/
    private int mediaShowCount;
    /** 点击数（媒体） **/
    private int mediaClickCount;
    private String mediaClickRate;
    private String cpc;
    private String cpm;
    /** 总花费（元） **/
    private String totalAmount;
    /** 直接转化成本 **/
    private String directTransformCost;

    /** 创建日期 **/
    private Date createTime;
    /** 更新日期 **/
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

    public int getTransformCount() {
        return transformCount;
    }

    public void setTransformCount(int transformCount) {
        this.transformCount = transformCount;
    }

    public int getWebBrowseCount() {
        return webBrowseCount;
    }

    public void setWebBrowseCount(int webBrowseCount) {
        this.webBrowseCount = webBrowseCount;
    }

    public int getWebClickCount() {
        return webClickCount;
    }

    public void setWebClickCount(int webClickCount) {
        this.webClickCount = webClickCount;
    }

    public int getArriveCount() {
        return arriveCount;
    }

    public void setArriveCount(int arriveCount) {
        this.arriveCount = arriveCount;
    }

    public int getArriveUserCount() {
        return arriveUserCount;
    }

    public void setArriveUserCount(int arriveUserCount) {
        this.arriveUserCount = arriveUserCount;
    }

    public String getArriveRate() {
        return arriveRate;
    }

    public void setArriveRate(String arriveRate) {
        this.arriveRate = arriveRate;
    }

    public int getAgainCount() {
        return againCount;
    }

    public void setAgainCount(int againCount) {
        this.againCount = againCount;
    }

    public String getAgainRate() {
        return againRate;
    }

    public void setAgainRate(String againRate) {
        this.againRate = againRate;
    }

    public String getAverageStayTime() {
        return averageStayTime;
    }

    public void setAverageStayTime(String averageStayTime) {
        this.averageStayTime = averageStayTime;
    }

    public int getMediaShowCount() {
        return mediaShowCount;
    }

    public void setMediaShowCount(int mediaShowCount) {
        this.mediaShowCount = mediaShowCount;
    }

    public int getMediaClickCount() {
        return mediaClickCount;
    }

    public void setMediaClickCount(int mediaClickCount) {
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDirectTransformCost() {
        return directTransformCost;
    }

    public void setDirectTransformCost(String directTransformCost) {
        this.directTransformCost = directTransformCost;
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
