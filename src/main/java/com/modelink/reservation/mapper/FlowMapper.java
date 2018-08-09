package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Flow;
import com.modelink.reservation.bean.Underwrite;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface FlowMapper extends Mapper<Flow>, MySqlMapper<Flow> {

}
