package com.modelink.thirdparty.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.modelink.admin.bean.Sms;
import com.modelink.admin.service.SmsService;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.ValidateUtils;
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
import java.util.regex.Pattern;

@Controller
@RequestMapping("/sms")
public class SmsController {


    @Resource
    private SmsService smsService;

    /**
     * 发送短信验证码
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendCaptcha")
    public ResultVo sendCaptcha(String mobile){
        return smsService.sendCaptcha(mobile);
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
        return smsService.validateCaptcha(mobile, captcha);
    }

    /**
     * 发送短信
     * @param smsParamVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendSms")
    public ResultVo sendSms(SmsParamVo smsParamVo){
        return smsService.sendSms(smsParamVo);
    }

    private String formSmsCaptcha(){
        int smsCaptcha = new Random().nextInt(999999);
        return new DecimalFormat("000000").format(smsCaptcha);
    }
}
