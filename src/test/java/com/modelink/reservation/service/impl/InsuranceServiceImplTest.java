package com.modelink.reservation.service.impl;

import com.modelink.reservation.bean.Insurance;
import com.modelink.reservation.service.InsuranceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InsuranceServiceImplTest {

    @Resource
    private InsuranceService insuranceService;

    @Test
    public void insert() throws Exception {
        Insurance insurance = new Insurance();
        insuranceService.insert(insurance);
    }

    @Test
    public void countByParam() throws Exception {

    }

    @Test
    public void findListByParam() throws Exception {

    }

    @Test
    public void findPagerByParam() throws Exception {

    }

}