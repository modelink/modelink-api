package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Repellent;
import com.modelink.reservation.mapper.RepellentMapper;
import com.modelink.reservation.service.RepellentService;
import com.modelink.reservation.vo.RepellentParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RepellentServiceImpl implements RepellentService {

    @Resource
    private RepellentMapper repellentMapper;

    /**
     * 插入一条记录
     *
     * @param repellent
     * @return
     */
    @Override
    public int insert(Repellent repellent) {
        return repellentMapper.insertSelective(repellent);
    }

    /**
     * 更新一条记录
     *
     * @param repellent
     * @return
     */
    @Override
    public int update(Repellent repellent) {
        return repellentMapper.updateByPrimaryKeySelective(repellent);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param repellent
     * @return
     */
    @Override
    public int countByParam(Repellent repellent) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param repellent
     * @return
     */
    public Repellent findOneByParam(Repellent repellent) {
        List<Repellent> repellentList = repellentMapper.select(repellent);
        if(repellentList != null && repellentList.size() > 0){
            return repellentList.get(0);
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
    public List<Repellent> findListByParam(RepellentParamPagerVo paramPagerVo) {
        Example example = new Example(Repellent.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.hasText(paramPagerVo.getColumnFieldIds())) {
            example.selectProperties(paramPagerVo.getColumnFieldIds().split(","));
        }
        String dateField = paramPagerVo.getDateField();
        if(StringUtils.isEmpty(dateField)){
            dateField = "insuranceDate";
        }
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
        }
        List<Repellent> repellentList = repellentMapper.selectByExample(example);
        return repellentList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Repellent> findPagerByParam(RepellentParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Repellent.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo("insurance_date", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("insurance_date", chooseDates[0]);
        }
        example.setOrderByClause("insurance_date desc");
        List<Repellent> repellentList = repellentMapper.selectByExample(example);
        PageInfo<Repellent> pageInfo = new PageInfo<>(repellentList);
        return pageInfo;
    }
}
