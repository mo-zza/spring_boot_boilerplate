package com.mozza.springboilerplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(this.getOrigins());
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    private List<String> getOrigins() {
        List<String> origins = new ArrayList<>();
        origins.add("https://numble-client-v2.vercel.app");
        origins.add("https://numble.it");
        origins.add("https://www.numble.it");
        origins.add("https://stage.numble.it");
        origins.add("https://admin.numble.it");
        origins.add("https://admin-dev.numble.it");
        origins.add("http://localhost:3000");
        return origins;
    }
}
