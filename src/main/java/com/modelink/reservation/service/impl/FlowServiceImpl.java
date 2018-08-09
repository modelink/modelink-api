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
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("createTime", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("createTime", chooseDates[0]);
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
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("createTime", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("createTime", chooseDates[0]);
        }
        example.setOrderByClause("create_time desc");
        List<Flow> flowList = flowMapper.selectByExample(example);
        PageInfo<Flow> pageInfo = new PageInfo<>(flowList);
        return pageInfo;
    }
}
