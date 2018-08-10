package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.FlowArea;
import com.modelink.reservation.mapper.FlowAreaMapper;
import com.modelink.reservation.service.FlowAreaService;
import com.modelink.reservation.vo.FlowAreaParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FlowAreaServiceImpl implements FlowAreaService {

    @Resource
    private FlowAreaMapper flowAreaMapper;

    /**
     * 插入一条记录
     *
     * @param flowArea
     * @return
     */
    @Override
    public int insert(FlowArea flowArea) {
        return flowAreaMapper.insertSelective(flowArea);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param flowArea
     * @return
     */
    @Override
    public int countByParam(FlowArea flowArea) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param flowArea
     * @return
     */
    public FlowArea findOneByParam(FlowArea flowArea) {
        List<FlowArea> flowAreaList = flowAreaMapper.select(flowArea);
        if(flowAreaList != null && flowAreaList.size() > 0){
            return flowAreaList.get(0);
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
    public List<FlowArea> findListByParam(FlowAreaParamPagerVo paramPagerVo) {
        Example example = new Example(FlowArea.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("date", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("date", chooseDates[0]);
        }

        List<FlowArea> flowAreaList = flowAreaMapper.selectByExample(example);
        return flowAreaList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<FlowArea> findPagerByParam(FlowAreaParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(FlowArea.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("date", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("date", chooseDates[0]);
        }
        example.setOrderByClause("date desc");
        List<FlowArea> flowAreaList = flowAreaMapper.selectByExample(example);
        PageInfo<FlowArea> pageInfo = new PageInfo<>(flowAreaList);
        return pageInfo;
    }
}
