package com.kangui.talentsharehub.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_DUPLICATED(HttpStatus.CONFLICT, ""),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "");

    private HttpStatus httpStatus;
    private String message;
}
