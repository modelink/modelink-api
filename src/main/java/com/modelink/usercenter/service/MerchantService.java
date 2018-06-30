package com.modelink.usercenter.service;

import com.modelink.usercenter.bean.Merchant;

import java.util.List;

/**
 * 第三方渠道接口
 */
public interface MerchantService {

    /**
     * 根据 appKey 查询渠道信息
     * @param appKey
     * @return
     */
    public Merchant findByAppKey(Long appKey);

    /**
     * 获取渠道列表
     * @param merchant
     * @return
     */
    public List<Merchant> findList(Merchant merchant);
}
