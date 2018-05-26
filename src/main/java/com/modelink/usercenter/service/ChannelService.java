package com.modelink.usercenter.service;

import com.modelink.usercenter.bean.Channel;

import java.util.List;

/**
 * 第三方渠道接口
 */
public interface ChannelService {

    /**
     * 根据 appKey 查询渠道信息
     * @param appKey
     * @return
     */
    public Channel findByAppKey(Long appKey);

    /**
     * 获取渠道列表
     * @param channel
     * @return
     */
    public List<Channel> findList(Channel channel);
}
