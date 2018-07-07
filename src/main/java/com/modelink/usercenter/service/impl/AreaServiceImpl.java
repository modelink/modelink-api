package com.modelink.usercenter.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.AreaParamPagerVo;
import com.modelink.usercenter.bean.Area;
import com.modelink.usercenter.mapper.AreaMapper;
import com.modelink.usercenter.service.AreaService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Resource
    private AreaMapper areaMapper;

    /**
     * 插入一条记录
     *
     * @param area
     **/
    @Override
    public int insert(Area area) {
        return areaMapper.insertSelective(area);
    }

    /**
     * 更新一条记录
     *
     * @param area
     **/
    @Override
    public int update(Area area) {
        return areaMapper.updateByPrimaryKeySelective(area);
    }

    /**
     * 根据ID查询记录
     *
     * @param areaId
     **/
    @Override
    public Area findById(Integer areaId) {
        return areaMapper.selectByPrimaryKey(areaId);
    }

    /**
     * 根据条件查询记录列表
     *
     * @param area
     **/
    @Override
    public List<Area> findListByParam(Area area) {
        return areaMapper.select(area);
    }

    /**
     * 根据条件查询记录列表（支持分页）
     *
     * @param paramPagerVo
     **/
    @Override
    public PageInfo<Area> findPagerByParam(AreaParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.hasText(paramPagerVo.getAreaId())){
            criteria.andEqualTo("areaId", paramPagerVo.getAreaId());
        }
        if(StringUtils.hasText(paramPagerVo.getAreaName())){
            criteria.andEqualTo("areaName", paramPagerVo.getAreaName());
        }
        if(StringUtils.hasText(paramPagerVo.getAreaType())){
            criteria.andEqualTo("areaType", paramPagerVo.getAreaType());
        }
        List<Area> areaList = areaMapper.selectByExample(example);
        return new PageInfo<>(areaList);
    }
}
