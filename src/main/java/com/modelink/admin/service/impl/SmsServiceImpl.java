package com.modelink.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.Sms;
import com.modelink.admin.mapper.SmsMapper;
import com.modelink.admin.service.SmsService;
import com.modelink.admin.vo.SmsParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {

    @Resource
    private SmsMapper smsMapper;
    /**
     * 插入信息
     *
     * @param sms
     * @return
     */
    @Override
    public boolean save(Sms sms) {
        boolean success = false;
        try {
            success = smsMapper.insertSelective(sms) > 0;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    /**
     * 更新信息
     *
     * @param sms
     * @return
     */
    @Override
    public boolean update(Sms sms) {
        return smsMapper.updateByPrimaryKeySelective(sms) > 0;
    }

    /**
     * 根据指定条件查询信息
     *
     * @param sms
     * @return
     */
    @Override
    public Sms findOneByParam(Sms sms) {
        return smsMapper.selectOne(sms);
    }

    /**
     * 根据指定条件查询信息列表
     *
     * @param sms
     * @return
     */
    @Override
    public List<Sms> findListByParam(Sms sms) {
        return smsMapper.select(sms);
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Sms> findPagerByParam(SmsParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Sms.class);
        Example.Criteria criteria = example.createCriteria();
        String dateField = paramPagerVo.getDateField();
        if(StringUtils.isEmpty(dateField)){
            dateField = "createTime";
        }
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
        }
        if(StringUtils.hasText(paramPagerVo.getMobile())){
            criteria.andLike("phoneNumbers", paramPagerVo.getMobile());
        }
        example.setOrderByClause("create_time desc");
        List<Sms> abnormalList = smsMapper.selectByExample(example);
        PageInfo<Sms> pageInfo = new PageInfo<>(abnormalList);
        return pageInfo;
    }
}
