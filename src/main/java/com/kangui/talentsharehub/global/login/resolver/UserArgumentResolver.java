package com.kangui.talentsharehub.global.login.resolver;

import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.jwt.service.JwtService;
import com.kangui.talentsharehub.global.login.resolver.annotation.AuthPrincipal;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
        boolean hasAuthPrincipalAnnotation = parameter.hasParameterAnnotation(AuthPrincipal.class);
        boolean hasPrincipalType = Principal.class.isAssignableFrom(parameter.getParameterType());
        return hasAuthPrincipalAnnotation && hasPrincipalType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("AuthPrincipal 시작");
        final String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String accessToken = authorization.split(" ")[1];

        final Long userId = jwtService.extractUserIdFromAccessToken(accessToken)
                .orElseThrow(() -> new AppException(ErrorCode.FAIL_AUTHENTICATION,
                                    "인증에 실패 했습니다. (JWT AccessToken Payload UserId 누락"));

        return new Principal(userId);
    }
}
