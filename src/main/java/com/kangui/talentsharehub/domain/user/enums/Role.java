package com.kangui.talentsharehub.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST"),
    USER("USER");

    private final String key;
}
