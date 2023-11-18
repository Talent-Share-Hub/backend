package com.kangui.talentsharehub.domain.auth.dto.request;

import com.kangui.talentsharehub.domain.user.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "OAuth2 정보 추가 요청")
@Getter
public class RequestAddInfo {

    @NotBlank
    @Schema(description = "이름", example = "홍길동")
    private final String name;

    @NotNull
    @Past(message = "올바른 날짜를 입력하세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "생일", example = "2000-01-01")
    private final LocalDate birthDay;

    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "올바른 연락처를 입력하세요.")
    @Schema(description = "연락처", example = "010-1234-5678")
    private final String phoneNumber;

    @NotNull
    @Schema(description = "성별", example = "MALE")
    private final Gender gender;

    @NotBlank
    @Size(max = 15, message = "닉네임은 15자 이하이어야 합니다.")
    @Schema(description = "닉네임", example = "nickName")
    private final String nickname;

    @Size(max = 200, message = "소개는 200자 이하이어야 합니다.")
    @Schema(description = "소개", example = "introduce")
    private final String introduction;

    public RequestAddInfo(
            final String name,
            final LocalDate birthDay,
            final String phoneNumber,
            final Gender gender,
            final String nickname,
            final String introduction
    ) {
        this.name = name;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
