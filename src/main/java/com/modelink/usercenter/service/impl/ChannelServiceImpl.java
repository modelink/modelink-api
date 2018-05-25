package com.modelink.usercenter.service.impl;

import com.modelink.usercenter.bean.Channel;
import com.modelink.usercenter.mapper.ChannelMapper;
import com.modelink.usercenter.service.ChannelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private ChannelMapper channelMapper;

    /**
     * 根据 appKey 查询渠道信息
     *
     * @param appKey
     * @return
     */
    @Override
    public Channel findByAppKey(String appKey) {
        return channelMapper.selectByPrimaryKey(appKey);
    }

    /**
     * 获取渠道列表
     *
     * @param channel
     * @return
     */
    @Override
    public List<Channel> findList(Channel channel) {
        return channelMapper.select(channel);
    }
}
