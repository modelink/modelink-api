package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.FlowReserve;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface FlowReserveMapper extends Mapper<FlowReserve>, MySqlMapper<FlowReserve> {

}
