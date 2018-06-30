package com.modelink.usercenter.service.impl;

import com.modelink.usercenter.bean.Merchant;
import com.modelink.usercenter.service.MerchantService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MerchantServiceImplTest {

    @Resource
    private MerchantService merchantService;

    @Test
    public void findByAppKey() throws Exception {
        System.out.println(merchantService.findByAppKey(10001L));
    }

    @Test
    public void findList() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setAppKey(10001L);
        System.out.println(merchantService.findList(merchant).size());
    }

}