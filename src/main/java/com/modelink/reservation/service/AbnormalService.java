package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Abnormal;
import com.modelink.reservation.vo.AbnormalParamPagerVo;

import java.util.List;

/**
 * 异常数据服务接口
 */
public interface AbnormalService {

    /**
     * 插入一条记录
     * @param abnormal
     * @return
     */
    public int insert(Abnormal abnormal);

    /**
     * 查询符合条件的记录总数
     * @param abnormal
     * @return
     */
    public int countByParam(Abnormal abnormal);

    /**
     * 查询符合条件的记录
     * @param abnormal
     * @return
     */
    public Abnormal findOneByParam(Abnormal abnormal);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<Abnormal> findListByParam(AbnormalParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<Abnormal> findPagerByParam(AbnormalParamPagerVo paramPagerVo);
}
