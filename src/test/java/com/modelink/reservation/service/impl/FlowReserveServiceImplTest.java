package com.modelink.reservation.service.impl;

import com.modelink.reservation.service.FlowReserveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FlowReserveServiceImplTest {

    @Resource
    private FlowReserveService flowReserveService;

    @Test
    public void findAdvertiseActiveList() {
        List<String> advertiseActiveList = flowReserveService.findAdvertiseActiveList();
        for (String advertiseActive : advertiseActiveList) {
            System.out.println(advertiseActive);
        }
    }
}