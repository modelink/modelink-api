package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.HuaxiaDataReport;
import com.modelink.reservation.mapper.HuaxiaDataReportMapper;
import com.modelink.reservation.service.HuaxiaDataReportService;
import com.modelink.reservation.vo.HuaxiaDataReportParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HuaxiaDataReportServiceImpl implements HuaxiaDataReportService {

    @Resource
    private HuaxiaDataReportMapper huaxiaDataReportMapper;

    /**
     * 插入一条承保记录
     *
     * @param huaxiaDataReport
     * @return
     */
    @Override
    public int insert(HuaxiaDataReport huaxiaDataReport) {
        return huaxiaDataReportMapper.insertSelective(huaxiaDataReport);
    }

    /**
     * 更新一条记录
     * @param huaxiaDataReport
     * @return
     */
    @Override
    public int update(HuaxiaDataReport huaxiaDataReport) {
        return huaxiaDataReportMapper.updateByPrimaryKey(huaxiaDataReport);
    }

    /**
     * 查询符合条件的记录
     * @param huaxiaDataReport
     * @return
     */
    @Override
    public HuaxiaDataReport findOneByParam(HuaxiaDataReport huaxiaDataReport) {
        List<HuaxiaDataReport> huaxiaDataReportList = huaxiaDataReportMapper.select(huaxiaDataReport);
        if(huaxiaDataReportList != null && huaxiaDataReportList.size() > 0){
            return huaxiaDataReportList.get(0);
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
    public List<HuaxiaDataReport> findListByParam(HuaxiaDataReportParamPagerVo paramPagerVo) {
        Example example = new Example(HuaxiaDataReport.class);
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
        if(StringUtils.hasText(paramPagerVo.getDataSource())){
            criteria.andEqualTo("dataSource", paramPagerVo.getDataSource());
        }
        List<HuaxiaDataReport> huaxiaDataReportList = huaxiaDataReportMapper.selectByExample(example);
        return huaxiaDataReportList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<HuaxiaDataReport> findPagerByParam(HuaxiaDataReportParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(HuaxiaDataReport.class);
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
        example.setOrderByClause("date desc");
        List<HuaxiaDataReport> huaxiaDataReportList = huaxiaDataReportMapper.selectByExample(example);
        PageInfo<HuaxiaDataReport> pageInfo = new PageInfo<>(huaxiaDataReportList);
        return pageInfo;
    }

}
