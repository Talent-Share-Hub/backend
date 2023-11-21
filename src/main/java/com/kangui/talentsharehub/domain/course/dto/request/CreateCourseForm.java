package com.kangui.talentsharehub.domain.course.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Category;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.CourseImageFile;
import com.kangui.talentsharehub.domain.course.entity.embeded.DateRange;
import com.kangui.talentsharehub.domain.course.enums.CourseStatus;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Slf4j
@Schema(description = "강의 생성 요청")
@Getter
public class CreateCourseForm {

    @NotNull
    @Schema(description = "강의 메인 이미지")
    private final MultipartFile courseImage;

    @NotNull
    @Schema(description = "카테고리 ID", example = "1")
    private final Long categoryId;

    @NotBlank
    @Schema(description = "제목", example = "title")
    private final String title;

    @NotBlank
    @Schema(description = "설명", example = "description")
    private final String description;

    @Schema(description = "참고 자료 링크", example = "refrence")
    private final String reference;

    @Schema(description = "강의 링크", example = "link")
    private final String link;

    @Schema(description = "연락처", example = "contact")
    private final String contact;

    @NotNull
    @Positive(message = "수용 인원은 양수여야 합니다.")
    @Schema(description = "수용 인원", example = "10")
    private final int capacity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "시작일", example = "2023-12-15")
    private final LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "종료일", example = "2023-12-16")
    private final LocalDate endDate;

    public CreateCourseForm(
            final MultipartFile courseImage,
            final Long categoryId,
            final String title,
            final String description,
            final String reference,
            final String link,
            final String contact,
            final int capacity,
            final LocalDate startDate,
            final LocalDate endDate) {
        checkDateRange(startDate, endDate);
        this.courseImage = courseImage;
        this.categoryId = categoryId;
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
        if (startDate.isAfter(endDate)) {
            log.info("종료 일이 시작 일보다 빠를 수 없습니다.");
            throw new AppException(ErrorCode.INVALID_DATE_RANGE, "종료 일이 시작 일보다 빠를 수 없습니다.");
        }
    }
}
