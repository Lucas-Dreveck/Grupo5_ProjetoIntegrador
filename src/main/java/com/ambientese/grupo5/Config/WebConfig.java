package com.ambientese.grupo5.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.beans.factory.annotation.Autowired;
import com.ambientese.grupo5.Filters.AuthFilter;
import com.ambientese.grupo5.Services.UsuarioService.JWTUtil;

@Configuration
public class WebConfig {

    @Autowired
    private JWTUtil jwtUtil;

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter(jwtUtil));
        registrationBean.addUrlPatterns("/auth/*", "/funcionarios", "/empresas", "/start-avaliacao", "/avaliacao", "/result-avaliacao");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
