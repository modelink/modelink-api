package com.modelink.reservation.service;

import com.modelink.reservation.bean.Reservation;

import java.util.List;

/**
 * 预约服务接口
 */
public interface ReservationService {

    /**
     * 插入一条预约记录
     * @param reservation
     * @return
     */
    public int insert(Reservation reservation);

    /**
     * 查询符合条件的记录总数
     * @param reservation
     * @return
     */
    public int countByParam(Reservation reservation);

    /**
     * 查询符合条件的记录列表
     * @param reservation
     * @return
     */
    public List<Reservation> findListByParam(Reservation reservation);
}
