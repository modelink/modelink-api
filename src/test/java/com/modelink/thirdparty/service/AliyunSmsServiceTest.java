package com.modelink.thirdparty.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.modelink.common.vo.ResultVo;
import com.modelink.thirdparty.bean.SmsParamVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AliyunSmsServiceTest {

    public static Logger logger = LoggerFactory.getLogger(AliyunSmsServiceTest.class);

    @Resource
    private AliyunSmsService aliyunSmsService;

    @Test
    public void sendSms() {

        JSONObject templateValue = new JSONObject();
        templateValue.put("code", "123456");

        SmsParamVo smsParamVo = new SmsParamVo();
        smsParamVo.setPhoneNumbers("15110100578");
        smsParamVo.setTemplateCode("SMS_140035039");
        smsParamVo.setTemplateParam(JSON.toJSONString(templateValue));
        smsParamVo.setSignName("阿里云短信测试专用");

        ResultVo resultVo = aliyunSmsService.sendSms(smsParamVo);
        logger.info("sms send result : " + JSON.toJSONString(resultVo));
    }
}