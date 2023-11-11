package com.kangui.talentsharehub.domain.course.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.course.entity.Syllabus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "course-id에 해당 하는 강의 계획서 조회 응답")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSyllabusByCourseId {

    @Schema(description = "강의 계획서 번호")
    private Long id;

    @Schema(description = "주차")
    private int week;

    @Schema(description = "강의 내용")
    private String courseContent;

    public ResponseSyllabusByCourseId(Syllabus syllabus) {
        this.id = syllabus.getId();
        this.week = syllabus.getWeek();
        this.courseContent = syllabus.getCourseContent();
    }
}
