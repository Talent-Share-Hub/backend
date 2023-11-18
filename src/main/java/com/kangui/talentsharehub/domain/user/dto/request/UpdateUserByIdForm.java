package com.kangui.talentsharehub.domain.user.dto.request;

import com.kangui.talentsharehub.global.valid.annotation.ValidImageFile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "사용자 수정 요청")
@Getter
public class UpdateUserByIdForm {

    @ValidImageFile
    @Schema(description = "프로필 사진")
    private final MultipartFile profileImage;

    @NotBlank
    @Size(min = 3, max = 15, message = "닉네임은 3자 이상 15자 이하이어야 합니다.")
    @Schema(description = "닉네임")
    private final String nickname;

    @NotBlank
    @Size(max = 200, message = "소개는 200자 이하이어야 합니다.")
    @Schema(description = "소개")
    private final String introduction;

    public UpdateUserByIdForm(
            final MultipartFile profileImage,
            final String nickname,
            final String introduction
    ) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
