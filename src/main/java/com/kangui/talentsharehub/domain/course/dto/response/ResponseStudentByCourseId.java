package com.kangui.talentsharehub.domain.course.dto.response;

import com.kangui.talentsharehub.domain.course.entity.Student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "course-id에 해당하는 강의 수강생 조회 응답")
@Getter
@RequiredArgsConstructor
public class ResponseStudentByCourseId {

    @Schema(description = "수강생 번호")
    private final Long studentId;

    @Schema(description = "수강생 이름")
    private final String userName;

    public static ResponseStudentByCourseId of(final Student student) {
        return new ResponseStudentByCourseId(
                student.getId(),
                student.getUser().getUserProfile().getName()
        );
    }
}
