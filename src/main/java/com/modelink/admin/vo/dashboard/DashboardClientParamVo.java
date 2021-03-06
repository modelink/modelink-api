package com.modelink.admin.vo.dashboard;

import java.io.Serializable;

public class DashboardClientParamVo implements Serializable {

    /** 合作商户ID **/
    private Long merchantId;
    /** 选择字段 **/
    private String chooseItems;
    /** 渠道归属 **/
    private String platformName;
    /** 广告活动 **/
    private String advertiseActive;
    /** 日期值 **/
    private String chooseDate;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getChooseItems() {
        return chooseItems;
    }

    public void setChooseItems(String chooseItems) {
        this.chooseItems = chooseItems;
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

    public String getChooseDate() {
        return chooseDate;
    }

    public void setChooseDate(String chooseDate) {
        this.chooseDate = chooseDate;
    }

}
