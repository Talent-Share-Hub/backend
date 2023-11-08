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
    HOMEWORK_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    USER_IMAGE_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    COURSE_IMAGE_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    HOMEWORK_ATTACHMENT_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, ""),

    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "");

    private HttpStatus httpStatus;
    private String message;
}
