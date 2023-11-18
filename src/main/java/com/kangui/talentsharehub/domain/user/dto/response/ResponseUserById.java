package com.kangui.talentsharehub.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Schema(description = "user-id에 해당하는 사용자 정보 응답")
@Getter
@RequiredArgsConstructor
public class ResponseUserById {
    @Schema(description = "유저 번호", example = "1")
    private final Long id;

    @Schema(description = "이름", example = "홍길동")
    private final String name;

    @Schema(description = "생일", example = "2000-01-01")
    private final LocalDate birthDay;

    @Schema(description = "휴대전화 번호", example = "010-1234-5678")
    private final String phoneNumber;

    @Schema(description = "성별", example = "MALE")
    private final Gender gender;

    @Schema(description = "닉네임", example = "홍길동이올시다")
    private final String nickname;

    @Schema(description = "사용자 이미지 파일 URL")
    private final String userImageUrl;

    @Schema(description = "소개", example = "안녕하세요~ 홍길동입니다.")
    private final String introduction;

    public static ResponseUserById of(final Users user) {

        return new ResponseUserById(
                user.getId(),
                user.getUserProfile().getName(),
                user.getUserProfile().getBirthDay(),
                user.getUserProfile().getPhoneNumber(),
                user.getUserProfile().getGender(),
                user.getNickname(),
                user.getUserImageFile().getFileUrl(),
                user.getIntroduction()
        );
    }
}
