package com.kangui.talentsharehub.domain.homework.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "과제 생성 요청")
@Getter
public class CreateHomeworkForm {

    @NotBlank
    @Size(max = 255, message = "과제 제목은 255자 이하이어야 합니다.")
    @Schema(description = "과제 제목", example = "homeworkTitle")
    private final String title;

    @NotBlank
    @Size(max = 1000, message = "과제 본문은 1000자 이하이어야 합니다.")
    @Schema(description = "과제 본문", example = "homeworkContents")
    private final String contents;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "과제 시작일", example = "2023-12-15 11:22:24")
    private final LocalDateTime startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "과제 종료일", example = "2023-12-17 12:12:12")
    private final LocalDateTime endDate;

    private final List<MultipartFile> attachmentFiles;

    public CreateHomeworkForm(
            final String title,
            final String contents,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final List<MultipartFile> attachmentFiles
    ) {
        checkDateRange(startDate, endDate);
        this.title = title;
        this.contents = contents;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attachmentFiles = attachmentFiles;
    }

    private void checkDateRange(final LocalDateTime startDate, final LocalDateTime endDate) {
        if (!startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE, "종료 일이 시작 일보다 빠를 수 없습니다.");
        }
    }
}
