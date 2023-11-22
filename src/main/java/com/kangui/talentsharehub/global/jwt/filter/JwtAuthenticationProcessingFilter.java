package com.kangui.talentsharehub.global.jwt.filter;

import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.jwt.service.JwtService;
import com.kangui.talentsharehub.global.jwt.util.PasswordUtil;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * Jwt 인증 필터
 * "/login, /sign-up" 이외의 URI 요청이 왔을 때 처리하는 필터
 *
 * 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청
 * AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AccessToken과 함께 요청
 *
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다.
 * 2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR
 * 3. RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급(RTR 방식)
 *                              인증 성공 처리는 하지 않고 실패 처리
 *
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        log.info("Path {}", path);

        if (path.startsWith("/login") || path.startsWith("/api/auth") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") || "/".equals(path) || "/favicon.ico".equals(path)) {
            log.info("JWT 인증을 무시 합니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new AppException(ErrorCode.NO_HAVE_AUTHORIZATION_HEADER,
                                                    "해당 API를 사용하기 위해선 인증이 필요합니다."));

        if(!jwtService.isAccessTokenValid(accessToken)) {
            throw new AppException(ErrorCode.INVALID_ACCESS_TOKEN, "유효하지 않은 ACCESS TOKEN입니다.");
        }

        filterChain.doFilter(request, response);
    }
}