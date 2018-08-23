package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Permiums;
import com.modelink.reservation.mapper.PermiumsMapper;
import com.modelink.reservation.service.PermiumsService;
import com.modelink.reservation.vo.PermiumsParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermiumsServiceImpl implements PermiumsService {

    @Resource
    private PermiumsMapper permiumsMapper;

    /**
     * 插入一条记录
     *
     * @param permiums
     * @return
     */
    @Override
    public int insert(Permiums permiums) {
        return permiumsMapper.insertSelective(permiums);
    }

    /**
     * 更新一条记录
     *
     * @param permiums
     * @return
     */
    @Override
    public int update(Permiums permiums) {
        return permiumsMapper.updateByPrimaryKeySelective(permiums);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param permiums
     * @return
     */
    @Override
    public int countByParam(Permiums permiums) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param permiums
     * @return
     */
    public Permiums findOneByParam(Permiums permiums) {
        List<Permiums> permiumsList = permiumsMapper.select(permiums);
        if(permiumsList != null && permiumsList.size() > 0){
            return permiumsList.get(0);
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
    public List<Permiums> findListByParam(PermiumsParamPagerVo paramPagerVo) {
        Example example = new Example(Permiums.class);
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
        List<Permiums> permiumsList = permiumsMapper.selectByExample(example);
        return permiumsList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Permiums> findPagerByParam(PermiumsParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Permiums.class);
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
        List<Permiums> permiumsList = permiumsMapper.selectByExample(example);
        PageInfo<Permiums> pageInfo = new PageInfo<>(permiumsList);
        return pageInfo;
    }
}
