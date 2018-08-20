package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.MediaTactics;
import com.modelink.reservation.mapper.MediaTacticsMapper;
import com.modelink.reservation.service.MediaTacticsService;
import com.modelink.reservation.vo.MediaTacticsParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MediaTacticsServiceImpl implements MediaTacticsService {

    @Resource
    private MediaTacticsMapper mediaTacticsMapper;

    /**
     * 插入一条记录
     *
     * @param mediaTactics
     * @return
     */
    @Override
    public int insert(MediaTactics mediaTactics) {
        return mediaTacticsMapper.insertSelective(mediaTactics);
    }

    /**
     * 更新一条记录
     *
     * @param mediaTactics
     * @return
     */
    @Override
    public int update(MediaTactics mediaTactics) {
        return mediaTacticsMapper.updateByPrimaryKeySelective(mediaTactics);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param mediaTactics
     * @return
     */
    @Override
    public int countByParam(MediaTactics mediaTactics) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param mediaTactics
     * @return
     */
    public MediaTactics findOneByParam(MediaTactics mediaTactics) {
        List<MediaTactics> mediaTacticsList = mediaTacticsMapper.select(mediaTactics);
        if(mediaTacticsList != null && mediaTacticsList.size() > 0){
            return mediaTacticsList.get(0);
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
    public List<MediaTactics> findListByParam(MediaTacticsParamPagerVo paramPagerVo) {
        Example example = new Example(MediaTactics.class);
        Example.Criteria criteria = example.createCriteria();
        String dateField = paramPagerVo.getDateField();
        if(StringUtils.isEmpty(dateField)){
            dateField = "month";
        }
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
        }

        List<MediaTactics> mediaTacticsList = mediaTacticsMapper.selectByExample(example);
        return mediaTacticsList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<MediaTactics> findPagerByParam(MediaTacticsParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(MediaTactics.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo("month", chooseDates[1]);
            criteria.andGreaterThanOrEqualTo("month", chooseDates[0]);
        }
        example.setOrderByClause("month desc");
        List<MediaTactics> mediaTacticsList = mediaTacticsMapper.selectByExample(example);
        PageInfo<MediaTactics> pageInfo = new PageInfo<>(mediaTacticsList);
        return pageInfo;
    }
}
