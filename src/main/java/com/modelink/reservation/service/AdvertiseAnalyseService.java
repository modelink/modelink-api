package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.basedata.AdvertiseParamPagerVo;
import com.modelink.reservation.bean.AdvertiseAnalyse;

import java.util.List;

/**
 * 预约服务接口
 */
public interface AdvertiseAnalyseService {

    /**
     * 插入一条承保记录
     * @param advertiseAnalyse
     * @return
     */
    public int insert(AdvertiseAnalyse advertiseAnalyse);

    /**
     * 查询符合条件的记录总数
     * @param advertiseAnalyse
     * @return
     */
    public int countByParam(AdvertiseAnalyse advertiseAnalyse);

    /**
     * 查询符合条件的记录
     * @param advertiseAnalyse
     * @return
     */
    public AdvertiseAnalyse findOneByParam(AdvertiseAnalyse advertiseAnalyse);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<AdvertiseAnalyse> findListByParam(AdvertiseParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<AdvertiseAnalyse> findPagerByParam(AdvertiseParamPagerVo paramPagerVo);
}
