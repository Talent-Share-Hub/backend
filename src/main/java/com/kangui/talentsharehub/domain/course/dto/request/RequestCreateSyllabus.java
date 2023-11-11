package com.kangui.talentsharehub.domain.course.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.Syllabus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "강의 계획서 생성 요청")
@Getter
@AllArgsConstructor
public class RequestCreateSyllabus {

    @NotNull
    @Schema(description = "강의 ID", example = "1")
    private Long courseId;

    @NotNull
    @Schema(description = "주차", example = "1")
    private int week;

    @NotBlank
    @Schema(description = "강의 내용", example = "courseContent")
    private String courseContent;

    public Syllabus toEntity(Course course) {
        return Syllabus.builder()
                .course(course)
                .week(week)
                .courseContent(courseContent)
                .build();
    }
}
