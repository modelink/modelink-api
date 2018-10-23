package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.huaxiaReport.HuaxiaReportParamVo;
import com.modelink.reservation.bean.HuaxiaFlowReport;
import com.modelink.reservation.vo.HuaxiaFlowReportParamPagerVo;

import java.util.List;
import java.util.Map;

/**
 * 华夏日报基础流量数据服务接口
 */
public interface HuaxiaFlowReportService {

    /**
     * 插入一条记录
     * @param huaxiaFlowReport
     * @return
     */
    public int insert(HuaxiaFlowReport huaxiaFlowReport);

    /**
     * 更新一条记录
     * @param huaxiaFlowReport
     * @return
     */
    public int update(HuaxiaFlowReport huaxiaFlowReport);

    /**
     * 查询符合条件的记录
     * @param huaxiaFlowReport
     * @return
     */
    public HuaxiaFlowReport findOneByParam(HuaxiaFlowReport huaxiaFlowReport);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<HuaxiaFlowReport> findListByParam(HuaxiaFlowReportParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<HuaxiaFlowReport> findPagerByParam(HuaxiaFlowReportParamPagerVo paramPagerVo);

    /**
     * 根据查询条件查询相应的记录列表（按指定属性分组）
     * @param paramVo
     * @return
     */
    public Map<String, HuaxiaFlowReport> findMapByParamGroup(HuaxiaReportParamVo paramVo);

    /**
     * 根据查询条件查询相应的记录列表（按指定属性分组）
     * @param paramPagerVo
     * @return
     */
    public List<HuaxiaFlowReport> findListByParamGroup(HuaxiaFlowReportParamPagerVo paramPagerVo);

    /**
     * 根据查询条件查询相应的记录列表（按指定月份分组）
     * @param paramPagerVo
     * @return
     */
    public List<HuaxiaFlowReport> findListByMonthGroup(HuaxiaFlowReportParamPagerVo paramPagerVo);
}
