package com.modelink.admin.interceptor;

import com.modelink.admin.vo.AdminVo;
import com.modelink.usercenter.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.modelink.admin.constants.AdminConstant.ADMIN_SESSION_NAME;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);

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
        logger.info("[adminInterceptor|preHandle]获取请求参数。parameterMap={}", parameterMap);

        // 校验用户是否登录
        AdminVo adminVo = (AdminVo)httpServletRequest.getSession().getAttribute(ADMIN_SESSION_NAME);
        if(adminVo == null){
            httpServletResponse.sendRedirect("/admin/login");
            return false;
        }



        return true;
    }

}