package com.modelink.admin.mapper;

import com.modelink.admin.bean.Role;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface RoleMapper extends Mapper<Role>, MySqlMapper<Role> {

}
