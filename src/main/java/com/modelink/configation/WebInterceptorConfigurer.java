package com.modelink.configation;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.modelink.admin.interceptor.AdminInterceptor;
import com.modelink.reservation.interceptor.ReservationInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Enumeration;

@Configuration
public class WebInterceptorConfigurer extends WebMvcConfigurationSupport {

    private static Logger logger = LoggerFactory.getLogger(WebInterceptorConfigurer.class);

    @Bean
    AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }
    @Bean
    ReservationInterceptor reservationInterceptor() {
        return new ReservationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(adminInterceptor()).addPathPatterns("/admin/**")
                .excludePathPatterns(
                        "/admin/register",
                        "/admin/login",
                        "/admin/doLogin",
                        "/admin/insurance/importExcel"
                );
        registry.addInterceptor(reservationInterceptor()).addPathPatterns("/reservation/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        ServletContext servletContext = this.getServletContext();
        Enumeration<String> enumeration = servletContext.getInitParameterNames();

        String initParameterName, initParameterValue;
        while (enumeration.hasMoreElements()) {
            initParameterName = enumeration.nextElement();
            initParameterValue = servletContext.getInitParameter(initParameterName);
            logger.info("[webInterceptorConfigurer]获取servlet初始化参数。initParameterName={}, initParameterValue={}",
                    initParameterName, initParameterValue);
        }

        return new ServletRegistrationBean(new KaptchaServlet(),"/common/captcha-image.jpg");
    }

}
