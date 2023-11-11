package com.kangui.talentsharehub.domain.course.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Category;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.CourseImageFile;
import com.kangui.talentsharehub.domain.course.entity.embeded.DateRange;
import com.kangui.talentsharehub.domain.course.enums.CourseStatus;
import com.kangui.talentsharehub.domain.user.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Schema(description = "강의 생성 요청")
@Getter
@AllArgsConstructor
public class CreateCourseForm {

    @NotNull
    @Schema(description = "선생님 ID", example = "1")
    private Long teacherId;

    @NotNull
    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

    @NotNull
    @Schema(description = "강의 메인 이미지")
    private MultipartFile courseImage;

    @NotBlank
    @Schema(description = "제목", example = "title")
    private String title;

    @NotBlank
    @Schema(description = "설명", example = "description")
    private String description;

    @Schema(description = "참고 자료 링크", example = "refrence")
    private String reference;

    @Schema(description = "강의 링크", example = "link")
    private String link;

    @Schema(description = "연락처", example = "contact")
    private String contact;

    @NotNull
    @Positive(message = "수용 인원은 양수여야 합니다.")
    @Schema(description = "수용 인원", example = "10")
    private int capacity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "시작일", example = "2023-12-15")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "종료일", example = "2023-12-16")
    private LocalDate endDate;

    public Course toEntity(Users teacher, Category category) {
        return Course.builder()
                .user(teacher)
                .category(category)
                .title(title)
                .description(description)
                .reference(reference)
                .link(link)
                .contact(contact)
                .capacity(capacity)
                .enrolledStudents(1)
                .courseStatus(CourseStatus.RECRUITING)
                .dateRange(
                        DateRange.builder()
                                .startDate(startDate)
                                .endDate(endDate)
                                .build())
                .build();
    }
}
