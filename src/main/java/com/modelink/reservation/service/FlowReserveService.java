package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;

import java.util.List;

/**
 * 地区流量数据服务接口
 */
public interface FlowReserveService {

    /**
     * 插入一条地区流量数据记录
     * @param flowReserve
     * @return
     */
    public int insert(FlowReserve flowReserve);

    /**
     * 更新一条记录
     * @param flowReserve
     * @return
     */
    public int update(FlowReserve flowReserve);

    /**
     * 查询符合条件的记录总数
     * @param flowReserve
     * @return
     */
    public int countByParam(FlowReserve flowReserve);

    /**
     * 查询符合条件的记录
     * @param flowReserve
     * @return
     */
    public FlowReserve findOneByParam(FlowReserve flowReserve);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<FlowReserve> findListByParam(FlowReserveParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<FlowReserve> findPagerByParam(FlowReserveParamPagerVo paramPagerVo);

    /**
     * 获取指定日期内的数据（只查日期与联系方式两列，节省内存）
     * @param paramVo
     * @return
     */
    public List<FlowReserve> findListWithLimitColumnByDateRange(DashboardParamVo paramVo);
}
