package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Permiums;
import com.modelink.reservation.vo.PermiumsParamPagerVo;

import java.util.List;

/**
 * 保费数据服务接口
 */
public interface PermiumsService {

    /**
     * 插入一条记录
     * @param permiums
     * @return
     */
    public int insert(Permiums permiums);

    /**
     * 更新一条记录
     * @param permiums
     * @return
     */
    public int update(Permiums permiums);

    /**
     * 查询符合条件的记录总数
     * @param permiums
     * @return
     */
    public int countByParam(Permiums permiums);

    /**
     * 查询符合条件的记录
     * @param permiums
     * @return
     */
    public Permiums findOneByParam(Permiums permiums);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<Permiums> findListByParam(PermiumsParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<Permiums> findPagerByParam(PermiumsParamPagerVo paramPagerVo);
}
