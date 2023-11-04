package com.kangui.talentsharehub.domain.user.dto.request;

import com.kangui.talentsharehub.domain.user.enums.Gender;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
public class RequestUpdateUserById {

    private final MultipartFile profileImage;
    private final String nickname;
    private final String introduction;

    public RequestUpdateUserById(MultipartFile profileImage, String nickname, String introduction) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
