package com.modelink.admin.service;

import com.modelink.admin.bean.ExceptionLogger;

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

}
