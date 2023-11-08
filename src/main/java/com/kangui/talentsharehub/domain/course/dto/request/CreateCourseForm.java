package com.kangui.talentsharehub.domain.course.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Category;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.CourseImageFile;
import com.kangui.talentsharehub.domain.course.entity.embeded.DateRange;
import com.kangui.talentsharehub.domain.course.enums.CourseStatus;
import com.kangui.talentsharehub.domain.user.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Schema(description = "강의 생성 요청")
@Getter
@AllArgsConstructor
public class CreateCourseForm {

    @Schema(description = "선생님 ID", example = "1")
    @NotNull
    private Long teacherId;

    @Schema(description = "카테고리 ID", example = "1")
    @NotNull
    private Long categoryId;

    @Schema(description = "강의 메인 이미지")
    private MultipartFile courseImage;

    @Schema(description = "제목")
    @NotBlank
    private String title;

    @Schema(description = "설명")
    @NotBlank
    private String description;

    @Schema(description = "참고 자료 링크")
    private String reference;

    @Schema(description = "강의 링크")
    private String link;

    @Schema(description = "연락처")
    private String contact;

    @Schema(description = "수용 인원")
    @NotNull
    private int capacity;

    @Schema(description = "시작일")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate startDate;

    @Schema(description = "종료일")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate endDate;

    public Course toEntity(Users teacher, Category category, CourseImageFile courseImageFile) {
        return Course.builder()
                .user(teacher)
                .category(category)
                .courseImageFile(courseImageFile)
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
