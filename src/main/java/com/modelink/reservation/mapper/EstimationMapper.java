package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Estimation;
import com.modelink.reservation.bean.Reservation;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface EstimationMapper extends Mapper<Estimation>, MySqlMapper<Estimation> {

}
