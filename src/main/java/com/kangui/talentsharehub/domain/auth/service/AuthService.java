package com.kangui.talentsharehub.domain.auth.service;

import com.kangui.talentsharehub.domain.auth.dto.request.RequestAddInfo;
import com.kangui.talentsharehub.domain.auth.dto.request.SignUpForm;
import com.kangui.talentsharehub.domain.user.entity.UserImageFile;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.entity.embeded.UserProfile;
import com.kangui.talentsharehub.domain.user.enums.Role;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.global.file.FileStore;
import com.kangui.talentsharehub.global.file.UploadFile;
import com.kangui.talentsharehub.global.jwt.service.JwtService;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Value("${file.user}")
    private String userPath;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStore fileStore;
    private final JwtService jwtService;

    public Long signUp(final SignUpForm signUpForm) {
        if (userRepository.existsByLoginId(signUpForm.getLoginId())) {
            throw new AppException(ErrorCode.USER_DUPLICATED, "이미 존재하는 아이디 입니다.");
        }

        if (userRepository.existsByNickname(signUpForm.getNickname())) {
            throw new AppException(ErrorCode.USER_DUPLICATED, "이미 존재하는 닉네임 입니다.");
        }

        final UploadFile uploadFile = fileStore.storeFile(signUpForm.getProfileImage(), userPath);

        Users user = new Users(
                null,
                signUpForm.getLoginId(),
                signUpForm.getPassword(),
                new UserProfile(
                        signUpForm.getName(),
                        signUpForm.getBirthDay(),
                        signUpForm.getPhoneNumber(),
                        signUpForm.getGender()
                ),
                signUpForm.getNickname(),
                signUpForm.getIntroduction(),
                Role.USER,
                null,
                null,
                null
        );

        UserImageFile userImageFile = uploadFile.toUserImageFile();
        user.changeUserImageFile(userImageFile);
        user.encodePassword(passwordEncoder);

        return userRepository.save(user).getId();
    }

    public void logout(Principal principal) {
        Users user = userRepository.findById(principal.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 사용자 입니다."));

        user.removeRefreshToken();
    }

    public Long addInfo(final Long userId, final RequestAddInfo requestAddInfo) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 사용자 입니다."));

        if(!user.getRole().equals(Role.GUEST)) {
            throw new AppException(ErrorCode.NOT_FIRST_OAUTH2_USER, "OAuth2로 처음 가입하는 회원이 아닙니다.");
        }

        if(userRepository.existsByNickname(requestAddInfo.getNickname())) {
            throw new AppException(ErrorCode.USER_DUPLICATED, "이미 존재하는 닉네임 입니다.");
        }

        user.addInfo(requestAddInfo);

        return user.getId();
    }

    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED, "RefreshToken이 존재하지 않습니다."));

        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN, "유효하지 않은 refreshtoken 입니다.");
        }

        Long userId = jwtService.extractUserIdFromRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.FAIL_AUTHENTICATION,
                        "인증에 실패 했습니다. (JWT RefreshToken Payload UserId 누락"));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 유저가 없습니다."));

        String savedRefreshToken = user.getRefreshToken();

        if(savedRefreshToken.equals(refreshToken)) {
            String reIssuedRefreshToken = jwtService.createRefreshToken(user.getId());
            user.updateRefreshToken(reIssuedRefreshToken);

            jwtService.sendAccessAndRefreshToken(
                    response,
                    jwtService.createAccessToken(user.getId()),
                    reIssuedRefreshToken
            );
        } else {
            /*
             유효한 refresh token임에도 불구하고 현재 User의 RefreshToken과 동일하지 않다면
             해킹 의심으로 기존 RefreshToken 삭제 및 재로그인 요청
             (한 곳에서만 로그인 가능)
             */
            user.removeRefreshToken();
            // 로그인 화면으로 Redirect 필요
        }
    }


}
