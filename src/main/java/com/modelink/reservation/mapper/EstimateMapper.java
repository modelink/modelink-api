package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Estimate;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface EstimateMapper extends Mapper<Estimate>, MySqlMapper<Estimate> {

}
