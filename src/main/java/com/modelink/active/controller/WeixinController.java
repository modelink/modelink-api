package com.modelink.active.controller;

import com.modelink.common.utils.PastUtil;
import com.modelink.thirdparty.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/weixin")
public class WeixinController {

    private static Logger logger = LoggerFactory.getLogger(WeixinController.class);

    @Value("${weixin.pn.appId}")
    private String appId;
    @Value("${weixin.pn.appSecret}")
    private String appSecret;
    @Value("${environment.debug}")
    private boolean debug;

    @Resource
    private RedisService redisService;

    @ResponseBody
    @RequestMapping(value = "/getSignature")
    public Map<String, String> getSignature(@RequestParam(value = "url", required = true) String url) {
        Map<String, String> rtnMap = null;
        try {
            if (debug) {
                rtnMap = new HashMap<>();
            } else {
                rtnMap = PastUtil.getParam(redisService, appId, appSecret, url);
            }
            logger.info("[weixinController|getSignature]返回结果。rtnMap={}", rtnMap);
        } catch (Exception e) {
            logger.error("[weixinController|getSignature]发生异常", e);
        }
        return rtnMap;
    }
}
