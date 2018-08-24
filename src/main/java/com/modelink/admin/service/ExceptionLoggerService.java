package com.modelink.admin.service;

import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.reservation.vo.ExceptionLoggerParamPagerVo;

import java.util.List;

public interface ExceptionLoggerService {

    /**
     * 插入信息
     * @param exceptionLogger
     * @return
     */
    public boolean save(ExceptionLogger exceptionLogger);

    /**
     * 更新信息
     * @param exceptionLogger
     * @return
     */
    public boolean update(ExceptionLogger exceptionLogger);

    /**
     * 删除信息
     * @param exceptionLogger
     * @return
     */
    public boolean delete(ExceptionLogger exceptionLogger);

    /**
     * 根据指定条件查询信息
     * @param exceptionLogger
     * @return
     */
    public ExceptionLogger findOneByParam(ExceptionLogger exceptionLogger);

    /**
     * 根据指定条件查询信息列表
     * @param exceptionLogger
     * @return
     */
    public List<ExceptionLogger> findListByParam(ExceptionLogger exceptionLogger);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<ExceptionLogger> findPagerByParam(ExceptionLoggerParamPagerVo paramPagerVo);
}
