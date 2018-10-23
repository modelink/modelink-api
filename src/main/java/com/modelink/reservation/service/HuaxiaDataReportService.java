package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.HuaxiaDataReport;
import com.modelink.reservation.vo.HuaxiaDataReportParamPagerVo;

import java.util.List;

/**
 * 华夏日报基础数量数据服务接口
 */
public interface HuaxiaDataReportService {

    /**
     * 插入一条记录
     * @param huaxiaDataReport
     * @return
     */
    public int insert(HuaxiaDataReport huaxiaDataReport);

    /**
     * 更新一条记录
     * @param huaxiaDataReport
     * @return
     */
    public int update(HuaxiaDataReport huaxiaDataReport);

    /**
     * 查询符合条件的记录
     * @param huaxiaDataReport
     * @return
     */
    public HuaxiaDataReport findOneByParam(HuaxiaDataReport huaxiaDataReport);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<HuaxiaDataReport> findListByParam(HuaxiaDataReportParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<HuaxiaDataReport> findPagerByParam(HuaxiaDataReportParamPagerVo paramPagerVo);

    /**
     * 根据查询条件查询相应的记录列表（按指定月份分组）
     * @param paramPagerVo
     * @return
     */
    public List<HuaxiaDataReport> findListByMonthGroup(HuaxiaDataReportParamPagerVo paramPagerVo);
}
