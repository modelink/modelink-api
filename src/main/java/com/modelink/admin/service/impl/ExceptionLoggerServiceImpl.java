package com.modelink.admin.service.impl;

import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.admin.mapper.ExceptionLoggerMapper;
import com.modelink.admin.service.ExceptionLoggerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExceptionLoggerServiceImpl implements ExceptionLoggerService {

    @Resource
    private ExceptionLoggerMapper exceptionLoggerMapper;
    /**
     * 插入信息
     *
     * @param exceptionLogger
     * @return
     */
    @Override
    public boolean save(ExceptionLogger exceptionLogger) {
        boolean success = false;
        try {
            success = exceptionLoggerMapper.insertSelective(exceptionLogger) > 0;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    /**
     * 更新信息
     *
     * @param exceptionLogger
     * @return
     */
    @Override
    public boolean update(ExceptionLogger exceptionLogger) {
        return exceptionLoggerMapper.updateByPrimaryKeySelective(exceptionLogger) > 0;
    }

    /**
     * 根据指定条件查询信息
     *
     * @param exceptionLogger
     * @return
     */
    @Override
    public ExceptionLogger findOneByParam(ExceptionLogger exceptionLogger) {
        return exceptionLoggerMapper.selectOne(exceptionLogger);
    }

    /**
     * 根据指定条件查询信息列表
     *
     * @param exceptionLogger
     * @return
     */
    @Override
    public List<ExceptionLogger> findListByParam(ExceptionLogger exceptionLogger) {
        return exceptionLoggerMapper.select(exceptionLogger);
    }
}
