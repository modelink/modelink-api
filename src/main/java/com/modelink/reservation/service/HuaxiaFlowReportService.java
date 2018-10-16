package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.HuaxiaFlowReport;
import com.modelink.reservation.vo.HuaxiaFlowReportParamPagerVo;

import java.util.List;

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
}
