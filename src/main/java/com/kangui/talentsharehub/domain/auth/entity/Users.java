package com.kangui.talentsharehub.domain.auth.entity;

import com.kangui.talentsharehub.domain.auth.enums.Gender;
import com.kangui.talentsharehub.domain.auth.enums.Role;
import com.kangui.talentsharehub.domain.auth.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId; // 로그인 아이디

    private String password; // 비밀번호

    private String name; // 이름

    private LocalDate birthDay; // 생년월일 (2000-01-01)

    private String phoneNumber; // 연락처

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별

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

    // 유저 권한 설정 메서드
    public void authorizeUser() {
        this.role = Role.USER;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
