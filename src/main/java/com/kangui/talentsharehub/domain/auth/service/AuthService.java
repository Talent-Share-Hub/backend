package com.kangui.talentsharehub.domain.auth.service;

import com.kangui.talentsharehub.domain.auth.dto.request.RequestSignUp;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.entity.embeded.UserProfile;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(RequestSignUp requestSignUp) {
        if (userRepository.findByLoginId(requestSignUp.getLoginId()).isPresent()) {
            throw new AppException(ErrorCode.USER_DUPLICATED, "이미 존재하는 아이디입니다.");
        }

        if (userRepository.findByNickname(requestSignUp.getNickname()).isPresent()) {
            throw new AppException(ErrorCode.NICKNAME_DUPLICATED, "이미 존재하는 닉네임입니다.");
        }

        Users user = Users.builder()
                .loginId(requestSignUp.getLoginId())
                .password(requestSignUp.getPassword())
                .nickname(requestSignUp.getNickname())
                .userProfile(UserProfile.builder()
                        .birthDay(requestSignUp.getBirthDay())
                        .gender(requestSignUp.getGender())
                        .phoneNumber(requestSignUp.getPhoneNumber())
                        .build())
                .build();

        user.encodePassword(passwordEncoder);
        userRepository.save(user);
    }
}
