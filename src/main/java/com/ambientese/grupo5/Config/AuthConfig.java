package com.ambientese.grupo5.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ambientese.grupo5.filters.AuthFilter;
import com.ambientese.grupo5.services.JWTUtil;

@Configuration
public class AuthConfig {
    private final JWTUtil jwtUtil;

    public AuthConfig(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter(jwtUtil));
        registrationBean.addUrlPatterns("/api/auth/*", "/employees", "/empresas", "/start-evaluation", "/evaluation", "/result-evaluation");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
