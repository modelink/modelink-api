package com.modelink.admin.vo.huaxiaReport;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class HuaxiaReportSummaryVo implements Serializable {

    private String date;
    private String dataSource;
    /** 取自“华夏日报-基础数量表”—字段“有效+营销标记电话”加和 **/
    private Integer validArrange;
    private Integer validCount;
    private Integer flagCount;
    /** 取自“华夏日报-基础数量表”—字段“总转化-pc（去重后）+总转化-移动（去重后）”加和 */
    private Integer totalCount;
    /** 注意：
     * “预约”时取自“华夏日报-基础数量表”—字段“总转化-pc（去重后）+总转化-移动（去重后）-微信端（去重后）”；
     * “测保”时取自“华夏日报-基础数量表”—字段“总转化-pc（去重后）+总转化-移动（去重后）-小米“
     * **/
    private Integer miniTotalCount;
    private Integer pcCount;
    private Integer wapCount;
    /** 微信端（去重后） **/
    private Integer weixinCount;
    private Integer directTransformCount;

    private Integer browseCount;
    private Integer clickCount;
    private Integer arriveCount;
    private Integer arriveUserCount;
    private Integer againCount;
    private String averageStayTime;
    private String againRate;
    private String arriveRate;

    private Integer mediaShowCount;
    private Integer mediaClickCount;
    private String mediaClickRate;
    private String cpc;
    private String cpm;
    private String consumeAmount;

    private String directTransformCost;
    private String totalTransformCost;
    private String insuranceAmount;


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

    public Integer getValidArrange() {
        return validArrange;
    }

    public void setValidArrange(Integer validArrange) {
        this.validArrange = validArrange;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getMiniTotalCount() {
        return miniTotalCount;
    }

    public void setMiniTotalCount(Integer miniTotalCount) {
        this.miniTotalCount = miniTotalCount;
    }

    public Integer getPcCount() {
        return pcCount;
    }

    public void setPcCount(Integer pcCount) {
        this.pcCount = pcCount;
    }

    public Integer getWapCount() {
        return wapCount;
    }

    public void setWapCount(Integer wapCount) {
        this.wapCount = wapCount;
    }

    public Integer getWeixinCount() {
        return weixinCount;
    }

    public void setWeixinCount(Integer weixinCount) {
        this.weixinCount = weixinCount;
    }

    public Integer getDirectTransformCount() {
        return directTransformCount;
    }

    public void setDirectTransformCount(Integer directTransformCount) {
        this.directTransformCount = directTransformCount;
    }

    public String getArriveRate() {
        return arriveRate;
    }

    public void setArriveRate(String arriveRate) {
        this.arriveRate = arriveRate;
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

    public String getTotalTransformCost() {
        return totalTransformCost;
    }

    public void setTotalTransformCost(String totalTransformCost) {
        this.totalTransformCost = totalTransformCost;
    }

    public String getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(String insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public Integer getValidCount() {
        return validCount;
    }

    public void setValidCount(Integer validCount) {
        this.validCount = validCount;
    }

    public Integer getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(Integer flagCount) {
        this.flagCount = flagCount;
    }
}
