package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.AdvertiseAnalyse;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface AdvertiseAnalyseMapper extends Mapper<AdvertiseAnalyse>, MySqlMapper<AdvertiseAnalyse> {

}
