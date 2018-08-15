package com.modelink.reservation.vo;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 异常数据
 */
public class AbnormalVo implements Serializable {

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
    /** 预约日期 **/
    private String reserveDate;
    /** 数据下发日期 **/
    private String arrangeDate;
    /** 首拨日期 **/
    private String callDate;
    /** 首拨结果 **/
    private String callResult;
    /** 最终状态 **/
    private String lastResult;
    /** 是否问题数据 **/
    private String problemData;
    /** 拨打次数 **/
    private Integer callCount;
    /** 内部媒体 **/
    private String sourceMedia;
    /** 设备 **/
    private String deviceName;
    /** 创建日期 **/
    private String createTime;
    /** 更新日期 **/
    private String updateTime;

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

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getLastResult() {
        return lastResult;
    }

    public void setLastResult(String lastResult) {
        this.lastResult = lastResult;
    }

    public String getProblemData() {
        return problemData;
    }

    public void setProblemData(String problemData) {
        this.problemData = problemData;
    }

    public Integer getCallCount() {
        return callCount;
    }

    public void setCallCount(Integer callCount) {
        this.callCount = callCount;
    }

    public String getSourceMedia() {
        return sourceMedia;
    }

    public void setSourceMedia(String sourceMedia) {
        this.sourceMedia = sourceMedia;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
