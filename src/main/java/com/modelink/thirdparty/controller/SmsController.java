package com.modelink.thirdparty.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.bean.Sms;
import com.modelink.admin.service.SmsService;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.vo.ResultVo;
import com.modelink.thirdparty.bean.SmsParamVo;
import com.modelink.thirdparty.constants.SmsContant;
import com.modelink.thirdparty.service.AliyunSmsService;
import com.modelink.thirdparty.service.RedisService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Random;

@Controller
@RequestMapping("/sms")
public class SmsController {

    private static final String Prefix = "sms_";
    @Resource
    private SmsService smsService;
    @Resource
    private RedisService redisService;
    @Resource
    private AliyunSmsService aliyunSmsService;

    /**
     * 发送短信验证码
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendCaptcha")
    public ResultVo sendCaptcha(String mobile){
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
        return sendSms(smsParamVo);
    }

    /**
     * 校验手机验证码是否正确
     * @param mobile
     * @param captcha
     * @return
     */
    @ResponseBody
    @RequestMapping("/validateCaptcha")
    public ResultVo validateCaptcha(String mobile, String captcha){
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
     * 发送短信
     * @param smsParamVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendSms")
    public ResultVo sendSms(SmsParamVo smsParamVo){
        Sms sms = new Sms();
        BeanUtils.copyProperties(smsParamVo, sms);
        sms.setStatus(Sms.STATUS_INIT);
        smsService.save(sms);

        ResultVo resultVo = aliyunSmsService.sendSms(smsParamVo);
        sms.setStatus(resultVo.getRtnCode());
        sms.setResult(resultVo.getRtnMsg());
        smsService.update(sms);
        return resultVo;
    }

    private String formSmsCaptcha(){
        int smsCaptcha = new Random().nextInt(999999);
        return new DecimalFormat("000000").format(smsCaptcha);
    }
}
