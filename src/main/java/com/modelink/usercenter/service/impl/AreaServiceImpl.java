package com.modelink.usercenter.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.AreaParamPagerVo;
import com.modelink.usercenter.bean.Area;
import com.modelink.usercenter.mapper.AreaMapper;
import com.modelink.usercenter.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    public static Logger logger = LoggerFactory.getLogger(AreaService.class);

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
     * 根据ID查询记录
     *
     * @param areaIdList
     **/
    @Override
    public List<Area> findByIdList(List<Integer> areaIdList) {
        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        if(areaIdList != null && areaIdList.size() > 0) {
            criteria.andIn("areaId", areaIdList);
        }
        List<Area> areaList = areaMapper.selectByExample(example);
        return areaList;
    }

    /**
     * 查询符合条件的记录
     *
     * @param areaName
     * @param areaType
     **/
    public Area findByNameAndType(String areaName, int areaType) {
        if("未知地区".equals(areaName) || "-".equals(areaName)){
            return null;
        }
        Area area = new Area();
        area.setAreaName(areaName);
        area.setAreaType(areaType);
        area = areaMapper.selectOne(area);
        if(area == null){
            logger.error("[areaService|findByNameAndType]{}不存在，地区类型为{}", areaName, areaType);
        }
        return area;
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
