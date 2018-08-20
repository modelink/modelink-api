package com.modelink.admin.mapper;

import com.modelink.admin.bean.Admin;
import com.modelink.admin.bean.ExceptionLogger;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface ExceptionLoggerMapper extends Mapper<ExceptionLogger>, MySqlMapper<ExceptionLogger> {

}
