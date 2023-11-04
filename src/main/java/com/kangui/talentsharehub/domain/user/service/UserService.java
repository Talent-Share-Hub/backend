package com.kangui.talentsharehub.domain.user.service;

import com.kangui.talentsharehub.domain.user.dto.request.RequestUpdateUserById;
import com.kangui.talentsharehub.domain.user.dto.response.ResponseUserById;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${file.user}")
    private String userPath;

    private final UserRepository userRepository;
    private final FileStore fileStore;

    public ResponseUserById getUserById(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));

        return ResponseUserById.builder().user(user).build();
    }

    @Transactional
    public Long updateUserById(Long userId, RequestUpdateUserById requestUpdateUser) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));

        String profileImageUrl = null;

        if(requestUpdateUser.getProfileImage() != null && !requestUpdateUser.getProfileImage().isEmpty()) {
            try {
                fileStore.deleteFile(user.getImageUrl());
                profileImageUrl = fileStore.storeFile(requestUpdateUser.getProfileImage(), userPath);
            } catch (IOException e) {
                log.error("Failed to update profile image for user with ID: {}", userId, e);
            }
        }

        user.updateUser(requestUpdateUser, profileImageUrl);

        return userId;
    }

    @Transactional
    public void deleteUserById(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));

        userRepository.delete(user);
    }
}
