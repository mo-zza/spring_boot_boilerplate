package com.mozza.springboilerplate.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenResponse {
    private String accessToken;

    private String refreshToken;

    public static TokenResponse from(String accessToken, String refreshToken) {
        return new TokenResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }
}
