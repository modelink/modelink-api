package com.modelink.reservation.vo;

import com.modelink.common.annotation.ExportField;

import java.io.Serializable;

public class UnderwriteVo implements Serializable {

    @ExportField(value="ID")
    private Long id;
    @ExportField(value="合作商户")
    private String merchantName;
    @ExportField(value="产品名称")
    private String productName;
    @ExportField(value="机构名称")
    private String orgName;
    @ExportField(value="保单号")
    private String insuranceNo;
    @ExportField(value="预约电话")
    private String reserveMobile;
    @ExportField(value="渠道明细")
    private String platformName;
    @ExportField(value="广告活动")
    private String advertiseActive;
    @ExportField(value="数据来源")
    private String source;
    @ExportField(value="来源日期")
    private String reserveDate;
    @ExportField(value="成单日期")
    private String finishDate;
    @ExportField(value="缴费方式")
    private String payType;
    @ExportField(value="保额")
    private String insuranceAmount;
    @ExportField(value="保费")
    private String insuranceFee;
    @ExportField(value="投保人性别")
    private String gender;
    @ExportField(value="投保人生日")
    private String birthday;
    @ExportField(value="投保人年龄")
    private Integer age;
    @ExportField(value="投保人地址")
    private String address;
    @ExportField(value="省份")
    private String provinceName;
    @ExportField(value="城市")
    private String cityName;
    @ExportField(value="创建时间")
    private String createTime;
    @ExportField(value="更新时间")
    private String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
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

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public String getAdvertiseActive() {
        return advertiseActive;
    }

    public void setAdvertiseActive(String advertiseActive) {
        this.advertiseActive = advertiseActive;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
