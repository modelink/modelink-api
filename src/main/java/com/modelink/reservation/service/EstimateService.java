package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Estimate;
import com.modelink.reservation.vo.EstimateParamPagerVo;

import java.util.List;

/**
 * 测保服务接口
 */
public interface EstimateService {

    /**
     * 插入一条记录
     * @param estimate
     * @return
     */
    public int insert(Estimate estimate);

    /**
     * 更新一条记录
     * @param estimate
     * @return
     */
    public int update(Estimate estimate);

    /**
     * 查询符合条件的记录总数
     * @param estimate
     * @return
     */
    public int countByParam(Estimate estimate);

    /**
     * 查询符合条件的记录
     * @param estimate
     * @return
     */
    public Estimate findOneByParam(Estimate estimate);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<Estimate> findListByParam(EstimateParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<Estimate> findPagerByParam(EstimateParamPagerVo paramPagerVo);
}
