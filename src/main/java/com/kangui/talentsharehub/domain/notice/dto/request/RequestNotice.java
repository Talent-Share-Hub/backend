package com.kangui.talentsharehub.domain.notice.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.notice.entity.Notice;
import com.kangui.talentsharehub.domain.user.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "공지 생성 요청")
@Getter
@AllArgsConstructor
public class RequestNotice {

    @Schema(description = "강의 ID")
    private Long courseId;

    @Schema(description = "선생님 ID")
    private Long teacherId;

    @Schema(description = "공지 제목")
    private String title;

    @Schema(description = "공지 내용")
    private String contents;

    public Notice toEntity(Course course, Users user) {
        return Notice.builder()
                .user(user)
                .course(course)
                .title(title)
                .contents(contents)
                .views(0)
                .build();
    }
}