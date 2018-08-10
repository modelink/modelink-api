package com.modelink.reservation.bean;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 预约流量数据
 */
public class FlowReserve implements Serializable {

    @Id
    private Long id;
    /** 合作商户 **/
    private Long merchantId;
    /** 日期 **/
    private String date;
    /** 时间 **/
    private String time;
    /** 预约编号 **/
    private String reserveNo;
    /** 预约电话 **/
    private String reserveMobile;
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

    private String advertiseTime;
    private String advertiseTransformTime;

    private String firstAdvertiseActive;
    private String firstAdvertiseMedia;
    private String firstAdvertiseDesc;
    private String firstAdvertiseTime;

    private String stationAdvertise;
    private String stationAdvertiseTime;
    private String stationAdvertiseTransformTime;
    private String transformType;

    private String ip;
    private Integer provinceId;
    private Integer cityId;
    private String source;
    private String isAdvertise;
    private String website;
    private String searchWord;
    private String isNewVisitor;

    private String last2AdvertiseActive;
    private String last2AdvertiseMedia;
    private String last2AdvertiseDesc;
    private String last2AdvertiseTime;

    private String last3AdvertiseActive;
    private String last3AdvertiseMedia;
    private String last3AdvertiseDesc;
    private String last3AdvertiseTime;

    private String os;
    private String browser;
    private String resolutionRatio;
    private String deviceType;

    private String themePage;
    private String last2ThemePage;
    private String last2ThemePageNo;
    private String last2ThemeClickTime;
    private String last2ThemeTransformTime;
    private String isMakeUp;

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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReserveNo() {
        return reserveNo;
    }

    public void setReserveNo(String reserveNo) {
        this.reserveNo = reserveNo;
    }

    public String getReserveMobile() {
        return reserveMobile;
    }

    public void setReserveMobile(String reserveMobile) {
        this.reserveMobile = reserveMobile;
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

    public String getAdvertiseTime() {
        return advertiseTime;
    }

    public void setAdvertiseTime(String advertiseTime) {
        this.advertiseTime = advertiseTime;
    }

    public String getAdvertiseTransformTime() {
        return advertiseTransformTime;
    }

    public void setAdvertiseTransformTime(String advertiseTransformTime) {
        this.advertiseTransformTime = advertiseTransformTime;
    }

    public String getFirstAdvertiseActive() {
        return firstAdvertiseActive;
    }

    public void setFirstAdvertiseActive(String firstAdvertiseActive) {
        this.firstAdvertiseActive = firstAdvertiseActive;
    }

    public String getFirstAdvertiseMedia() {
        return firstAdvertiseMedia;
    }

    public void setFirstAdvertiseMedia(String firstAdvertiseMedia) {
        this.firstAdvertiseMedia = firstAdvertiseMedia;
    }

    public String getFirstAdvertiseDesc() {
        return firstAdvertiseDesc;
    }

    public void setFirstAdvertiseDesc(String firstAdvertiseDesc) {
        this.firstAdvertiseDesc = firstAdvertiseDesc;
    }

    public String getFirstAdvertiseTime() {
        return firstAdvertiseTime;
    }

    public void setFirstAdvertiseTime(String firstAdvertiseTime) {
        this.firstAdvertiseTime = firstAdvertiseTime;
    }

    public String getStationAdvertise() {
        return stationAdvertise;
    }

    public void setStationAdvertise(String stationAdvertise) {
        this.stationAdvertise = stationAdvertise;
    }

    public String getStationAdvertiseTime() {
        return stationAdvertiseTime;
    }

    public void setStationAdvertiseTime(String stationAdvertiseTime) {
        this.stationAdvertiseTime = stationAdvertiseTime;
    }

    public String getStationAdvertiseTransformTime() {
        return stationAdvertiseTransformTime;
    }

    public void setStationAdvertiseTransformTime(String stationAdvertiseTransformTime) {
        this.stationAdvertiseTransformTime = stationAdvertiseTransformTime;
    }

    public String getTransformType() {
        return transformType;
    }

    public void setTransformType(String transformType) {
        this.transformType = transformType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getIsAdvertise() {
        return isAdvertise;
    }

    public void setIsAdvertise(String isAdvertise) {
        this.isAdvertise = isAdvertise;
    }

    public String getIsNewVisitor() {
        return isNewVisitor;
    }

    public void setIsNewVisitor(String isNewVisitor) {
        this.isNewVisitor = isNewVisitor;
    }

    public String getLast2AdvertiseActive() {
        return last2AdvertiseActive;
    }

    public void setLast2AdvertiseActive(String last2AdvertiseActive) {
        this.last2AdvertiseActive = last2AdvertiseActive;
    }

    public String getLast2AdvertiseMedia() {
        return last2AdvertiseMedia;
    }

    public void setLast2AdvertiseMedia(String last2AdvertiseMedia) {
        this.last2AdvertiseMedia = last2AdvertiseMedia;
    }

    public String getLast2AdvertiseDesc() {
        return last2AdvertiseDesc;
    }

    public void setLast2AdvertiseDesc(String last2AdvertiseDesc) {
        this.last2AdvertiseDesc = last2AdvertiseDesc;
    }

    public String getLast2AdvertiseTime() {
        return last2AdvertiseTime;
    }

    public void setLast2AdvertiseTime(String last2AdvertiseTime) {
        this.last2AdvertiseTime = last2AdvertiseTime;
    }

    public String getLast3AdvertiseActive() {
        return last3AdvertiseActive;
    }

    public void setLast3AdvertiseActive(String last3AdvertiseActive) {
        this.last3AdvertiseActive = last3AdvertiseActive;
    }

    public String getLast3AdvertiseMedia() {
        return last3AdvertiseMedia;
    }

    public void setLast3AdvertiseMedia(String last3AdvertiseMedia) {
        this.last3AdvertiseMedia = last3AdvertiseMedia;
    }

    public String getLast3AdvertiseDesc() {
        return last3AdvertiseDesc;
    }

    public void setLast3AdvertiseDesc(String last3AdvertiseDesc) {
        this.last3AdvertiseDesc = last3AdvertiseDesc;
    }

    public String getLast3AdvertiseTime() {
        return last3AdvertiseTime;
    }

    public void setLast3AdvertiseTime(String last3AdvertiseTime) {
        this.last3AdvertiseTime = last3AdvertiseTime;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getResolutionRatio() {
        return resolutionRatio;
    }

    public void setResolutionRatio(String resolutionRatio) {
        this.resolutionRatio = resolutionRatio;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getThemePage() {
        return themePage;
    }

    public void setThemePage(String themePage) {
        this.themePage = themePage;
    }

    public String getLast2ThemePage() {
        return last2ThemePage;
    }

    public void setLast2ThemePage(String last2ThemePage) {
        this.last2ThemePage = last2ThemePage;
    }

    public String getLast2ThemePageNo() {
        return last2ThemePageNo;
    }

    public void setLast2ThemePageNo(String last2ThemePageNo) {
        this.last2ThemePageNo = last2ThemePageNo;
    }

    public String getLast2ThemeClickTime() {
        return last2ThemeClickTime;
    }

    public void setLast2ThemeClickTime(String last2ThemeClickTime) {
        this.last2ThemeClickTime = last2ThemeClickTime;
    }

    public String getLast2ThemeTransformTime() {
        return last2ThemeTransformTime;
    }

    public void setLast2ThemeTransformTime(String last2ThemeTransformTime) {
        this.last2ThemeTransformTime = last2ThemeTransformTime;
    }

    public String getIsMakeUp() {
        return isMakeUp;
    }

    public void setIsMakeUp(String isMakeUp) {
        this.isMakeUp = isMakeUp;
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
