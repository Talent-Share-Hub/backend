package com.kangui.talentsharehub.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "user-id에 해당하는 사용자 정보 응답")
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUserById {
    @Schema(description = "유저 번호", example = "1")
    private Long id;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "생일", example = "2000-01-01")
    private LocalDate birthDay;

    @Schema(description = "휴대전화 번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "성별", example = "MALE")
    private Gender gender;

    @Schema(description = "닉네임", example = "홍길동이올시다")
    private String nickname;

    @Schema(description = "프로필 이미지 URL")
    private String imageUrl;

    @Schema(description = "소개", example = "안녕하세요~ 홍길동입니다.")
    private String introduction;

    @Builder
    public ResponseUserById(Users user) {
        this.id = user.getId();
        this.name = user.getUserProfile().getName();
        this.birthDay = user.getUserProfile().getBirthDay();
        this.phoneNumber = user.getUserProfile().getPhoneNumber();
        this.gender = user.getUserProfile().getGender();
        this.nickname = user.getNickname();
        this.imageUrl = user.getImageUrl();
        this.introduction = user.getIntroduction();
    }
}
