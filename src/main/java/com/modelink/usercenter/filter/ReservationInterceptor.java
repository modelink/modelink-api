package com.modelink.usercenter.filter;

import com.alibaba.fastjson.JSON;
import com.modelink.common.enums.RetStatus;
import com.modelink.common.vo.ResultVo;
import com.modelink.usercenter.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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

        PrintWriter out = null ;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        out = httpServletResponse.getWriter();

        // 校验参数是否符合要求
        ResultVo resultVo = new ResultVo();
        if(StringUtils.isEmpty(parameterMap.get("appKey"))){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("参数appKey不能为空");
            out.append(JSON.toJSONString(resultVo));
            return false;
        }
        if(StringUtils.isEmpty(parameterMap.get("sign"))){
            resultVo.setRtnCode(RetStatus.Fail.getValue());
            resultVo.setRtnMsg("参数sign不能为空");
            out.append(JSON.toJSONString(resultVo));
            return false;
        }


        return true;    //如果false，停止流程，api被拦截
    }
}
