package com.modelink.thirdparty.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.vo.ResultVo;
import com.modelink.thirdparty.bean.SmsParamVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AliyunSmsService {

    public static Logger logger = LoggerFactory.getLogger(AliyunSmsService.class);

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;
    @Value("${environment.debug}")
    private boolean debug;

    public ResultVo sendSms(SmsParamVo smsParamVo) {
        JSONObject resultJson = new JSONObject();
        ResultVo resultVo = new ResultVo();
        if(debug){
            resultVo.setRtnCode(RetStatus.Ok.getValue());
            resultVo.setRtnMsg(RetStatus.Ok.getText());
            return resultVo;
        }

        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (Exception e) {
            resultJson.put("code", "modelink.exception");
            resultJson.put("message", "短信发送异常");
            resultVo.setRtnCode(RetStatus.Exception.getValue());
            resultVo.setRtnMsg(resultJson.toJSONString());
            logger.error("[aliyunSmsService|sendSms]发生异常", e);
            return resultVo;
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,
        // 批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；
        // 发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request.setPhoneNumbers(smsParamVo.getPhoneNumbers());
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsParamVo.getSignName());
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(smsParamVo.getTemplateCode());
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的
        // 情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(smsParamVo.getTemplateParam());
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        if(StringUtils.hasText(smsParamVo.getOutId())) {
            request.setOutId(smsParamVo.getOutId());
        }
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (Exception e) {
            sendSmsResponse = null;
        }
        if(sendSmsResponse == null || sendSmsResponse.getCode() == null){
            resultJson.put("code", "modelink.exception");
            resultJson.put("message", "短信发送异常");
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(resultJson.toJSONString());
            return resultVo;
        }
        if(!sendSmsResponse.getCode().equals("OK")){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg(JSON.toJSONString(sendSmsResponse));
            return resultVo;
        }
        //请求成功
        resultVo.setRtnCode(RetStatus.Ok.getValue());
        resultVo.setRtnMsg(RetStatus.Ok.getText());
        return resultVo;
    }
}
