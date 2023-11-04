package com.kangui.talentsharehub.domain.course.dto.response;

import com.kangui.talentsharehub.domain.course.entity.Student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "course-id에 해당하는 강의 수강생 조회 응답")
@Getter
public class ResponseStudentByCourseId {

    @Schema(description = "수강생 번호")
    private Long id;

    @Schema(description = "수강생 이름")
    private String userName;

    public ResponseStudentByCourseId(Student student) {
        this.id = student.getId();
        this.userName = student.getUser().getUserProfile().getName();
    }
}
