package com.modelink.reservation.bean;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 保费数据
 */
public class Permiums implements Serializable {

    @Id
    private Long id;
    /** 反馈日期 **/
    private String date;
    /** 机构名称 **/
    private Long merchantId;
    /** 有效数据（下发） **/
    private Integer validCount;
    /** 总转化量 **/
    private Integer transformCount;
    /** 总转化量（不包含微信） **/
    private Integer transformCountNowx;
    /** 总花费（元） **/
    private String consumeAmount;
    /** 直接转化成本 **/
    private String directTransformCost;
    /** 总转化成本（不含微信） **/
    private String totalTransformCost;
    /** 保件 **/
    private Integer insuranceCount;
    /** 保费 **/
    private String insuranceFee;


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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getValidCount() {
        return validCount;
    }

    public void setValidCount(Integer validCount) {
        this.validCount = validCount;
    }

    public Integer getTransformCount() {
        return transformCount;
    }

    public void setTransformCount(Integer transformCount) {
        this.transformCount = transformCount;
    }

    public Integer getTransformCountNowx() {
        return transformCountNowx;
    }

    public void setTransformCountNowx(Integer transformCountNowx) {
        this.transformCountNowx = transformCountNowx;
    }

    public String getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(String consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public String getDirectTransformCost() {
        return directTransformCost;
    }

    public void setDirectTransformCost(String directTransformCost) {
        this.directTransformCost = directTransformCost;
    }

    public String getTotalTransformCost() {
        return totalTransformCost;
    }

    public void setTotalTransformCost(String totalTransformCost) {
        this.totalTransformCost = totalTransformCost;
    }

    public Integer getInsuranceCount() {
        return insuranceCount;
    }

    public void setInsuranceCount(Integer insuranceCount) {
        this.insuranceCount = insuranceCount;
    }

    public String getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(String insuranceFee) {
        this.insuranceFee = insuranceFee;
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
