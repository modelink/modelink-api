package com.modelink.usercenter.mapper;

import com.modelink.usercenter.bean.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface UserMapper extends Mapper<User>, MySqlMapper<User> {

}
