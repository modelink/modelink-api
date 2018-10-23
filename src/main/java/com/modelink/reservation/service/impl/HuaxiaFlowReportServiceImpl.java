package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.huaxiaReport.HuaxiaReportParamVo;
import com.modelink.reservation.bean.HuaxiaFlowReport;
import com.modelink.reservation.mapper.HuaxiaFlowReportMapper;
import com.modelink.reservation.service.HuaxiaFlowReportService;
import com.modelink.reservation.vo.HuaxiaFlowReportParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class HuaxiaFlowReportServiceImpl implements HuaxiaFlowReportService {

    @Resource
    private HuaxiaFlowReportMapper huaxiaFlowReportMapper;

    /**
     * 插入一条承保记录
     *
     * @param huaxiaFlowReport
     * @return
     */
    @Override
    public int insert(HuaxiaFlowReport huaxiaFlowReport) {
        return huaxiaFlowReportMapper.insertSelective(huaxiaFlowReport);
    }

    /**
     * 更新一条记录
     * @param huaxiaFlowReport
     * @return
     */
    @Override
    public int update(HuaxiaFlowReport huaxiaFlowReport) {
        return huaxiaFlowReportMapper.updateByPrimaryKey(huaxiaFlowReport);
    }

    /**
     * 查询符合条件的记录
     * @param huaxiaFlowReport
     * @return
     */
    @Override
    public HuaxiaFlowReport findOneByParam(HuaxiaFlowReport huaxiaFlowReport) {
        List<HuaxiaFlowReport> huaxiaFlowReportList = huaxiaFlowReportMapper.select(huaxiaFlowReport);
        if(huaxiaFlowReportList != null && huaxiaFlowReportList.size() > 0){
            return huaxiaFlowReportList.get(0);
        }
        return null;
    }

    /**
     * 查询符合条件的记录列表
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public List<HuaxiaFlowReport> findListByParam(HuaxiaFlowReportParamPagerVo paramPagerVo) {
        Example example = new Example(HuaxiaFlowReport.class);
        if(StringUtils.hasText(paramPagerVo.getColumnFieldIds())) {
            example.selectProperties(paramPagerVo.getColumnFieldIds().split(","));
        }
        String dateField = paramPagerVo.getDateField();
        if(StringUtils.isEmpty(dateField)){
            dateField = "date";
        }
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
        }
        if(StringUtils.hasText(paramPagerVo.getPlatformName())){
            criteria.andEqualTo("platformName", paramPagerVo.getPlatformName());
        }
        if(StringUtils.hasText(paramPagerVo.getAdvertiseActive())){
            criteria.andIn("advertiseActive", Arrays.asList(paramPagerVo.getAdvertiseActive().split(",")));
        }
        if(StringUtils.hasText(paramPagerVo.getDataSource())){
            criteria.andEqualTo("dataSource", paramPagerVo.getDataSource());
        }
        List<HuaxiaFlowReport> huaxiaFlowReportList = huaxiaFlowReportMapper.selectByExample(example);
        return huaxiaFlowReportList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<HuaxiaFlowReport> findPagerByParam(HuaxiaFlowReportParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(HuaxiaFlowReport.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.hasText(paramPagerVo.getColumnFieldIds())) {
            example.selectProperties(paramPagerVo.getColumnFieldIds().split(","));
        }
        String dateField = paramPagerVo.getDateField();
        if(StringUtils.isEmpty(dateField)){
            dateField = "date";
        }
        if(StringUtils.hasText(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
        }
        if(StringUtils.hasText(paramPagerVo.getDataSource())){
            criteria.andEqualTo("dataSource", paramPagerVo.getDataSource());
        }
        if(StringUtils.hasText(paramPagerVo.getPlatformName())){
            criteria.andEqualTo("platformName", paramPagerVo.getPlatformName());
        }
        if(StringUtils.hasText(paramPagerVo.getAdvertiseActive())){
            criteria.andIn("advertiseActive", Arrays.asList(paramPagerVo.getAdvertiseActive().split(",")));
        }
        example.setOrderByClause("date desc");
        List<HuaxiaFlowReport> huaxiaFlowReportList = huaxiaFlowReportMapper.selectByExample(example);
        PageInfo<HuaxiaFlowReport> pageInfo = new PageInfo<>(huaxiaFlowReportList);
        return pageInfo;
    }

    /**
     * 根据查询条件查询相应的记录列表（按日期分组）
     * @param paramVo
     * @return
     */
    @Override
    public Map<String, HuaxiaFlowReport> findMapByParamGroup(HuaxiaReportParamVo paramVo) {
        if (StringUtils.isEmpty(paramVo.getChooseDate())) {
            return new HashMap<>();
        }
        String[] dateArray = paramVo.getChooseDate().split(" - ");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startDate", dateArray[0]);
        paramMap.put("endDate", dateArray[1]);
        paramMap.put("dataSource", paramVo.getDataSource());

        Map<String, HuaxiaFlowReport> huaxiaFlowReportMap = huaxiaFlowReportMapper.findMapByParamGroup(paramMap);
        if (huaxiaFlowReportMap == null) {
            huaxiaFlowReportMap = new HashMap<>();
        }
        return huaxiaFlowReportMap;
    }

    /**
     * 根据查询条件查询相应的记录列表（按指定属性分组）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public List<HuaxiaFlowReport> findListByParamGroup(HuaxiaFlowReportParamPagerVo paramPagerVo) {
        if (StringUtils.isEmpty(paramPagerVo.getChooseDate())) {
            return new ArrayList<>();
        }
        String[] dateArray = paramPagerVo.getChooseDate().split(" - ");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startDate", dateArray[0]);
        paramMap.put("endDate", dateArray[1]);
        paramMap.put("dataSource", paramPagerVo.getDataSource());
        paramMap.put("platformName", paramPagerVo.getPlatformName());
        if (StringUtils.hasText(paramPagerVo.getAdvertiseActive())) {
            paramMap.put("advertiseActiveList", Arrays.asList(paramPagerVo.getAdvertiseActive().split(",")));
        }

        List<HuaxiaFlowReport> flowReportList = huaxiaFlowReportMapper.findListByParamGroup(paramMap);
        if (flowReportList == null) {
            flowReportList = new ArrayList<>();
        }
        return flowReportList;
    }

    /**
     * 根据查询条件查询相应的记录列表（按指定月份分组）
     * @param paramPagerVo
     * @return
     */
    public List<HuaxiaFlowReport> findListByMonthGroup(HuaxiaFlowReportParamPagerVo paramPagerVo) {
        if (StringUtils.isEmpty(paramPagerVo.getChooseDate())) {
            return new ArrayList<>();
        }
        String[] dateArray = paramPagerVo.getChooseDate().split(" - ");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startDate", dateArray[0]);
        paramMap.put("endDate", dateArray[1]);
        paramMap.put("dataSource", paramPagerVo.getDataSource());
        paramMap.put("platformName", paramPagerVo.getPlatformName());
        if (StringUtils.hasText(paramPagerVo.getAdvertiseActive())) {
            paramMap.put("advertiseActiveList", Arrays.asList(paramPagerVo.getAdvertiseActive().split(",")));
        }
        List<HuaxiaFlowReport> flowReportList = huaxiaFlowReportMapper.findListByMonthGroup(paramMap);
        if (flowReportList == null) {
            flowReportList = new ArrayList<>();
        }
        return flowReportList;
    }

    /**
     * 根据查询条件查询相应的记录列表（按指定月份分组）
     *
     * @param paramVo
     * @return
     */
    @Override
    public Map<String, Map<String, Object>> findMapByMonthGroup(HuaxiaReportParamVo paramVo) {

        if (StringUtils.isEmpty(paramVo.getChooseDate())) {
            return new HashMap<>();
        }
        String[] dateArray = paramVo.getChooseDate().split(" - ");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startDate", dateArray[0]);
        paramMap.put("endDate", dateArray[1]);
        paramMap.put("dataSource", paramVo.getDataSource());
        Map<String, Map<String, Object>> mediaItemMap = huaxiaFlowReportMapper.findMapByMonthGroup(paramMap);
        if (mediaItemMap == null) {
            mediaItemMap = new HashMap<>();
        }
        return mediaItemMap;
    }
}
