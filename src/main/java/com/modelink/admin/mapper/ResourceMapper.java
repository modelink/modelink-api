package com.modelink.admin.mapper;

import com.modelink.admin.bean.Resource;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface ResourceMapper extends Mapper<Resource>, MySqlMapper<Resource> {

}
