package com.modelink.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.bean.Sms;
import com.modelink.admin.mapper.SmsMapper;
import com.modelink.admin.service.SmsService;
import com.modelink.admin.vo.common.SmsParamPagerVo;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.ValidateUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.thirdparty.bean.SmsParamVo;
import com.modelink.thirdparty.constants.SmsContant;
import com.modelink.thirdparty.service.AliyunSmsService;
import com.modelink.thirdparty.service.RedisService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

@Service
public class SmsServiceImpl implements SmsService {

    public static final String Prefix = "sms_";

    @Resource
    private SmsMapper smsMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private AliyunSmsService aliyunSmsService;
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

    /**
     * 发送验证码
     *
     * @param mobile
     * @return
     */
    @Override
    public ResultVo sendCaptcha(String mobile) {
        ResultVo resultVo = new ResultVo<>();
        String validResult = ValidateUtils.isValidPhone(mobile);
        if (StringUtils.hasText(validResult)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(validResult);
            return resultVo;
        }

        String smsCaptcha = formSmsCaptcha();
        /** 设置验证码5分钟有效 **/
        redisService.setString(Prefix + mobile, smsCaptcha, 300000);
        JSONObject templateValue = new JSONObject();
        templateValue.put("code", smsCaptcha);
        SmsParamVo smsParamVo = new SmsParamVo();
        smsParamVo.setPhoneNumbers(mobile);
        smsParamVo.setTemplateCode(SmsContant.ALIYUN_TEMPLATE_CAPTCHA);
        smsParamVo.setTemplateParam(JSON.toJSONString(templateValue));
        smsParamVo.setSignName(SmsContant.ALIYUN_SIGNNAME);
        resultVo = sendSms(smsParamVo);
        return resultVo;
    }

    /**
     * 校验验证码
     *
     * @param mobile
     * @param captcha
     * @return
     */
    @Override
    public ResultVo validateCaptcha(String mobile, String captcha) {
        ResultVo resultVo = new ResultVo();
        if(StringUtils.isEmpty(mobile)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("手机号码不能为空");
            return resultVo;
        }
        if(StringUtils.isEmpty(captcha)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("手机验证码不能为空");
            return resultVo;
        }
        String redisCaptcha = redisService.getString(Prefix + mobile);
        if(!captcha.equals(redisCaptcha)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("手机验证码不正确");
            return resultVo;
        }
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        return resultVo;
    }

    /**
     * 清除验证码
     *
     * @param mobile
     */
    @Override
    public void clearCaptcha(String mobile) {
        redisService.delString(Prefix + mobile);
    }

    /**
     * 发送短信
     * @param smsParamVo
     * @return
     */
    @Override
    public ResultVo sendSms(SmsParamVo smsParamVo){
        Sms sms = new Sms();
        BeanUtils.copyProperties(smsParamVo, sms);
        sms.setStatus(Sms.STATUS_INIT);
        this.save(sms);

        ResultVo resultVo = aliyunSmsService.sendSms(smsParamVo);
        sms.setStatus(resultVo.getRtnCode());
        sms.setResult(resultVo.getRtnMsg());
        this.update(sms);
        return resultVo;
    }

    private String formSmsCaptcha(){
        int smsCaptcha = new Random().nextInt(999999);
        return new DecimalFormat("000000").format(smsCaptcha);
    }

}
