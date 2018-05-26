package com.modelink.reservation.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebInterceptorConfigurer extends WebMvcConfigurationSupport {

    @Bean
    ReservationInterceptor reservationInterceptor() {
        return new ReservationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(reservationInterceptor()).addPathPatterns("/reservation/**");
        super.addInterceptors(registry);
    }

}
