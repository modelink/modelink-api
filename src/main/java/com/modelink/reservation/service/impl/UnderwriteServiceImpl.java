package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.mapper.UnderwriteMapper;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UnderwriteServiceImpl implements UnderwriteService {

    @Resource
    private UnderwriteMapper underwriteMapper;

    /**
     * 插入一条承保记录
     *
     * @param underwrite
     * @return
     */
    @Override
    public int insert(Underwrite underwrite) {
        return underwriteMapper.insertSelective(underwrite);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param underwrite
     * @return
     */
    @Override
    public int countByParam(Underwrite underwrite) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param underwrite
     * @return
     */
    public Underwrite findOneByParam(Underwrite underwrite) {
        List<Underwrite> underwriteList = underwriteMapper.select(underwrite);
        if(underwriteList != null && underwriteList.size() > 0){
            return underwriteList.get(0);
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
    public List<Underwrite> findListByParam(UnderwriteParamPagerVo paramPagerVo) {
        Example example = new Example(Underwrite.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("createTime", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("createTime", chooseDates[0]);
        }
        if(!StringUtils.isEmpty(paramPagerVo.getMobile())) {
            criteria.andEqualTo("mobile", paramPagerVo.getMobile());
        }

        List<Underwrite> underwriteList = underwriteMapper.selectByExample(example);
        return underwriteList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Underwrite> findPagerByParam(UnderwriteParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Underwrite.class);
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
        List<Underwrite> underwriteList = underwriteMapper.selectByExample(example);
        PageInfo<Underwrite> pageInfo = new PageInfo<>(underwriteList);
        return pageInfo;
    }
}
