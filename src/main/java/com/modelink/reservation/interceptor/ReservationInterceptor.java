package com.modelink.reservation.interceptor;

import com.alibaba.fastjson.JSON;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.utils.SignUtils;
import com.modelink.common.vo.ResultVo;
import com.modelink.usercenter.bean.Channel;
import com.modelink.usercenter.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class ReservationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ReservationInterceptor.class);

    @Resource
    private ChannelService channelService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {


        Enumeration<String> enumeration = httpServletRequest.getParameterNames();
        String parameterName, parameterValue;
        Map<String, String> parameterMap = new HashMap<>();
        while (enumeration.hasMoreElements()) {
            parameterName = enumeration.nextElement();
            parameterValue = httpServletRequest.getParameter(parameterName);
            parameterMap.put(parameterName, parameterValue);
        }
        logger.info("[reservationInterceptor|preHandle]获取请求参数。parameterMap={}", parameterMap);

        // 校验参数是否符合要求
        PrintWriter printWriter;
        ResultVo resultVo = new ResultVo();
        try {
            String appKey = parameterMap.get("appKey");
            if (StringUtils.isEmpty(appKey)) {
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("参数appKey不能为空");
                printWriter = formResponseJson(httpServletResponse, resultVo);
                printWriter.append(JSON.toJSONString(resultVo));
                return false;
            }

            String reqSign = parameterMap.get("sign");
            if (StringUtils.isEmpty(reqSign)) {
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("参数sign不能为空");
                printWriter = formResponseJson(httpServletResponse, resultVo);
                printWriter.append(JSON.toJSONString(resultVo));
                return false;
            }
            Channel channel = channelService.findByAppKey(Long.parseLong(appKey));
            if (StringUtils.isEmpty(channel)) {
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("参数appKey不正确");
                printWriter = formResponseJson(httpServletResponse, resultVo);
                printWriter.append(JSON.toJSONString(resultVo));
                return false;
            }
            // 自动增加渠道参数
            httpServletRequest.setAttribute("channel", channel.getId());
            // 校验签名是否正确
            String respSign = SignUtils.generateSignature(parameterMap, channel.getAppSecret());
            logger.info("[reservationInterceptor|preHandle]签名校验开始。reqSign={}, respSign={}", reqSign, respSign);
            if (!reqSign.equalsIgnoreCase(respSign)) {
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("该请求验证签名失败");
                printWriter = formResponseJson(httpServletResponse, resultVo);
                printWriter.append(JSON.toJSONString(resultVo));
                return false;
            }

            String timestamp = parameterMap.get("timestamp");
            if (StringUtils.isEmpty(timestamp)) {
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("参数timestrap不能为空");
                printWriter = formResponseJson(httpServletResponse, resultVo);
                printWriter.append(JSON.toJSONString(resultVo));
                return false;
            }
            // 判断请求是否过期
            if (System.currentTimeMillis() - Long.parseLong(timestamp) > 5 * 60 * 1000) {
                logger.error("[reservationInterceptor|preHandle]参数timestrap疑似遭到篡改");
                resultVo.setRtnCode(RetStatus.Fail.getValue());
                resultVo.setRtnMsg("该请求已过期");
                printWriter = formResponseJson(httpServletResponse, resultVo);
                printWriter.append(JSON.toJSONString(resultVo));
                return false;
            }
        } catch (Exception e) {
            logger.error("[reservationInterceptor|preHandle]发生异常。", e);
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("该请求参数不符合规范");
            printWriter = formResponseJson(httpServletResponse, resultVo);
            printWriter.append(JSON.toJSONString(resultVo));
            return false;
        }

        return true;
    }

    /**
     * 构建响应的JSON响应
     * @param httpServletResponse
     * @param resultVo
     * @return
     * @throws Exception
     */
    private PrintWriter formResponseJson(HttpServletResponse httpServletResponse, ResultVo resultVo) throws Exception {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = httpServletResponse.getWriter();
        return printWriter;
    }

    /**
     * httpServletRequest 中增加请求参数
     * @param httpServletRequest
     * @param parameterMap
     * @throws Exception
     */
    private void rewirteRequestParam(HttpServletRequest httpServletRequest, Map<String, String> parameterMap) throws Exception{
        Map<String, String[]> oldParameterMap  = httpServletRequest.getParameterMap();
        Field lockedField = oldParameterMap.getClass().getDeclaredField("locked");
        lockedField.setAccessible(true);
        lockedField.setBoolean(oldParameterMap, false);//将lock参数设置为false了，就是可以修改了

        // 增加请求参数
        String parameterName;
        String[] parameterValues = {};
        Iterator<String> iterator = parameterMap.keySet().iterator();
        while (iterator.hasNext()){
            parameterName = iterator.next();
            parameterValues = new String[]{ parameterMap.get(parameterName)};
            oldParameterMap.put(parameterName, parameterValues);
        }
        lockedField.setBoolean(oldParameterMap, true);
    }
}
