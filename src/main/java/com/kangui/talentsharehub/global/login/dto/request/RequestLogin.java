package com.kangui.talentsharehub.global.login.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestLogin {

    private final String loginId;

    private final String password;
}
