package com.modelink.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.ExceptionLogger;
import com.modelink.admin.mapper.ExceptionLoggerMapper;
import com.modelink.admin.service.ExceptionLoggerService;
import com.modelink.reservation.bean.Abnormal;
import com.modelink.reservation.vo.ExceptionLoggerParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

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

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<ExceptionLogger> findPagerByParam(ExceptionLoggerParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(ExceptionLogger.class);
        Example.Criteria criteria = example.createCriteria();
        String dateField = paramPagerVo.getDateField();
        if(StringUtils.isEmpty(dateField)){
            dateField = "loggerDate";
        }
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
        }
        if(StringUtils.hasText(paramPagerVo.getLoggerType())){
            criteria.andEqualTo("loggerType", paramPagerVo.getLoggerType());
        }
        example.setOrderByClause("logger_date desc");
        List<ExceptionLogger> abnormalList = exceptionLoggerMapper.selectByExample(example);
        PageInfo<ExceptionLogger> pageInfo = new PageInfo<>(abnormalList);
        return pageInfo;
    }
}
