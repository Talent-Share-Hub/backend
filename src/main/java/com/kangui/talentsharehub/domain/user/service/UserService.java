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
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${file.user}")
    private String userPath;

    private final UserRepository userRepository;
    private final FileStore fileStore;

    @Transactional(readOnly = true)
    public ResponseUserById getUserById(final Long userId) {
        final Users user = userRepository.findByIdWithUserImageFile(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));

        return ResponseUserById.of(user);
    }

    public Long updateUserById(final Principal principal, final UpdateUserByIdForm updateUserByIdForm) {
        Users user = userRepository.findByIdWithUserImageFile(principal.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));

        final UploadFile uploadFile = fileStore.updateFile(
                updateUserByIdForm.getProfileImage(),
                user.getUserImageFile().getStoreFileName(),
                userPath);

        user.updateUser(updateUserByIdForm);
        user.getUserImageFile().updateImageFile(
                uploadFile.getUploadFileName(),
                uploadFile.getStoreFileName(),
                uploadFile.getFileUrl());

        return user.getId();
    }

    public void deleteUserById(final Principal principal) {
        Users user = userRepository.findByIdWithUserImageFile(principal.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));

        fileStore.deleteFile(user.getUserImageFile().getStoreFileName(), userPath);

        userRepository.delete(user);
    }
}
