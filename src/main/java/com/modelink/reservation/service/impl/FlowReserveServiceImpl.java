package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.DashboardParamVo;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.mapper.FlowReserveMapper;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FlowReserveServiceImpl implements FlowReserveService {

    @Resource
    private FlowReserveMapper flowReserveMapper;

    /**
     * 插入一条记录
     *
     * @param flowReserve
     * @return
     */
    @Override
    public int insert(FlowReserve flowReserve) {
        return flowReserveMapper.insertSelective(flowReserve);
    }

    /**
     * 更新一条记录
     *
     * @param flowReserve
     * @return
     */
    @Override
    public int update(FlowReserve flowReserve) {
        return flowReserveMapper.updateByPrimaryKeySelective(flowReserve);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param flowReserve
     * @return
     */
    @Override
    public int countByParam(FlowReserve flowReserve) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param flowReserve
     * @return
     */
    public FlowReserve findOneByParam(FlowReserve flowReserve) {
        List<FlowReserve> flowReserveList = flowReserveMapper.select(flowReserve);
        if(flowReserveList != null && flowReserveList.size() > 0){
            return flowReserveList.get(0);
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
    public List<FlowReserve> findListByParam(FlowReserveParamPagerVo paramPagerVo) {
        Example example = new Example(FlowReserve.class);
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
        if(StringUtils.hasText(paramPagerVo.getMobile())){
            criteria.andEqualTo("reserveMobile", paramPagerVo.getMobile());
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
        if(paramPagerVo.getProvinceId() != null){
            criteria.andEqualTo("provinceId", paramPagerVo.getProvinceId());
        }
        if(StringUtils.hasText(paramPagerVo.getFeeType())){
            criteria.andEqualTo("feeType", paramPagerVo.getFeeType());
        }
        criteria.andEqualTo("isMakeUp", 0);
        if(StringUtils.hasText(paramPagerVo.getSortField())) {
            example.setOrderByClause(paramPagerVo.getSortField());
        }
        List<FlowReserve> flowReserveList = flowReserveMapper.selectByExample(example);
        return flowReserveList;
    }

    /**
     * 查询符合条件的列表
     *
     * @param mobileSet
     * @return
     */
    @Override
    public List<FlowReserve> findListByMobiles(Set<String> mobileSet, String sortField) {
        if(mobileSet == null || mobileSet.size() <= 0){
            return new ArrayList<>();
        }
        Example example = new Example(FlowReserve.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("reserveMobile", mobileSet);
        if(StringUtils.hasText(sortField)) {
            example.setOrderByClause(sortField);
        }
        criteria.andEqualTo("isMakeUp", 0);
        List<FlowReserve> flowReserveList = flowReserveMapper.selectByExample(example);
        return flowReserveList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<FlowReserve> findPagerByParam(FlowReserveParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(FlowReserve.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo("date", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("date", chooseDates[0]);
        }
        example.setOrderByClause("date desc");
        List<FlowReserve> flowReserveList = flowReserveMapper.selectByExample(example);
        PageInfo<FlowReserve> pageInfo = new PageInfo<>(flowReserveList);
        return pageInfo;
    }
}
