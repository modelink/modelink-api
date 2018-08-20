package com.modelink.reservation.bean;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 媒体策略数据表
 */
public class MediaTactics implements Serializable {

    @Id
    private Long id;
    /** 日期 **/
    private String month;
    /** 合作商户 **/
    private Long merchantId;
    /** 渠道归属 **/
    private String platformName;
    /** 广告活动 **/
    private String advertiseActive;
    /** 消费 **/
    private String speedCost;
    /** 预约数量 **/
    private Integer reserveCount;
    /** 承保件数 **/
    private Integer insuranceCount;
    /** 保费 **/
    private String insuranceFee;
    /** 总操作次数 **/
    private Integer operateCount;
    /** 关键词优化 **/
    private Integer optimizeKeyWord;
    /** 增加出价（次） **/
    private Integer addBid;
    /** 降低出价（次） **/
    private Integer reduceBid;
    /** 调宽匹配模式（次） **/
    private Integer addPatten;
    /** 调窄匹配模式（次） **/
    private Integer reducePatten;
    /** 增加关键词（次） **/
    private Integer addKeyWord;
    /** 删除关键词（次） **/
    private Integer reduceKeyWord;
    /** 搜索词过滤（次） **/
    private Integer filteKeyWord;
    /** 文字创意优化（次） **/
    private Integer optimizeWordIdea;
    /** 增加图片等高级样式（次） **/
    private Integer addStyle;
    /** 增加文字创意（次） **/
    private Integer addWordIdea;
    /** 展示类图片创意优化（次） **/
    private Integer optimizeImageIdea;
    /** 增加图片创意（次） **/
    private Integer addImageIdea;
    /** 删除图片创意（次） **/
    private Integer reduceImageIdea;
    /** 调整图片出价（次） **/
    private Integer modifyImageBid;
    /** 信息流文字创意优化（次） **/
    private Integer optimizeFlowIdea;
    /** 文案调整（次） **/
    private Integer modifyCopywrite;
    /** 信息流人群优化（次） **/
    private Integer optimizeFlowPeople;
    /** 修改定向关键词（次） **/
    private Integer modifyKeyWord;


    /** 创建时间 **/
    private Date createTime;
    /** 修改时间 **/
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAdvertiseActive() {
        return advertiseActive;
    }

    public void setAdvertiseActive(String advertiseActive) {
        this.advertiseActive = advertiseActive;
    }

    public String getSpeedCost() {
        return speedCost;
    }

    public void setSpeedCost(String speedCost) {
        this.speedCost = speedCost;
    }

    public Integer getReserveCount() {
        return reserveCount;
    }

    public void setReserveCount(Integer reserveCount) {
        this.reserveCount = reserveCount;
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

    public Integer getOperateCount() {
        return operateCount;
    }

    public void setOperateCount(Integer operateCount) {
        this.operateCount = operateCount;
    }

    public Integer getOptimizeKeyWord() {
        return optimizeKeyWord;
    }

    public void setOptimizeKeyWord(Integer optimizeKeyWord) {
        this.optimizeKeyWord = optimizeKeyWord;
    }

    public Integer getAddBid() {
        return addBid;
    }

    public void setAddBid(Integer addBid) {
        this.addBid = addBid;
    }

    public Integer getReduceBid() {
        return reduceBid;
    }

    public void setReduceBid(Integer reduceBid) {
        this.reduceBid = reduceBid;
    }

    public Integer getAddPatten() {
        return addPatten;
    }

    public void setAddPatten(Integer addPatten) {
        this.addPatten = addPatten;
    }

    public Integer getReducePatten() {
        return reducePatten;
    }

    public void setReducePatten(Integer reducePatten) {
        this.reducePatten = reducePatten;
    }

    public Integer getAddKeyWord() {
        return addKeyWord;
    }

    public void setAddKeyWord(Integer addKeyWord) {
        this.addKeyWord = addKeyWord;
    }

    public Integer getReduceKeyWord() {
        return reduceKeyWord;
    }

    public void setReduceKeyWord(Integer reduceKeyWord) {
        this.reduceKeyWord = reduceKeyWord;
    }

    public Integer getFilteKeyWord() {
        return filteKeyWord;
    }

    public void setFilteKeyWord(Integer filteKeyWord) {
        this.filteKeyWord = filteKeyWord;
    }

    public Integer getOptimizeWordIdea() {
        return optimizeWordIdea;
    }

    public void setOptimizeWordIdea(Integer optimizeWordIdea) {
        this.optimizeWordIdea = optimizeWordIdea;
    }

    public Integer getAddStyle() {
        return addStyle;
    }

    public void setAddStyle(Integer addStyle) {
        this.addStyle = addStyle;
    }

    public Integer getAddWordIdea() {
        return addWordIdea;
    }

    public void setAddWordIdea(Integer addWordIdea) {
        this.addWordIdea = addWordIdea;
    }

    public Integer getOptimizeImageIdea() {
        return optimizeImageIdea;
    }

    public void setOptimizeImageIdea(Integer optimizeImageIdea) {
        this.optimizeImageIdea = optimizeImageIdea;
    }

    public Integer getAddImageIdea() {
        return addImageIdea;
    }

    public void setAddImageIdea(Integer addImageIdea) {
        this.addImageIdea = addImageIdea;
    }

    public Integer getReduceImageIdea() {
        return reduceImageIdea;
    }

    public void setReduceImageIdea(Integer reduceImageIdea) {
        this.reduceImageIdea = reduceImageIdea;
    }

    public Integer getModifyImageBid() {
        return modifyImageBid;
    }

    public void setModifyImageBid(Integer modifyImageBid) {
        this.modifyImageBid = modifyImageBid;
    }

    public Integer getOptimizeFlowIdea() {
        return optimizeFlowIdea;
    }

    public void setOptimizeFlowIdea(Integer optimizeFlowIdea) {
        this.optimizeFlowIdea = optimizeFlowIdea;
    }

    public Integer getModifyCopywrite() {
        return modifyCopywrite;
    }

    public void setModifyCopywrite(Integer modifyCopywrite) {
        this.modifyCopywrite = modifyCopywrite;
    }

    public Integer getOptimizeFlowPeople() {
        return optimizeFlowPeople;
    }

    public void setOptimizeFlowPeople(Integer optimizeFlowPeople) {
        this.optimizeFlowPeople = optimizeFlowPeople;
    }

    public Integer getModifyKeyWord() {
        return modifyKeyWord;
    }

    public void setModifyKeyWord(Integer modifyKeyWord) {
        this.modifyKeyWord = modifyKeyWord;
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
