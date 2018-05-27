package com.modelink.admin.interceptor;

import com.modelink.admin.vo.AdminVo;
import com.modelink.common.vo.ResultVo;
import com.modelink.usercenter.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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
