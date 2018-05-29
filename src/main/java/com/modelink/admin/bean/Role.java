package com.modelink.admin.bean;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

public class Role {

    private Long id;
    private String name;
    @Transient
    private List<Long> resourceIdList;

    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getResourceIdList() {
        return resourceIdList;
    }

    public void setResourceIdList(List<Long> resourceIdList) {
        this.resourceIdList = resourceIdList;
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