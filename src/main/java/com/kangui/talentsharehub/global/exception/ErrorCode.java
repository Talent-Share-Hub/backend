package com.kangui.talentsharehub.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_DUPLICATED(HttpStatus.CONFLICT, ""),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, ""),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    COURSE_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    HOMEWORK_NOT_FOUND(HttpStatus.NOT_FOUND, "");

    private HttpStatus httpStatus;
    private String message;
}
