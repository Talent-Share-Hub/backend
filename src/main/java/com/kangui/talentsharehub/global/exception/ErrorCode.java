package com.kangui.talentsharehub.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 400
    NOT_FIRST_OAUTH2_USER(HttpStatus.BAD_REQUEST, ""),
    STUDENT_EXCEED(HttpStatus.BAD_REQUEST, ""),
    STUDENT_TEACHER_ENROLL(HttpStatus.BAD_REQUEST, ""),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, ""),

    // 401
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, ""),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, ""),
    NO_HAVE_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, ""),
    FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, ""),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ""),

    // 403
    FORBIDDEN(HttpStatus.FORBIDDEN, ""),

    // 404
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
    SUBMISSION_ATTACHMENT_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, ""),

    // 409
    USER_DUPLICATED(HttpStatus.CONFLICT, ""),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
    SYLLABUS_DUPLICATED(HttpStatus.CONFLICT, ""),
    STUDENT_DUPLICATED(HttpStatus.CONFLICT, ""),

    // 500
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "");


    private final HttpStatus httpStatus;
    private final String message;
}
