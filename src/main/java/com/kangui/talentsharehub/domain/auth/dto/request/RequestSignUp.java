package com.kangui.talentsharehub.domain.auth.dto.request;

import com.kangui.talentsharehub.domain.auth.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class RequestSignUp {

    private String loginId;
    private String password;
    private String nickname;
    private LocalDate birthDay;
    private Gender gender;
    private String phoneNumber;

}
