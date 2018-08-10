package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.FlowArea;
import com.modelink.reservation.vo.FlowAreaParamPagerVo;

import java.util.List;

/**
 * 地区流量数据服务接口
 */
public interface FlowAreaService {

    /**
     * 插入一条地区流量数据记录
     * @param flowArea
     * @return
     */
    public int insert(FlowArea flowArea);

    /**
     * 查询符合条件的记录总数
     * @param flowArea
     * @return
     */
    public int countByParam(FlowArea flowArea);

    /**
     * 查询符合条件的记录
     * @param flowArea
     * @return
     */
    public FlowArea findOneByParam(FlowArea flowArea);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<FlowArea> findListByParam(FlowAreaParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<FlowArea> findPagerByParam(FlowAreaParamPagerVo paramPagerVo);
}
