package com.modelink.reservation.interceptor;

import com.alibaba.fastjson.JSON;
import com.modelink.common.enums.RetStatus;
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
import java.util.Enumeration;
import java.util.HashMap;
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
        while(enumeration.hasMoreElements()){
            parameterName = enumeration.nextElement();
            parameterValue = httpServletRequest.getParameter(parameterName);
            parameterMap.put(parameterName, parameterValue);
        }
        logger.info("[reservationInterceptor|preHandle]获取请求参数。parameterMap={}", parameterMap);

        // 校验参数是否符合要求
        PrintWriter printWriter;
        ResultVo resultVo = new ResultVo();
        String appKey = parameterMap.get("appKey");
        if(StringUtils.isEmpty(appKey)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("参数appKey不能为空");
            printWriter = formResponseJson(httpServletResponse, resultVo);
            printWriter.append(JSON.toJSONString(resultVo));
            return false;
        }

        String reqSign = parameterMap.get("sign");
        if(StringUtils.isEmpty(reqSign)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("参数sign不能为空");
            printWriter = formResponseJson(httpServletResponse, resultVo);
            printWriter.append(JSON.toJSONString(resultVo));
            return false;
        }

        Channel channel = channelService.findByAppKey(Long.parseLong(appKey));
        if(StringUtils.isEmpty(channel)){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("参数appKey渠道不存在");
            printWriter = formResponseJson(httpServletResponse, resultVo);
            printWriter.append(JSON.toJSONString(resultVo));
            return false;
        }
        // 自动增加渠道参数
        httpServletRequest.setAttribute("channel", channel.getId());
        logger.info("[reservationInterceptor|preHandle]签名校验开始。reqSign={}, respSign={}", reqSign, channel.getAppSecret());


        return true;
    }

    private PrintWriter formResponseJson(HttpServletResponse httpServletResponse, ResultVo resultVo) throws Exception {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = httpServletResponse.getWriter();
        return printWriter;
    }
}
