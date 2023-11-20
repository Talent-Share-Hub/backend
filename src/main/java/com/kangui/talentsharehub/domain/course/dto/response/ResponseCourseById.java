package com.kangui.talentsharehub.domain.course.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.course.entity.Category;
import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.enums.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Schema(description = "강의 정보 응답")
@Getter
@RequiredArgsConstructor
public class ResponseCourseById {

    @Schema(description = "강의 번호")
    private final Long id;

    @Schema(description = "선생님 이름")
    private final String teacherName;

    @Schema(description = "카테고리")
    private final String category;

    @Schema(description = "강의 이미지 파일 url")
    private final String courseImageUrl;

    @Schema(description = "강의 제목")
    private final String title;

    @Schema(description = "강의 설명")
    private final String description;

    @Schema(description = "참고 자료")
    private final String reference;

    @Schema(description = "강의 링크")
    private final String link;

    @Schema(description = "연락처")
    private final String contact;

    @Schema(description = "수용 인원")
    private final int capacity;

    @Schema(description = "등록 인원")
    private final int enrolledStudents;

    @Schema(description = "강의 상태")
    private final CourseStatus courseStatus;

    @Schema(description = "강의 시작일")
    private final LocalDate startDate;

    @Schema(description = "강의 종료일")
    private final LocalDate endDate;

    public static ResponseCourseById of(final Course course) {

        return new ResponseCourseById(
                course.getId(),
                course.getUser().getUserProfile().getName(),
                course.getCategory().getName(),
                course.getCourseImageFile().getFileUrl(),
                course.getTitle(),
                course.getDescription(),
                course.getReference(),
                course.getLink(),
                course.getContact(),
                course.getCapacity(),
                course.getEnrolledStudents(),
                course.getCourseStatus(),
                course.getDateRange().getStartDate(),
                course.getDateRange().getEndDate()
        );
    }
}
