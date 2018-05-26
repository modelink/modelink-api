package com.modelink.reservation.service.impl;

import com.modelink.reservation.bean.Reservation;
import com.modelink.reservation.mapper.ReservationMapper;
import com.modelink.reservation.service.ReservationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Resource
    private ReservationMapper reservationMapper;

    /**
     * 插入一条预约记录
     *
     * @param reservation
     * @return
     */
    @Override
    public int insert(Reservation reservation) {
        return reservationMapper.insert(reservation);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param reservation
     * @return
     */
    @Override
    public int countByParam(Reservation reservation) {
        return reservationMapper.selectCount(reservation);
    }

    /**
     * 查询符合条件的记录列表
     *
     * @param reservation
     * @return
     */
    @Override
    public List<Reservation> findListByParam(Reservation reservation) {
        return reservationMapper.select(reservation);
    }
}
