package com.modelink.common.utils;

import com.modelink.thirdparty.service.RedisService;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PastUtil {
    public static String time = null;
    public static String jsapi_ticket = null;

    /**
     *
     * @param appId 账号appId
     * @param appSecret  账号appSecret
     * @param url 当前网页的URL，不包含#及其后面部分
     * @return
     */
    public static Map<String, String> getParam(RedisService redisService, String appId, String appSecret, String url) throws Exception {
        String key = "WX_TICKET";

        jsapi_ticket = redisService.getString(key);

        if (jsapi_ticket == null || jsapi_ticket.equals("")) {
            String access_token = WXUtil.getAccess_token(appId, appSecret);
            jsapi_ticket = WXUtil.getJsApiTicket(access_token);
            redisService.setString(key, jsapi_ticket, 3600); // 缓存1小时
        }

        Map<String, String> params = sign(jsapi_ticket, url);
        params.put("appid", appId);


        return params;
    }

    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String str;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        str = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    //获取当前系统时间 用来判断access_token是否过期
    public static String getTime(){
        Date dt=new Date();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dt);
    }

}
