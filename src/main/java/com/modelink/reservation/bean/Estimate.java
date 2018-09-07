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
    private Integer transformCount;
    /** 浏览量（网站） **/
    private Integer webBrowseCount;
    /** 点击量（网站） **/
    private Integer webClickCount;
    /** 到达量 **/
    private Integer arriveCount;
    /** 到达用户 **/
    private Integer arriveUserCount;
    /** 到达率 **/
    private String arriveRate;
    /** 二跳量 **/
    private Integer againCount;
    /** 二跳率 **/
    private String againRate;
    /** 平均停留时间 **/
    private String averageStayTime;
    /** 展示数（媒体） **/
    private Integer mediaShowCount;
    /** 点击数（媒体） **/
    private Integer mediaClickCount;
    /** 点击率（媒体） **/
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

    public Integer getTransformCount() {
        return transformCount;
    }

    public void setTransformCount(Integer transformCount) {
        this.transformCount = transformCount;
    }

    public Integer getWebBrowseCount() {
        return webBrowseCount;
    }

    public void setWebBrowseCount(Integer webBrowseCount) {
        this.webBrowseCount = webBrowseCount;
    }

    public Integer getWebClickCount() {
        return webClickCount;
    }

    public void setWebClickCount(Integer webClickCount) {
        this.webClickCount = webClickCount;
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

    public String getArriveRate() {
        return arriveRate;
    }

    public void setArriveRate(String arriveRate) {
        this.arriveRate = arriveRate;
    }

    public Integer getAgainCount() {
        return againCount;
    }

    public void setAgainCount(Integer againCount) {
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
