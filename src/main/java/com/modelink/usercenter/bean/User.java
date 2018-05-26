package com.modelink.usercenter.bean;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    /** 用户ID **/
    @Id
    private Long id;
    /** 手机号 **/
    private String mobile;
    /** 用户名 **/
    private String userName;
    /** 真实姓名 **/
    private String realName;
    /** 身份证号 **/
    private String idCard;
    /** 昵称 **/
    private String nickName;
    /** 地址 **/
    private String address;
    /** 状态 **/
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
