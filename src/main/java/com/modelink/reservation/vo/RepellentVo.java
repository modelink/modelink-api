package com.modelink.reservation.vo;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class RepellentVo implements Serializable {

    @Id
    private Long id;
    /** 合作商户 **/
    private String merchantName;
    /** 数据导出机构名称 **/
    private String exportOrgName;
    /** 投保单号 **/
    private String repellentNo;
    /** 保单号 **/
    private String insuranceNo;
    /** 保单状态 **/
    private String status;
    /** 保单子状态 **/
    private String childStatus;
    /** 被保人名称 **/
    private String insuranceName;
    /** 产品名称 **/
    private String productName;
    /** 附加险种 **/
    private String extraInsurance;
    /** 保额 **/
    private String insuranceAmount;
    /** 年化保费 **/
    private String yearInsuranceFee;
    /** 标准保费 **/
    private String insuranceFee;
    /** tsr工号 **/
    private String tsrNumber;
    /** tsr姓名 **/
    private String tsrName;
    /** tl工号 **/
    private String tlNumber;
    /** tl姓名 **/
    private String tlName;
    /** 管理机构 **/
    private String orgName;
    /** 部 **/
    private String department;
    /** 区 **/
    private String regionName;
    /** 组 **/
    private String groupName;
    /** 专案名称 **/
    private String specialCaseName;
    /** 承保日期 **/
    private String insuranceDate;
    /** 交费方式 **/
    private String payTypeName;
    /** 犹豫日期 **/
    private String hesitateDate;
    /** 交费期间 **/
    private Integer payInterval;

    private String createTime;
    private String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRepellentNo() {
        return repellentNo;
    }

    public void setRepellentNo(String repellentNo) {
        this.repellentNo = repellentNo;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getExtraInsurance() {
        return extraInsurance;
    }

    public void setExtraInsurance(String extraInsurance) {
        this.extraInsurance = extraInsurance;
    }

    public String getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(String insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getYearInsuranceFee() {
        return yearInsuranceFee;
    }

    public void setYearInsuranceFee(String yearInsuranceFee) {
        this.yearInsuranceFee = yearInsuranceFee;
    }

    public String getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(String insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public String getTsrNumber() {
        return tsrNumber;
    }

    public void setTsrNumber(String tsrNumber) {
        this.tsrNumber = tsrNumber;
    }

    public String getTsrName() {
        return tsrName;
    }

    public void setTsrName(String tsrName) {
        this.tsrName = tsrName;
    }

    public String getTlNumber() {
        return tlNumber;
    }

    public void setTlNumber(String tlNumber) {
        this.tlNumber = tlNumber;
    }

    public String getTlName() {
        return tlName;
    }

    public void setTlName(String tlName) {
        this.tlName = tlName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSpecialCaseName() {
        return specialCaseName;
    }

    public void setSpecialCaseName(String specialCaseName) {
        this.specialCaseName = specialCaseName;
    }

    public String getInsuranceDate() {
        return insuranceDate;
    }

    public void setInsuranceDate(String insuranceDate) {
        this.insuranceDate = insuranceDate;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChildStatus() {
        return childStatus;
    }

    public void setChildStatus(String childStatus) {
        this.childStatus = childStatus;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
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

    public String getExportOrgName() {
        return exportOrgName;
    }

    public void setExportOrgName(String exportOrgName) {
        this.exportOrgName = exportOrgName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getHesitateDate() {
        return hesitateDate;
    }

    public void setHesitateDate(String hesitateDate) {
        this.hesitateDate = hesitateDate;
    }

    public Integer getPayInterval() {
        return payInterval;
    }

    public void setPayInterval(Integer payInterval) {
        this.payInterval = payInterval;
    }
}
