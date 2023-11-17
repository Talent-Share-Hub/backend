package com.kangui.talentsharehub.global.login.resolver;

import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.jwt.service.JwtService;
import com.kangui.talentsharehub.global.login.resolver.annotation.AuthPrincipal;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthPrincipalAnnotation = parameter.hasParameterAnnotation(AuthPrincipal.class);
        boolean hasPrincipalType = Principal.class.isAssignableFrom(parameter.getParameterType());
        return hasAuthPrincipalAnnotation && hasPrincipalType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String accessToken = jwtService.extractAccessToken((HttpServletRequest) webRequest)
                .orElseThrow(() -> new AppException(ErrorCode.NO_HAVE_AUTHORIZATION_HEADER, "인증 헤더가 존재 하지 않습니다."));
        final Long userId = Long.valueOf(jwtService.extractUserId(accessToken)
                .orElseThrow(() -> new AppException(ErrorCode.FAIL_AUTHENTICATION,
                                                        "인증에 실패 했습니다. (JWT AccessToken Payload UserId 누락")));

        return new Principal(userId);
    }
}
