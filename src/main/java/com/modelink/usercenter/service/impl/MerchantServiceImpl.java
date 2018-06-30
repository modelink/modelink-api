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
     * 根据 appKey 查询渠道信息
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
     * 获取渠道列表
     *
     * @param merchant
     * @return
     */
    @Override
    public List<Merchant> findList(Merchant merchant) {
        return merchantMapper.select(merchant);
    }
}
