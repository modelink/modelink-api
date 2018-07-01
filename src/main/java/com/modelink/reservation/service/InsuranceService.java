package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.InsuranceParamPagerVo;
import com.modelink.reservation.bean.Insurance;

import java.util.List;

/**
 * 预约服务接口
 */
public interface InsuranceService {

    /**
     * 插入一条承保记录
     * @param insurance
     * @return
     */
    public int insert(Insurance insurance);

    /**
     * 查询符合条件的记录总数
     * @param insurance
     * @return
     */
    public int countByParam(Insurance insurance);

    /**
     * 查询符合条件的记录
     * @param insurance
     * @return
     */
    public Insurance findOneByParam(Insurance insurance);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<Insurance> findListByParam(InsuranceParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<Insurance> findPagerByParam(InsuranceParamPagerVo paramPagerVo);
}
