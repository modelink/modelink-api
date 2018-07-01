package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.InsuranceParamPagerVo;
import com.modelink.reservation.bean.Insurance;
import com.modelink.reservation.mapper.InsuranceMapper;
import com.modelink.reservation.service.InsuranceService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    @Resource
    private InsuranceMapper insuranceMapper;

    /**
     * 插入一条承保记录
     *
     * @param insurance
     * @return
     */
    @Override
    public int insert(Insurance insurance) {
        return insuranceMapper.insertSelective(insurance);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param insurance
     * @return
     */
    @Override
    public int countByParam(Insurance insurance) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param insurance
     * @return
     */
    public Insurance findOneByParam(Insurance insurance) {
        List<Insurance> insuranceList = insuranceMapper.select(insurance);
        if(insuranceList != null && insuranceList.size() > 0){
            return insuranceList.get(0);
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
    public List<Insurance> findListByParam(InsuranceParamPagerVo paramPagerVo) {
        Example example = new Example(Insurance.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("createTime", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("createTime", chooseDates[0]);
        }
        if(!StringUtils.isEmpty(paramPagerVo.getMobile())) {
            criteria.andEqualTo("mobile", paramPagerVo.getMobile());
        }

        List<Insurance> insuranceList = insuranceMapper.selectByExample(example);
        return insuranceList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Insurance> findPagerByParam(InsuranceParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Insurance.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("createTime", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("createTime", chooseDates[0]);
        }
        if(!StringUtils.isEmpty(paramPagerVo.getMobile())) {
            criteria.andEqualTo("mobile", paramPagerVo.getMobile());
        }
        example.setOrderByClause("create_time desc");
        List<Insurance> insuranceList = insuranceMapper.selectByExample(example);
        PageInfo<Insurance> pageInfo = new PageInfo<>(insuranceList);
        return pageInfo;
    }
}
