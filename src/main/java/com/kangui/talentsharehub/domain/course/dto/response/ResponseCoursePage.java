package com.kangui.talentsharehub.domain.course.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.course.enums.CourseStatus;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "강의 정보 응답")
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCoursePage {

    @Schema(description = "강의 번호")
    private Long id;

    @Schema(description = "선생님 이름")
    private String teacherName;

    @Schema(description = "강의 이미지 url")
    private String imageUrl;

    @Schema(description = "강의 제목")
    private String title;

    @Schema(description = "수용 인원")
    private int capacity;

    @Schema(description = "등록 인원")
    private int enrolledStudents;

    @Schema(description = "강의 상태")
    private CourseStatus courseStatus;

    @Schema(description = "강의 시작일")
    private LocalDate startDate;

    @Schema(description = "강의 종료일")
    private LocalDate endDate;

    @QueryProjection
    public ResponseCoursePage(Long id, String teacherName, String imageUrl, String title, int capacity, int enrolledStudents, CourseStatus courseStatus, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.teacherName = teacherName;
        this.imageUrl = imageUrl;
        this.title = title;
        this.capacity = capacity;
        this.enrolledStudents = enrolledStudents;
        this.courseStatus = courseStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
