package com.modelink.usercenter.bean;

import javax.persistence.Id;
import java.util.Date;

public class Channel {

    @Id
    private int appKey;
    private String appSecret;
    private String name;
    private int status;
    private Date createTime;
    private Date updateTime;

    public int getAppKey() {
        return appKey;
    }

    public void setAppKey(int appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
