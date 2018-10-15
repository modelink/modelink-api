package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.EstimationParamPagerVo;
import com.modelink.reservation.bean.Estimation;

import java.util.List;

/**
 * 预约服务接口
 */
public interface EstimationService {

    /**
     * 插入一条测保记录
     * @param estimation
     * @return
     */
    public int insert(Estimation estimation);

    /**
     * 查询符合条件的记录总数
     * @param estimation
     * @return
     */
    public int countByParam(Estimation estimation);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<Estimation> findListByParam(EstimationParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<Estimation> findPagerByParam(EstimationParamPagerVo paramPagerVo);
}
