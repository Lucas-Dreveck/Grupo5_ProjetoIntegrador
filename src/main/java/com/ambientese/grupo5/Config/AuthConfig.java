package com.ambientese.grupo5.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ambientese.grupo5.Filters.AuthFilter;
import com.ambientese.grupo5.Services.JWTUtil;

@Configuration
public class AuthConfig {
    @Autowired
    private JWTUtil jwtUtil;

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter(jwtUtil));
        registrationBean.addUrlPatterns("/api/auth/*", "/employees", "/empresas", "/start-evaluation", "/evaluation", "/result-evaluation");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
