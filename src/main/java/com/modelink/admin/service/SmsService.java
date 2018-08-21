package com.modelink.admin.service;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.Sms;
import com.modelink.admin.vo.SmsParamPagerVo;

import java.util.List;

public interface SmsService {

    /**
     * 插入信息
     * @param sms
     * @return
     */
    public boolean save(Sms sms);

    /**
     * 更新信息
     * @param sms
     * @return
     */
    public boolean update(Sms sms);

    /**
     * 根据指定条件查询信息
     * @param sms
     * @return
     */
    public Sms findOneByParam(Sms sms);

    /**
     * 根据指定条件查询信息列表
     * @param sms
     * @return
     */
    public List<Sms> findListByParam(Sms sms);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<Sms> findPagerByParam(SmsParamPagerVo paramPagerVo);
}
