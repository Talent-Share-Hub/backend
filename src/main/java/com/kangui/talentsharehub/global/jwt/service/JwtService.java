package com.kangui.talentsharehub.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey.access}")
    private String accessSecretKey;

    @Value("${jwt.secretKey.refresh}")
    private String refreshSecretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USER_ID_CLAIM = "userId";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(Long userId) {
        Date now = new Date();
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정

                // 클레임으로는 loginId를 사용한다.
                // 추가적으로 식별자나, 이름 등의 정보를 더 추가해도 된다.
                // 추가할 경우 .withClaim(클레임 이름, 클레임 값)으로 설정해주면 된다.
                .withClaim(USER_ID_CLAIM, userId)
                .sign(Algorithm.HMAC512(accessSecretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 accessSecret 키로 암호화
    }

    /**
     * RefreshToken 생성
     * RefreshToken은 Claim에 loginId를 넣지 않으므로 withClaim() X
     */
    public String createRefreshToken() {
        Date now = new Date();

        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(refreshSecretKey));
    }

    /**
     * AccessToken 헤더에 실어서 보내기
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급 된 Access Token : {}", accessToken);
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    /**
     * 헤더에서 RefreshToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer "를 삭제(""로 replace)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 loginId 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 로그인 ID 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<Long> extractUserId(String accessToken) {
        try {
            log.info("extractUserId {}", accessToken);

            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(accessSecretKey))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim(USER_ID_CLAIM) // claim(userID) 가져오기
                    .asLong());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    /**
     * RefreshToken DB 저장(업데이트)
     */
    public void updateRefreshToken(String loginId, String refreshToken) {
        Users user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));

        user.updateRefreshToken(refreshToken);
    }

    /**
     * AccessToken 유효성 검사
     */
    public boolean isAccessTokenValid(String accessToken) {
        try {
            log.info(accessToken);
            JWT.require(Algorithm.HMAC512(accessSecretKey)).build().verify(accessToken);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    /**
     * RefreshToken 유효성 검사
     */
    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            JWT.require(Algorithm.HMAC512(refreshSecretKey)).build().verify(refreshToken);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
}
