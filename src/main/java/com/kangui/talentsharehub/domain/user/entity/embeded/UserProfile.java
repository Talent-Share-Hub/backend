package com.kangui.talentsharehub.domain.user.entity.embeded;

import com.kangui.talentsharehub.domain.user.enums.Gender;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor
public class UserProfile {
    private String name; // 이름

    @Past(message = "생년월일은 현재 날짜보다 이전이어야 합니다.")
    private LocalDate birthDay; // 생년월일 (2000-01-01)

    private String phoneNumber; // 연락처

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별

    @Builder
    public UserProfile(String name, LocalDate birthDay, String phoneNumber, Gender gender) {
        this.name = name;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }
}
