package com.mozza.springboilerplate.config;

import com.mozza.springboilerplate.shared.resolver.QueryParamResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig {
    private final QueryParamResolver argumentResolver;

    public WebConfig(QueryParamResolver argumentResolver) {
        this.argumentResolver = argumentResolver;
    }

    @Bean
    public boolean addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        return argumentResolvers.add(argumentResolver);
    }
}
