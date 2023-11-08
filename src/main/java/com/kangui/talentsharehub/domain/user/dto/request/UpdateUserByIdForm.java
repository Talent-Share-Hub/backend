package com.kangui.talentsharehub.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "사용자 수정 요청")
@Getter
public class UpdateUserByIdForm {

    @Schema(description = "프로필 사진")
    private final MultipartFile profileImage;

    @Schema(description = "닉네임")
    @NotBlank
    private final String nickname;

    @Schema(description = "소개")
    @NotBlank
    private final String introduction;

    public UpdateUserByIdForm(MultipartFile profileImage, String nickname, String introduction) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
