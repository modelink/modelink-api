package com.modelink.usercenter.mapper;

import com.modelink.usercenter.bean.Area;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface AreaMapper extends Mapper<Area>, MySqlMapper<Area> {

}
