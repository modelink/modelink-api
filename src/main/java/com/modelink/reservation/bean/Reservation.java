package com.modelink.reservation.bean;

import java.util.Date;

public class Reservation {

    private Long id;
    /** 预约姓名 **/
    private String contactName;
    /** 预约手机号 **/
    private String contactMobile;
    /** 预约时间 **/
    private String contactTime;
    /** 预约渠道（如小米、华夏） **/
    private int channel;
    /** 渠道入口类型 **/
    private int sourceType;
    /** 数据状态（0-正常; 1-无效; 2-其他） **/
    private int status;
    /** 创建时间 **/
    private Date createTime;
    /** 更新时间 **/
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactTime() {
        return contactTime;
    }

    public void setContactTime(String contactTime) {
        this.contactTime = contactTime;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
