package com.modelink.usercenter.service;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.common.AreaParamPagerVo;
import com.modelink.usercenter.bean.Area;

import java.util.List;

public interface AreaService {

    /** 插入一条记录 **/
    public int insert(Area area);
    /** 更新一条记录 **/
    public int update(Area area);
    /** 根据ID查询记录 **/
    public Area findById(Integer areaId);
    /** 根据ID查询记录 **/
    public List<Area> findByIdList(List<Integer> areaIdList);
    /** 查询符合条件的记录 **/
    public Area findByNameAndType(String areaName, int areaType);
    /** 根据条件查询记录列表 **/
    public List<Area> findListByParam(Area area);
    /** 根据条件查询记录列表（支持分页） **/
    public PageInfo<Area> findPagerByParam(AreaParamPagerVo areaParamPagerVo);
}
