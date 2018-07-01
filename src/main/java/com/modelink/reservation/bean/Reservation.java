package com.modelink.reservation.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable {

    @Id
    private Long id;
    /** 预约姓名 **/
    private String contactName;
    /** 预约手机号 **/
    private String contactMobile;
    /** 预约时间 **/
    private String contactTime;
    /** 预约渠道（如小米、华夏） **/
    private Long channel;
    /** 渠道入口类型（0-默认） **/
    private Integer sourceType = 0;
    /** 数据状态
     *CREATED(0, "已预约"),
     FINISHING(1, "沟通中"),
     FINISHED_SUCC(2, "已沟通_成功"),
     FINISHED_FAIL(3, "已沟通_失败");**/
    private Integer status;
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

    public Long getChannel() {
        return channel;
    }

    public void setChannel(Long channel) {
        this.channel = channel;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
