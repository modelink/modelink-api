package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.Repellent;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.vo.RepellentParamPagerVo;

import java.util.List;
import java.util.Set;

/**
 * 异常数据服务接口
 */
public interface RepellentService {

    /**
     * 插入一条记录
     * @param repellent
     * @return
     */
    public int insert(Repellent repellent);

    /**
     * 更新一条记录
     * @param repellent
     * @return
     */
    public int update(Repellent repellent);

    /**
     * 查询符合条件的记录总数
     * @param repellent
     * @return
     */
    public int countByParam(Repellent repellent);

    /**
     * 查询符合条件的记录
     * @param repellent
     * @return
     */
    public Repellent findOneByParam(Repellent repellent);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<Repellent> findListByParam(RepellentParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<Repellent> findPagerByParam(RepellentParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的列表
     * @param insuranceNoList
     * @return
     */
    public List<Repellent> findListByInsuranceNoList(Set<String> insuranceNoList);
}
