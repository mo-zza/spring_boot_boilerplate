package com.mozza.springboilerplate.config;

import com.mozza.springboilerplate.auth.jwt.JwtTokenProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Getter
    @Value("${config.jwt.secret}")
    private String secret;

    @Getter
    @Value("${config.jwt.access-token-validity-hours}")
    private int accessTokenValidityInHours;

    @Getter
    @Value("${config.jwt.refresh-token-validity-in-hours}")
    private int refreshTokenValidityInHours;

    @Bean(name = "tokenProvider")
    public JwtTokenProvider tokenProvider() {
        return new JwtTokenProvider(this.secret, this.accessTokenValidityInHours);
    }
}
