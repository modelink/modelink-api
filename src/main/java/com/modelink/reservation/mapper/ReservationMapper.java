package com.modelink.reservation.mapper;

import com.modelink.reservation.bean.Reservation;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface ReservationMapper extends Mapper<Reservation>, MySqlMapper<Reservation> {

}
