package com.modelink.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class InsuranceVo implements Serializable{

    private Long id;
    /** 保单编号 **/
    private String insuranceNo;
    /** 预约姓名 **/
    private String name;
    /** 投保人性别 **/
    private String gender;
    /** 投保人生日 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;
    /** 投保人年龄 **/
    private Integer age;
    /** 投保人手机号 **/
    private String mobile;
    /** 投保人地址 **/
    private String address;
    /** 预约时间 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date contactTime;
    /** 预约商户（如小米、华夏） **/
    private String merchantName;
    /** 平台类型（微信、PC、WAP、转介绍） **/
    private String platformName;
    /** 数据类型（自然流量、SEM） **/
    private String dataTypeName;
    /** 渠道入口类型（0-默认） **/
    private String sourceName;
    /** 下发时间 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date arrangeTime;
    /** 机构名称 **/
    private String orgName;
    /** 客服名称 **/
    private String tsrName;
    /** 第一天拨打 **/
    private String firstCall;
    /** 第二天拨打 **/
    private String secondCall;
    /** 第三天拨打 **/
    private String threeCall;
    /** 拨打状态 **/
    private String callStatus;
    /** 是否问题数据 **/
    private String problem;
    /** 成单日期 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date finishTime;
    /** 缴费方式 **/
    private String payTypeName;
    /** 保险额度 **/
    private BigDecimal insuranceAmount;
    /** 保险费用 **/
    private BigDecimal insuranceFee;
    /** 投保数量 **/
    private Integer insuranceCount;
    /** 备注信息 **/
    private String remark;
    /** 创建时间 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 更新时间 **/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getContactTime() {
        return contactTime;
    }

    public void setContactTime(Date contactTime) {
        this.contactTime = contactTime;
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

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Date getArrangeTime() {
        return arrangeTime;
    }

    public void setArrangeTime(Date arrangeTime) {
        this.arrangeTime = arrangeTime;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTsrName() {
        return tsrName;
    }

    public void setTsrName(String tsrName) {
        this.tsrName = tsrName;
    }

    public String getFirstCall() {
        return firstCall;
    }

    public void setFirstCall(String firstCall) {
        this.firstCall = firstCall;
    }

    public String getSecondCall() {
        return secondCall;
    }

    public void setSecondCall(String secondCall) {
        this.secondCall = secondCall;
    }

    public String getThreeCall() {
        return threeCall;
    }

    public void setThreeCall(String threeCall) {
        this.threeCall = threeCall;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public BigDecimal getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(BigDecimal insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public Integer getInsuranceCount() {
        return insuranceCount;
    }

    public void setInsuranceCount(Integer insuranceCount) {
        this.insuranceCount = insuranceCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
