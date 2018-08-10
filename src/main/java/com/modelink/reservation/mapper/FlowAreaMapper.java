package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.FlowArea;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface FlowAreaMapper extends Mapper<FlowArea>, MySqlMapper<FlowArea> {

}
