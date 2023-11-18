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
}
