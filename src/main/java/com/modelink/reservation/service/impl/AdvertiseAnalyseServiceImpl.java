package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.AdvertiseParamPagerVo;
import com.modelink.reservation.bean.AdvertiseAnalyse;
import com.modelink.reservation.mapper.AdvertiseAnalyseMapper;
import com.modelink.reservation.service.AdvertiseAnalyseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdvertiseAnalyseServiceImpl implements AdvertiseAnalyseService {

    @Resource
    private AdvertiseAnalyseMapper advertiseAnalyseMapper;

    /**
     * 插入一条承保记录
     *
     * @param advertiseAnalyse
     * @return
     */
    @Override
    public int insert(AdvertiseAnalyse advertiseAnalyse) {
        return advertiseAnalyseMapper.insertSelective(advertiseAnalyse);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param advertiseAnalyse
     * @return
     */
    @Override
    public int countByParam(AdvertiseAnalyse advertiseAnalyse) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param advertiseAnalyse
     * @return
     */
    public AdvertiseAnalyse findOneByParam(AdvertiseAnalyse advertiseAnalyse) {
        List<AdvertiseAnalyse> insuranceList = advertiseAnalyseMapper.select(advertiseAnalyse);
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
    public List<AdvertiseAnalyse> findListByParam(AdvertiseParamPagerVo paramPagerVo) {
        Example example = new Example(AdvertiseAnalyse.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("statTime", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("statTime", chooseDates[0]);
        }

        List<AdvertiseAnalyse> insuranceList = advertiseAnalyseMapper.selectByExample(example);
        return insuranceList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<AdvertiseAnalyse> findPagerByParam(AdvertiseParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(AdvertiseAnalyse.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThan("statTime", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("statTime", chooseDates[0]);
        }

        example.setOrderByClause("id desc");
        List<AdvertiseAnalyse> insuranceList = advertiseAnalyseMapper.selectByExample(example);
        PageInfo<AdvertiseAnalyse> pageInfo = new PageInfo<>(insuranceList);
        return pageInfo;
    }
}
