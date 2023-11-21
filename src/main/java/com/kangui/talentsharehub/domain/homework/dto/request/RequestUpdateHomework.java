package com.kangui.talentsharehub.domain.homework.dto.request;

import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "과제 수정 요청")
@Getter
public class RequestUpdateHomework {

    @NotBlank
    @Size(max = 255, message = "과제 제목은 255자 이하이어야 합니다.")
    @Schema(description = "과제 제목", example = "newTitle")
    private final String title;

    @NotBlank
    @Size(max = 1000, message = "과제 본문은 1000자 이하이어야 합니다.")
    @Schema(description = "과제 본문", example = "newContents")
    private final String contents;

    @NotNull
    @FutureOrPresent(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "시작 시간", example = "2023-12-15T18:55:11", type = "string")
    private final LocalDateTime startDate;

    @NotNull
    @FutureOrPresent(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "마감 시간", example = "2023-12-16T18:55:11", type = "string")
    private final LocalDateTime endDate;

    public RequestUpdateHomework(
            final String title,
            final String contents,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    ) {
        checkDateRange(startDate, endDate);
        this.title = title;
        this.contents = contents;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void checkDateRange(final LocalDateTime startDate, final LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE, "종료 일이 시작 일보다 빠를 수 없습니다.");
        }
    }
}
