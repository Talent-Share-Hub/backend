package com.kangui.talentsharehub.domain.course.dto.request;

import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.valid.annotation.ValidImageFile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Schema(description = "강의 수정 요청")
@Getter
public class UpdateCourseForm {

    @NotNull
    @ValidImageFile
    @Schema(description = "강의 메인 이미지")
    private final MultipartFile courseImage;

    @NotBlank
    @Schema(description = "제목", example = "title")
    private final String title;

    @Schema(description = "설명", example = "description")
    @NotBlank
    private final String description;

    @Schema(description = "참고 자료 링크", example = "reference")
    private final String reference;

    @Schema(description = "강의 링크", example = "link")
    private final String link;

    @Schema(description = "연락처", example = "contact")
    private final String contact;

    @NotNull
    @Positive(message = "수용 인원은 양수여야 합니다.")
    @Schema(description = "수용 인원", example = "10")
    private final int capacity;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "강의 시작일", example = "2023-12-14")
    private final LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "강의 종료일", example = "2023-12-16")
    private final LocalDate endDate;

    public UpdateCourseForm(
            MultipartFile courseImage,
            String title,
            String description,
            String reference,
            String link,
            String contact,
            int capacity,
            LocalDate startDate,
            LocalDate endDate) {
        checkDateRange(startDate, endDate);
        this.courseImage = courseImage;
        this.title = title;
        this.description = description;
        this.reference = reference;
        this.link = link;
        this.contact = contact;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void checkDateRange(final LocalDate startDate, final LocalDate endDate) {
        if (!startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE, "종료 일이 시작 일보다 빠를 수 없습니다.");
        }
    }
}
