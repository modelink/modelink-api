package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Flow;
import com.modelink.reservation.vo.FlowParamPagerVo;

import java.util.List;

/**
 * 地区流量数据服务接口
 */
public interface FlowService {

    /**
     * 插入一条记录
     * @param flow
     * @return
     */
    public int insert(Flow flow);

    /**
     * 更新一条记录
     * @param flow
     * @return
     */
    public int update(Flow flow);

    /**
     * 查询符合条件的记录总数
     * @param flow
     * @return
     */
    public int countByParam(Flow flow);

    /**
     * 查询符合条件的记录
     * @param flow
     * @return
     */
    public Flow findOneByParam(Flow flow);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<Flow> findListByParam(FlowParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<Flow> findPagerByParam(FlowParamPagerVo paramPagerVo);
}
