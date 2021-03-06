package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Flow;
import com.modelink.reservation.mapper.FlowMapper;
import com.modelink.reservation.service.FlowService;
import com.modelink.reservation.vo.FlowParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlowServiceImpl implements FlowService {

    @Resource
    private FlowMapper flowMapper;

    /**
     * 插入一条承保记录
     *
     * @param flow
     * @return
     */
    @Override
    public int insert(Flow flow) {
        return flowMapper.insertSelective(flow);
    }

    /**
     * 更新一条记录
     *
     * @param flow
     * @return
     */
    @Override
    public int update(Flow flow) {
        return flowMapper.updateByPrimaryKeySelective(flow);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param flow
     * @return
     */
    @Override
    public int countByParam(Flow flow) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param flow
     * @return
     */
    public Flow findOneByParam(Flow flow) {
        List<Flow> flowList = flowMapper.select(flow);
        if(flowList != null && flowList.size() > 0){
            return flowList.get(0);
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
    public List<Flow> findListByParam(FlowParamPagerVo paramPagerVo) {
        Example example = new Example(Flow.class);
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
        if(StringUtils.hasText(paramPagerVo.getSource())){
            criteria.andEqualTo("source", paramPagerVo.getSource());
        }
        List<Flow> flowList = flowMapper.selectByExample(example);
        return flowList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Flow> findPagerByParam(FlowParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Flow.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.hasText(paramPagerVo.getColumnFieldIds())) {
            example.selectProperties(paramPagerVo.getColumnFieldIds().split(","));
        }
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo("date", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("date", chooseDates[0]);
        }
        example.setOrderByClause("date desc");
        List<Flow> flowList = flowMapper.selectByExample(example);
        PageInfo<Flow> pageInfo = new PageInfo<>(flowList);
        return pageInfo;
    }
}
