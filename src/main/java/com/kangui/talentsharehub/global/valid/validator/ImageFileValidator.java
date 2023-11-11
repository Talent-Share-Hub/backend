package com.kangui.talentsharehub.global.valid.validator;


import com.kangui.talentsharehub.global.valid.annotation.ValidImageFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator implements ConstraintValidator<ValidImageFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // 빈 파일은 검증 통과
        }

        // MIME 타입 확인
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return false; // 이미지 타입이 아닌 경우 검증 실패
        }

        // 파일 확장자 확인
        String originalFilename = file.getOriginalFilename();

        if (originalFilename != null && !originalFilename.toLowerCase().matches(".+\\.(jpg|jpeg|png)$")) {
            return false; // 허용된 확장자가 아닌 경우 검증 실패
        }

        return true; // 검증 통과
    }
}

