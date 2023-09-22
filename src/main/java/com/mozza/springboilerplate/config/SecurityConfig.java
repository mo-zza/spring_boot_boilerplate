package com.mozza.springboilerplate.config;

import com.mozza.springboilerplate.auth.jwt.JwtAccessDeniedHandler;
import com.mozza.springboilerplate.auth.jwt.JwtAuthenticationEntryPoint;
import com.mozza.springboilerplate.auth.jwt.JwtSecurityConfig;
import com.mozza.springboilerplate.auth.jwt.JwtTokenProvider;
import com.mozza.springboilerplate.auth.security.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {
    private final JwtTokenProvider tokenProvider;
    private final SecurityFilter securityFilter;

    public SecurityConfig(
            JwtTokenProvider tokenProvider, CorsFilter corsFilter, Environment env,
            JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.securityFilter = new SecurityFilter(env.getProperty("spring.profiles.active"),
                corsFilter, jwtAccessDeniedHandler);
        this.tokenProvider = tokenProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.apply(new JwtSecurityConfig(this.tokenProvider));
        return securityFilter.get(http);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
