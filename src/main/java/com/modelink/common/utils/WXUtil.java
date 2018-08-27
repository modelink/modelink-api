package com.modelink.common.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class WXUtil {
    /**
     * 获取接口访问凭证
     *
     * @param appid     凭证
     * @param appsecret 密钥
     * @return
     */
    public static String getAccess_token(String appid, String appsecret) {
        //凭证获取(GET)
        String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        // 发起GET请求获取凭证
        String jstoken = HttpUtils.doGet(requestUrl);
        JSONObject jsonObject = JSONObject.parseObject(jstoken);
        String access_token = null;
        if (null != jsonObject) {
            try {
                access_token = jsonObject.getString("access_token");
            } catch (JSONException e) {
                // 获取token失败
            }
        }
        return access_token;
    }

    /**
     * 调用微信JS接口的临时票据
     *
     * @param access_token 接口访问凭证
     * @return
     */
    public static String getJsApiTicket(String access_token) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        String requestUrl = url.replace("ACCESS_TOKEN", access_token);
        // 发起GET请求获取凭证
        String jstoken = HttpUtils.doGet(requestUrl);
        JSONObject jsonObject = JSONObject.parseObject(jstoken);
        String ticket = null;
        if (null != jsonObject) {
            try {
                ticket = jsonObject.getString("ticket");
            } catch (JSONException e) {
                // 获取token失败
                //log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return ticket;
    }
}