package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.HuaxiaFlowReport;
import com.modelink.reservation.mapper.HuaxiaFlowReportMapper;
import com.modelink.reservation.service.HuaxiaFlowReportService;
import com.modelink.reservation.vo.HuaxiaFlowReportParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

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
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
        }
        example.setOrderByClause("date desc");
        List<HuaxiaFlowReport> huaxiaFlowReportList = huaxiaFlowReportMapper.selectByExample(example);
        PageInfo<HuaxiaFlowReport> pageInfo = new PageInfo<>(huaxiaFlowReportList);
        return pageInfo;
    }

}
