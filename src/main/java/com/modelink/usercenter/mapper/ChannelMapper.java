package com.modelink.usercenter.mapper;

import com.modelink.usercenter.bean.Channel;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface ChannelMapper extends Mapper<Channel>, MySqlMapper<Channel> {

}
