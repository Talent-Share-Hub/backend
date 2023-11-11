package com.kangui.talentsharehub.domain.homework.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "과제 수정 요청")
@Getter
@AllArgsConstructor
public class RequestUpdateHomework {

    @NotBlank
    @Size(max = 255, message = "과제 제목은 255자 이하이어야 합니다.")
    @Schema(description = "과제 제목", example = "title")
    private String title;

    @NotBlank
    @Size(max = 1000, message = "과제 본문은 1000자 이하이어야 합니다.")
    @Schema(description = "과제 본문", example = "contents")
    private String contents;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "시작 시간", example = "2023-12-15 18:55:11")
    private LocalDateTime startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "마감 시간", example = "2023-12-16 18:55:11")
    private LocalDateTime endDate;
}
