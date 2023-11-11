package com.kangui.talentsharehub.global.oauth2.handler;

import com.kangui.talentsharehub.domain.user.enums.Role;
import com.kangui.talentsharehub.global.jwt.service.JwtService;
import com.kangui.talentsharehub.global.oauth2.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${oauth2.redirect-url.guest}")
    private String guestRedirectUrl;

    @Value("${oauth2.redirect-url.user}")
    private String userRedirectUrl;

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("OAuth2 Login 성공!");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        if(oAuth2User.getRole() == Role.GUEST) {
            addInfo(response, oAuth2User); // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 추가 정보 입력 페이지로 리다이렉트
        } else {
            loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
        }
    }

    public void addInfo(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String redirectUri = UriComponentsBuilder.fromUriString(guestRedirectUrl)
                .queryParam("loginId", oAuth2User.getLoginId())
                .build()
                .toUriString();

        response.sendRedirect(redirectUri); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getLoginId());
        String refreshToken = jwtService.createRefreshToken();

        String redirectUri = UriComponentsBuilder.fromUriString(userRedirectUrl)
                                .queryParam("accessToken", accessToken)
                                .queryParam("refreshToken", refreshToken)
                                .build()
                                .toUriString();

        response.sendRedirect(redirectUri);
    }
}