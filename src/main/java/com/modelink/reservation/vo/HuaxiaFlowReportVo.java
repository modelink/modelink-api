package com.modelink.reservation.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class HuaxiaFlowReportVo implements Serializable {

    @Id
    private Long id;
    private String date;
    private String dataSource;
    private String platformName;
    private String advertiseActive;
    private Integer browseCount;
    private Integer clickCount;
    private Integer arriveCount;
    private Integer arriveUserCount;
    private Integer againCount;
    private String averageStayTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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

    public String getAverageStayTime() {
        return averageStayTime;
    }

    public void setAverageStayTime(String averageStayTime) {
        this.averageStayTime = averageStayTime;
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
