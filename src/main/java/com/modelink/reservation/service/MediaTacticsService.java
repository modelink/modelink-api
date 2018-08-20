package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.MediaTactics;
import com.modelink.reservation.vo.MediaTacticsParamPagerVo;

import java.util.List;

/**
 * 异常数据服务接口
 */
public interface MediaTacticsService {

    /**
     * 插入一条记录
     * @param mediaTactics
     * @return
     */
    public int insert(MediaTactics mediaTactics);

    /**
     * 更新一条记录
     * @param mediaTactics
     * @return
     */
    public int update(MediaTactics mediaTactics);

    /**
     * 查询符合条件的记录总数
     * @param mediaTactics
     * @return
     */
    public int countByParam(MediaTactics mediaTactics);

    /**
     * 查询符合条件的记录
     * @param mediaTactics
     * @return
     */
    public MediaTactics findOneByParam(MediaTactics mediaTactics);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<MediaTactics> findListByParam(MediaTacticsParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<MediaTactics> findPagerByParam(MediaTacticsParamPagerVo paramPagerVo);
}
