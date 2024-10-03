package com.ambientese.grupo5.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.beans.factory.annotation.Autowired;
import com.ambientese.grupo5.Filters.AuthFilter;
import com.ambientese.grupo5.Services.JWTUtil;

@Configuration
public class WebConfig {

    @Autowired
    private JWTUtil jwtUtil;

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter(jwtUtil));
        registrationBean.addUrlPatterns("/api/auth/*", "/employees", "/empresas", "/start-form", "/form", "/result-form");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    // For mobile development
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/")
                        .allowedOrigins("http://localhost:58608")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}