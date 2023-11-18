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
public class RequestCreateSyllabus {

    @NotNull
    @Schema(description = "주차", example = "1")
    private final int week;

    @NotBlank
    @Schema(description = "강의 내용", example = "courseContent")
    private final String courseContent;

    public RequestCreateSyllabus(
            final int week,
            final String courseContent
    ) {
        this.week = week;
        this.courseContent = courseContent;
    }
}
