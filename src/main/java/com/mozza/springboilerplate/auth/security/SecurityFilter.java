package com.mozza.springboilerplate.auth.security;

import com.mozza.springboilerplate.auth.jwt.JwtAccessDeniedHandler;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

public class SecurityFilter {

    private final String profile;

    private final CorsFilter corsFilter;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityFilter(String profile, CorsFilter corsFilter, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.profile = profile;
        this.corsFilter = corsFilter;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    public SecurityFilterChain get(HttpSecurity http) throws Exception {
        return switch (profile) {
            case "local" -> local(http);
            default -> prod(http);
        };
    }

    private SecurityFilterChain local(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .headers(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }

    private SecurityFilterChain prod(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .build();
    }
}
