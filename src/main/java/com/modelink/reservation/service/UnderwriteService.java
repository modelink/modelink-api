package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.dashboard.DashboardParamVo;
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
import com.modelink.reservation.bean.Underwrite;

import java.util.List;
import java.util.Set;

/**
 * 承保数据服务接口
 */
public interface UnderwriteService {

    /**
     * 插入一条承保记录
     * @param underwrite
     * @return
     */
    public int insert(Underwrite underwrite);

    /**
     * 更新一条记录
     * @param underwrite
     * @return
     */
    public int update(Underwrite underwrite);

    /**
     * 查询符合条件的记录总数
     * @param underwrite
     * @return
     */
    public int countByParam(Underwrite underwrite);

    /**
     * 查询符合条件的记录
     * @param underwrite
     * @return
     */
    public Underwrite findOneByParam(Underwrite underwrite);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<Underwrite> findListByParam(UnderwriteParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表
     * @param mobileSet
     * @param sortField
     * @return
     */
    public List<Underwrite> findListByMobiles(Set<String> mobileSet, String sortField);

    /**
     * 查询符合条件的记录列表
     * @param insuranceNoSet
     * @return
     */
    public List<Underwrite> findListByInsuranceNoSet(Set<String> insuranceNoSet, String filteFields);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<Underwrite> findPagerByParam(UnderwriteParamPagerVo paramPagerVo);

    /**
     * 获取指定日期内的数据（只查日期与联系方式两列，节省内存）
     * @param paramVo
     * @return
     */
    public List<Underwrite> findListWithLimitColumnByDateRange(DashboardParamVo paramVo);
}
