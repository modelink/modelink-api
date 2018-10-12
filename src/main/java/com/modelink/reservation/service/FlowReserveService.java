package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;

import java.util.List;
import java.util.Set;

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
     * 查询符合条件的列表
     * @param mobileSet
     * @return
     */
    public List<FlowReserve> findListByMobiles(Set<String> mobileSet, String sortField);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<FlowReserve> findPagerByParam(FlowReserveParamPagerVo paramPagerVo);

    /**
     * 查询广告活动列表
     * @return
     */
    public List<String> findAdvertiseActiveList();
}
