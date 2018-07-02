package com.modelink.usercenter.service;

import com.modelink.usercenter.bean.Merchant;

import java.util.List;

/**
 * 第三方合作商接口
 */
public interface MerchantService {

    /**
     * 根据 Id 查询合作商信息
     * @param id
     * @return
     */
    public Merchant findById(Long id);

    /**
     * 根据 name 查询合作商信息
     * @param name
     * @return
     */
    public Merchant findByName(String name);

    /**
     * 根据 appKey 查询合作商信息
     * @param appKey
     * @return
     */
    public Merchant findByAppKey(Long appKey);

    /**
     * 获取合作商列表
     * @param merchant
     * @return
     */
    public List<Merchant> findList(Merchant merchant);
}
