package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.FlowReserve;
import com.modelink.reservation.mapper.FlowReserveMapper;
import com.modelink.reservation.service.FlowReserveService;
import com.modelink.reservation.vo.FlowReserveParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("date", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("date", chooseDates[0]);
        }

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
            criteria.andLessThan("date", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("date", chooseDates[0]);
        }
        example.setOrderByClause("date desc");
        List<FlowReserve> flowReserveList = flowReserveMapper.selectByExample(example);
        PageInfo<FlowReserve> pageInfo = new PageInfo<>(flowReserveList);
        return pageInfo;
    }
}
