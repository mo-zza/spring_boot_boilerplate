package com.mozza.springboilerplate.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class JwtFilter extends GenericFilterBean {

    private final JwtTokenProvider tokenProvider;

    public JwtFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = resolveToken(httpServletRequest);

        try {
            if (StringUtils.hasText(token) && this.tokenProvider.extractClaims(token) != null) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        } catch (HttpClientErrorException e) {
            SecurityContextHolder.clearContext();

            // TODO: 조금 더 명확한 에러를 보낼 수 있도록 하드코딩 말고
            String message = """
                    {
                        "status": statusCode,
                        "divisionCode": "G007",
                        "message": errorMessage,
                        "errors": null,
                        "reason": errorReason
                    }
                    """
                    .replace("statusCode", String.valueOf(e.getStatusCode().value()))
                    .replace("errorMessage", e.getMessage())
                    .replace("errorReason", e.getMessage());
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(e.getStatusCode().value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(message);
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthConstant.AUTH_HEADER);
        if (StringUtils.hasText(bearerToken) && this.tokenProvider.isMatchedPrefix(bearerToken)) {
            return this.tokenProvider.removeTokenPrefix(bearerToken);
        }
        return null;
    }
}
