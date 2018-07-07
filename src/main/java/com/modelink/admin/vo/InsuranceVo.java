package com.modelink.admin.vo;

import com.modelink.common.annotation.ExportField;

import java.io.Serializable;
import java.math.BigDecimal;

public class InsuranceVo implements Serializable{

    @ExportField(value="ID")
    private Long id;
    /** 保单编号 **/
    @ExportField(value="保单编号")
    private String insuranceNo;
    /** 预约姓名 **/
    @ExportField(value="预约姓名")
    private String name;
    /** 投保人性别 **/
    @ExportField(value="性别")
    private String gender;
    /** 投保人生日 **/
    @ExportField(value="生日")
    private String birthday;
    /** 投保人年龄 **/
    @ExportField(value="年龄")
    private Integer age;
    /** 投保人手机号 **/
    @ExportField(value="投保人手机号")
    private String mobile;
    /** 投保人地址 **/
    @ExportField(value="投保人地址")
    private String address;
    /** 预约时间 **/
    @ExportField(value="预约时间")
    private String contactTime;
    /** 预约商户（如小米、华夏） **/
    @ExportField(value="项目")
    private String merchantName;
    /** 平台类型（微信、PC、WAP、转介绍） **/
    @ExportField(value="渠道归属")
    private String platformName;
    /** 数据类型（自然流量、SEM） **/
    @ExportField(value="渠道明细")
    private String dataTypeName;
    /** 渠道入口类型（0-默认） **/
    @ExportField(value="入口页面")
    private String sourceTypeName;
    /** 下发时间 **/
    @ExportField(value="下发时间")
    private String arrangeTime;
    /** 机构名称 **/
    @ExportField(value="机构名称")
    private String orgName;
    /** 客服名称 **/
    @ExportField(value="客服名称")
    private String tsrName;
    /** 第一天拨打 **/
    @ExportField(value="第一天拨打")
    private String firstCall;
    /** 第二天拨打 **/
    @ExportField(value="第二天拨打")
    private String secondCall;
    /** 第三天拨打 **/
    @ExportField(value="第三天拨打")
    private String threeCall;
    /** 拨打状态 **/
    @ExportField(value="拨打状态")
    private String callStatus;
    /** 是否问题数据 **/
    @ExportField(value="问题数据")
    private String problem;
    /** 成单日期 **/
    @ExportField(value="成单日期")
    private String finishTime;
    /** 缴费方式 **/
    @ExportField(value="缴费方式")
    private String payTypeName;
    /** 保险额度 **/
    @ExportField(value="保险额度")
    private BigDecimal insuranceAmount;
    /** 保险费用 **/
    @ExportField(value="保险费用")
    private BigDecimal insuranceFee;
    /** 投保数量 **/
    @ExportField(value="件数")
    private Integer insuranceCount;
    /** 备注信息 **/
    @ExportField(value="备注信息")
    private String remark;
    /** 创建时间 **/
    @ExportField(value="创建时间")
    private String createTime;
    /** 更新时间 **/
    @ExportField(value="更新时间")
    private String updateTime;

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

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public void setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getContactTime() {
        return contactTime;
    }

    public void setContactTime(String contactTime) {
        this.contactTime = contactTime;
    }

    public String getArrangeTime() {
        return arrangeTime;
    }

    public void setArrangeTime(String arrangeTime) {
        this.arrangeTime = arrangeTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
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
