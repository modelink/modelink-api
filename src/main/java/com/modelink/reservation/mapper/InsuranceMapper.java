package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Insurance;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface InsuranceMapper extends Mapper<Insurance>, MySqlMapper<Insurance> {

}
