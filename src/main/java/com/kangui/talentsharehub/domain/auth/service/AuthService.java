package com.kangui.talentsharehub.domain.auth.service;

import com.kangui.talentsharehub.domain.auth.dto.request.RequestAddInfo;
import com.kangui.talentsharehub.domain.auth.dto.request.SignUpForm;
import com.kangui.talentsharehub.domain.user.entity.UserImageFile;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.enums.Role;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.global.file.FileStore;
import com.kangui.talentsharehub.global.file.UploadFile;
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

    public Long signUp(SignUpForm signUpForm) {
        userRepository.findByLoginId(signUpForm.getLoginId())
                .ifPresent((user) -> {
                    throw new AppException(ErrorCode.USER_DUPLICATED, "이미 존재하는 아이디 입니다.");
                });

        userRepository.findByNickname(signUpForm.getNickname())
                .ifPresent((user) -> {
                    throw new AppException(ErrorCode.USER_DUPLICATED, "이미 존재하는 닉네임 입니다.");
                });

        UploadFile uploadFile = null;

        try {
            uploadFile = fileStore.storeFile(signUpForm.getProfileImage(), userPath);
        } catch (IOException e) {
            log.error("Failed to store profile image", e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "사용자 프로필 이미지 업로드에 실패 했습니다.");
        }

        Users user = signUpForm.toEntity();
        UserImageFile userImageFile = uploadFile.toUserImageFile();
        user.changeUserImageFile(userImageFile);
        user.encodePassword(passwordEncoder);

        return userRepository.save(user).getId();
    }

    public Long addInfo(String loginId, RequestAddInfo requestAddInfo) {
        Users user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 아이디 입니다."));

        if(!user.getRole().equals(Role.GUEST)) {
            throw new AppException(ErrorCode.NOT_FIRST_OAUTH2_USER, "OAuth2로 처음 가입하는 회원이 아닙니다.");
        }

        userRepository.findByNickname(requestAddInfo.getNickname())
                .ifPresent((u) -> {
                    throw new AppException(ErrorCode.USER_DUPLICATED, "이미 존재하는 닉네임 입니다.");
                });

        user.addInfo(requestAddInfo);

        return user.getId();
    }
}
