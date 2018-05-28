package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.ReservationParamPagerVo;
import com.modelink.reservation.bean.Reservation;
import com.modelink.reservation.mapper.ReservationMapper;
import com.modelink.reservation.service.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

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

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Reservation> findPagerByParam(ReservationParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Reservation.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("createTime", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("createTime", chooseDates[0]);
        }
        if(!StringUtils.isEmpty(paramPagerVo.getContactMobile())) {
            criteria.andEqualTo("contactMobile", paramPagerVo.getContactMobile());
        }

        List<Reservation> reservationList = reservationMapper.selectByExample(example);
        PageInfo<Reservation> pageInfo = new PageInfo<>(reservationList);
        return pageInfo;
    }
}
