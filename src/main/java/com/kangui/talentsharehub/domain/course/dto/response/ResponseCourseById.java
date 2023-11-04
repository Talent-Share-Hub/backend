package com.kangui.talentsharehub.domain.course.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.enums.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "강의 정보 응답")
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCourseById {

    @Schema(description = "강의 번호")
    private Long id;

    @Schema(description = "선생님 이름")
    private String teacherName;

    @Schema(description = "카테고리")
    private String category;

    @Schema(description = "강의 이미지 url")
    private String image_url;

    @Schema(description = "강의 제목")
    private String title;

    @Schema(description = "강의 설명")
    private String description;

    @Schema(description = "참고 자료")
    private String reference;

    @Schema(description = "강의 링크")
    private String link;

    @Schema(description = "연락처")
    private String contact;

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

    public ResponseCourseById(Course course) {
        this.id = course.getId();
        this.teacherName = course.getUser().getUserProfile().getName();
        this.category = course.getCategory().getName();
        this.image_url = course.getImage_url();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.reference = course.getReference();
        this.link = course.getLink();
        this.contact = course.getContact();
        this.capacity = course.getCapacity();
        this.enrolledStudents = course.getEnrolledStudents();
        this.courseStatus = course.getCourseStatus();
        this.startDate = course.getDateRange().getStartDate();
        this.endDate =course.getDateRange().getEndDate();
    }
}
