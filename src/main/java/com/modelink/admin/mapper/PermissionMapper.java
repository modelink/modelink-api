package com.modelink.admin.mapper;

import com.modelink.admin.bean.Permission;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface PermissionMapper extends Mapper<Permission>, MySqlMapper<Permission> {

}
