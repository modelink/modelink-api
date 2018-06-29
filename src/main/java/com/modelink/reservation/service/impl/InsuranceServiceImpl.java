package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.ReservationParamPagerVo;
import com.modelink.reservation.bean.Insurance;
import com.modelink.reservation.mapper.InsuranceMapper;
import com.modelink.reservation.service.InsuranceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    @Resource
    private InsuranceMapper insuranceMapper;

    /**
     * 插入一条承保记录
     *
     * @param insurance
     * @return
     */
    @Override
    public int insert(Insurance insurance) {
        return insuranceMapper.insertSelective(insurance);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param insurance
     * @return
     */
    @Override
    public int countByParam(Insurance insurance) {
        return 0;
    }

    /**
     * 查询符合条件的记录列表
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public List<Insurance> findListByParam(ReservationParamPagerVo paramPagerVo) {
        return null;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Insurance> findPagerByParam(ReservationParamPagerVo paramPagerVo) {
        return null;
    }
}
