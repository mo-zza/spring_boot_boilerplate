package com.mozza.springboilerplate.shared.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozza.springboilerplate.shared.annotation.QueryParam;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class QueryParamResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper mapper;


    public QueryParamResolver(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean supportsParameter(final MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(QueryParam.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter methodParameter,
                                  final ModelAndViewContainer modelAndViewContainer,
                                  final NativeWebRequest nativeWebRequest,
                                  final WebDataBinderFactory webDataBinderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        final String json = qs2json(request.getQueryString());
        return mapper.readValue(json, methodParameter.getParameterType());
    }

    private String qs2json(String a) {
        String res = "{\"";

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == '=') {
                res += "\"" + ":" + "\"";
            } else if (a.charAt(i) == '&') {
                res += "\"" + "," + "\"";
            } else {
                res += a.charAt(i);
            }
        }
        res += "\"" + "}";
        return res;
    }
}
