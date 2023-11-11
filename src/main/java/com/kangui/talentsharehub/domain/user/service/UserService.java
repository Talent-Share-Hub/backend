package com.kangui.talentsharehub.domain.user.service;

import com.kangui.talentsharehub.domain.auth.dto.request.SignUpForm;
import com.kangui.talentsharehub.domain.user.dto.request.UpdateUserByIdForm;
import com.kangui.talentsharehub.domain.user.dto.response.ResponseUserById;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.repository.UserImageFileRepository;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.file.FileStore;
import com.kangui.talentsharehub.global.file.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;

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
        Users user = userRepository.findByIdWithUserImageFile(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));

        return new ResponseUserById(user);
    }

    @Transactional
    public Long updateUserById(Long userId, UpdateUserByIdForm updateUserByIdForm) {
        Users user = userRepository.findByIdWithUserImageFile(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));

        UploadFile uploadFile = null;

        try {
            fileStore.deleteFile(user.getUserImageFile().getStoreFileName(), userPath);
            uploadFile = fileStore.storeFile(updateUserByIdForm.getProfileImage(), userPath);
        } catch (IOException e) {
            log.error("Failed to update profile image for user with ID: {}", userId, e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "사용자 프로필 이미지 업로드에 실패 했습니다.");
        }

        user.updateUser(updateUserByIdForm);
        user.getUserImageFile().updateImageFile(uploadFile.getUploadFileName(), uploadFile.getStoreFileName(), uploadFile.getFileUrl());

        return userId;
    }

    @Transactional
    public void deleteUserById(Long userId) {
        Users user = userRepository.findByIdWithUserImageFile(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));

        try {
            fileStore.deleteFile(user.getUserImageFile().getStoreFileName(), userPath);
        } catch (MalformedURLException e) {
            log.error("사용자 이미지 삭제에 실패 했습니다. userId: {}", userId, e);
            throw new AppException(ErrorCode.FILE_DELETE_FAILED, "사용자 프로필 이미지 삭제에 실패 했습니다.");
        }

        userRepository.delete(user);
    }
}
