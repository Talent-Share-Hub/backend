package com.kangui.talentsharehub.domain.user.entity;

import com.kangui.talentsharehub.domain.user.dto.request.RequestUpdateUserById;
import com.kangui.talentsharehub.domain.user.entity.embeded.UserProfile;
import com.kangui.talentsharehub.domain.user.enums.Role;
import com.kangui.talentsharehub.domain.user.enums.SocialType;
import com.kangui.talentsharehub.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends TimeStampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String loginId; // 로그인 아이디

    private String password; // 비밀번호

    @Embedded
    private UserProfile userProfile; // 유저 정보(이름, 생년월일, 연락처, 성별)

    private String nickname; // 닉네임

    private String imageUrl; // 프로필 이미지

    private String introduction; // 소개

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // 소셜 타입 (KAKAO, GOOGLE)

    @Column(nullable = false)
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인의 경우 null)

    private String refreshToken; // 리프레시 토큰

    @Builder
    public Users(Long id, String loginId, String password, UserProfile userProfile, String nickname, String imageUrl, String introduction, Role role, SocialType socialType, String socialId, String refreshToken) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.userProfile = userProfile;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.introduction = introduction;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
    }

    // 유저 권한 설정 메서드
    public void authorizeUser() {
        this.role = Role.USER;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void updateUser(RequestUpdateUserById requestUpdateUserById, String profileImageUrl) {
        if (StringUtils.hasText(requestUpdateUserById.getNickname())) {
            this.nickname = requestUpdateUserById.getNickname();
        }

        if (StringUtils.hasText(requestUpdateUserById.getIntroduction())) {
            this.introduction = requestUpdateUserById.getIntroduction();
        }

        if (StringUtils.hasText(profileImageUrl)) {
            this.imageUrl= profileImageUrl;
        }
    }
}
