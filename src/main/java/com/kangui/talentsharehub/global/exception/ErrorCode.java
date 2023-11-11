package com.kangui.talentsharehub.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    USER_DUPLICATED(HttpStatus.CONFLICT, ""),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
    SYLLABUS_DUPLICATED(HttpStatus.CONFLICT, ""),

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
    SUBMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, ""),

    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ""),

    NOT_FIRST_OAUTH2_USER(HttpStatus.BAD_REQUEST, ""),
    STUDENT_EXCEED(HttpStatus.BAD_REQUEST, "");

    private final HttpStatus httpStatus;
    private final String message;
}
