package com.mozza.springboilerplate.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // TODO: 조금 더 명확한 에러를 보낼 수 있도록 하드코딩 말고
        String message = """
                {
                    "status": 401,
                    "divisionCode": "G007",
                    "message": "Unauthorized",
                    "errors": null,
                    "reason": "401 권한이 없습니다."
                }
                """;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
        response.getWriter().flush();
        response.getWriter().close();
    }
}

