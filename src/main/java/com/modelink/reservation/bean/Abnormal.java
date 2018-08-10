package com.modelink.reservation.bean;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 异常数据
 */
public class Abnormal implements Serializable {

    @Id
    private Long id;
    /** 反馈日期 **/
    private String date;
    /** 机构名称 **/
    private String orgName;
    /** TSR姓名 **/
    private String tsrName;
    /** 数据来源 **/
    private String source;
    /** 手机号码 **/
    private String mobile;
    /** 数据下发日期 **/
    private String arrangeDate;
    /** 首拨日期 **/
    private String callDate;
    /** 首拨结果 **/
    private String callResult;
    /** 第1天拨打结果 **/
    private String firstCallResult;
    /** 第2天拨打结果 **/
    private String secondCallResult;
    /** 第3天拨打结果 **/
    private String thirdCallResult;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArrangeDate() {
        return arrangeDate;
    }

    public void setArrangeDate(String arrangeDate) {
        this.arrangeDate = arrangeDate;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getCallResult() {
        return callResult;
    }

    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }

    public String getFirstCallResult() {
        return firstCallResult;
    }

    public void setFirstCallResult(String firstCallResult) {
        this.firstCallResult = firstCallResult;
    }

    public String getSecondCallResult() {
        return secondCallResult;
    }

    public void setSecondCallResult(String secondCallResult) {
        this.secondCallResult = secondCallResult;
    }

    public String getThirdCallResult() {
        return thirdCallResult;
    }

    public void setThirdCallResult(String thirdCallResult) {
        this.thirdCallResult = thirdCallResult;
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
