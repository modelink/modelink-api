package com.modelink.usercenter.service.impl;

import com.modelink.usercenter.bean.Merchant;
import com.modelink.usercenter.mapper.MerchantMapper;
import com.modelink.usercenter.service.MerchantService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Resource
    private MerchantMapper merchantMapper;

    /**
     * 根据 appKey 查询合作商信息
     *
     * @param id
     * @return
     */
    @Override
    public Merchant findById(Long id) {
        return merchantMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据 name 查询合作商信息
     *
     * @param name
     * @return
     */
    @Override
    public Merchant findByName(String name) {
        Merchant merchant = new Merchant();
        merchant.setName(name);
        return merchantMapper.selectOne(merchant);
    }

    /**
     * 根据 appKey 查询合作商信息
     *
     * @param appKey
     * @return
     */
    @Override
    public Merchant findByAppKey(Long appKey) {
        Merchant merchant = new Merchant();
        merchant.setAppKey(appKey);
        return merchantMapper.selectOne(merchant);
    }

    /**
     * 获取合作商列表
     *
     * @param merchant
     * @return
     */
    @Override
    public List<Merchant> findList(Merchant merchant) {
        return merchantMapper.select(merchant);
    }
}
