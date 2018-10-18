package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.reserve.EstimationParamPagerVo;
import com.modelink.reservation.bean.Estimation;
import com.modelink.reservation.mapper.EstimationMapper;
import com.modelink.reservation.service.EstimationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EstimationServiceImpl implements EstimationService {

    @Resource
    private EstimationMapper estimationMapper;

    /**
     * 插入一条测保记录
     *
     * @param estimation
     * @return
     */
    @Override
    public int insert(Estimation estimation) {
        return estimationMapper.insert(estimation);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param estimation
     * @return
     */
    @Override
    public int countByParam(Estimation estimation) {
        return estimationMapper.selectCount(estimation);
    }

    /**
     * 查询符合条件的记录列表
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public List<Estimation> findListByParam(EstimationParamPagerVo paramPagerVo) {
        Example example = new Example(Estimation.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo("createTime", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("createTime", chooseDates[0]);
        }
        if(!StringUtils.isEmpty(paramPagerVo.getContactMobile())) {
            criteria.andEqualTo("mobile", paramPagerVo.getContactMobile());
        }

        List<Estimation> estimationList = estimationMapper.selectByExample(example);
        return estimationList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Estimation> findPagerByParam(EstimationParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Estimation.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo("createTime", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("createTime", chooseDates[0]);
        }
        if(!StringUtils.isEmpty(paramPagerVo.getContactMobile())) {
            criteria.andEqualTo("mobile", paramPagerVo.getContactMobile());
        }
        example.setOrderByClause("id desc");
        List<Estimation> estimationList = estimationMapper.selectByExample(example);
        PageInfo<Estimation> pageInfo = new PageInfo<>(estimationList);
        return pageInfo;
    }
}
