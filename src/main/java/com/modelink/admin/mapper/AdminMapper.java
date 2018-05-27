package com.modelink.admin.mapper;

import com.modelink.admin.bean.Admin;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface AdminMapper extends Mapper<Admin>, MySqlMapper<Admin> {

}
