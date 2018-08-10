package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Abnormal;
import com.modelink.reservation.mapper.AbnormalMapper;
import com.modelink.reservation.service.AbnormalService;
import com.modelink.reservation.vo.AbnormalParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AbnormalServiceImpl implements AbnormalService {

    @Resource
    private AbnormalMapper abnormalMapper;

    /**
     * 插入一条记录
     *
     * @param abnormal
     * @return
     */
    @Override
    public int insert(Abnormal abnormal) {
        return abnormalMapper.insertSelective(abnormal);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param abnormal
     * @return
     */
    @Override
    public int countByParam(Abnormal abnormal) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param abnormal
     * @return
     */
    public Abnormal findOneByParam(Abnormal abnormal) {
        List<Abnormal> abnormalList = abnormalMapper.select(abnormal);
        if(abnormalList != null && abnormalList.size() > 0){
            return abnormalList.get(0);
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
    public List<Abnormal> findListByParam(AbnormalParamPagerVo paramPagerVo) {
        Example example = new Example(Abnormal.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("date", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("date", chooseDates[0]);
        }

        List<Abnormal> abnormalList = abnormalMapper.selectByExample(example);
        return abnormalList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Abnormal> findPagerByParam(AbnormalParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Abnormal.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("date", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("date", chooseDates[0]);
        }
        example.setOrderByClause("date desc");
        List<Abnormal> abnormalList = abnormalMapper.selectByExample(example);
        PageInfo<Abnormal> pageInfo = new PageInfo<>(abnormalList);
        return pageInfo;
    }
}
