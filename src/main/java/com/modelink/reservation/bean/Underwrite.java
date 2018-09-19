package com.modelink.reservation.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * 承保效果
 */
public class Underwrite {

    @Id
    private Long id;
    /** 合作商户 **/
    @Column(name = "merchant_id")
    private Long merchantId;
    /** 产品名称 **/
    private String productName;
    /** 数据导出机构名称 **/
    private String orgName;
    /** 保单号 **/
    private String insuranceNo;
    /** 预约电话号码 **/
    private String reserveMobile;
    /** 渠道归属 **/
    private String platformName;
    /** 广告活动 **/
    private String advertiseActive;
    private String advertiseSeries;
    /** 关键词 **/
    private String keyword;
    /** 数据来源 **/
    private String source;
    /** 预约日期 **/
    private String reserveDate;
    /** 来源日期 **/
    private String sourceDate;
    /** 成单日期 **/
    private String finishDate;
    /** 缴费方式 **/
    private Integer payType;
    /** 保额 **/
    private String insuranceAmount;
    /** 保费 **/
    private String insuranceFee;
    /** 投保人性别 **/
    private String gender;
    /** 投保人生日 **/
    private String birthday;
    /** 投保人年龄 **/
    private Integer age;
    /** 投保人地址 **/
    private String address;
    /** 省份ID **/
    private Integer provinceId;
    /** 城市ID **/
    private Integer cityId;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
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

    public String getAdvertiseSeries() {
        return advertiseSeries;
    }

    public void setAdvertiseSeries(String advertiseSeries) {
        this.advertiseSeries = advertiseSeries;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(String sourceDate) {
        this.sourceDate = sourceDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(String insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(String insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
