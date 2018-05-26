package com.modelink.usercenter.service.impl;

import com.modelink.usercenter.bean.Channel;
import com.modelink.usercenter.service.ChannelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ChannelServiceImplTest {

    @Resource
    private ChannelService channelService;

    @Test
    public void findByAppKey() throws Exception {
        System.out.println(channelService.findByAppKey(10001));
    }

    @Test
    public void findList() throws Exception {
        Channel channel = new Channel();
        channel.setAppKey(10001);
        System.out.println(channelService.findList(channel).size());
    }

}