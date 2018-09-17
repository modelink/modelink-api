package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Estimate;
import com.modelink.reservation.mapper.EstimateMapper;
import com.modelink.reservation.service.EstimateService;
import com.modelink.reservation.vo.EstimateParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EstimateServiceImpl implements EstimateService {

    @Resource
    private EstimateMapper estimateMapper;

    /**
     * 插入一条记录
     *
     * @param estimate
     * @return
     */
    @Override
    public int insert(Estimate estimate) {
        return estimateMapper.insertSelective(estimate);
    }

    /**
     * 更新一条记录
     *
     * @param estimate
     * @return
     */
    @Override
    public int update(Estimate estimate) {
        return estimateMapper.updateByPrimaryKeySelective(estimate);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param estimate
     * @return
     */
    @Override
    public int countByParam(Estimate estimate) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param estimate
     * @return
     */
    public Estimate findOneByParam(Estimate estimate) {
        List<Estimate> estimateList = estimateMapper.select(estimate);
        if(estimateList != null && estimateList.size() > 0){
            return estimateList.get(0);
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
    public List<Estimate> findListByParam(EstimateParamPagerVo paramPagerVo) {
        Example example = new Example(Estimate.class);
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
        if(StringUtils.hasText(paramPagerVo.getPlatformName())){
            if("OTHER".equals(paramPagerVo.getPlatformName())) {
                List<String> list = new ArrayList<>();
                list.add("PC");
                list.add("WAP");
                criteria.andNotIn("platformName", list);
            }else{
                criteria.andEqualTo("platformName", paramPagerVo.getPlatformName());
            }
        }
        if(StringUtils.hasText(paramPagerVo.getAdvertiseActive())){
            criteria.andLike("advertiseActive", "%" + paramPagerVo.getAdvertiseActive() + "%");
        }
        List<Estimate> estimateList = estimateMapper.selectByExample(example);
        return estimateList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Estimate> findPagerByParam(EstimateParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Estimate.class);
        Example.Criteria criteria = example.createCriteria();
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
        List<Estimate> estimateList = estimateMapper.selectByExample(example);
        PageInfo<Estimate> pageInfo = new PageInfo<>(estimateList);
        return pageInfo;
    }
}
