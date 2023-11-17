package com.kangui.talentsharehub.domain.course.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.course.entity.Syllabus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "course-id에 해당 하는 강의 계획서 조회 응답")
@Getter
@RequiredArgsConstructor
public class ResponseSyllabusByCourseId {

    @Schema(description = "강의 계획서 번호")
    private final Long id;

    @Schema(description = "주차")
    private final int week;

    @Schema(description = "강의 내용")
    private final String courseContent;

    public static ResponseSyllabusByCourseId of(final Syllabus syllabus) {
        return new ResponseSyllabusByCourseId(
                syllabus.getId(),
                syllabus.getWeek(),
                syllabus.getCourseContent()
        );
    }
}
