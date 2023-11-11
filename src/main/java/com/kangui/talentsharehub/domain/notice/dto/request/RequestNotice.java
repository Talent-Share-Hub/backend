package com.kangui.talentsharehub.domain.notice.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.notice.entity.Notice;
import com.kangui.talentsharehub.domain.user.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "공지 생성 요청")
@Getter
@AllArgsConstructor
public class RequestNotice {

    @NotNull
    @Schema(description = "강의 ID", example = "1")
    private Long courseId;

    @NotNull
    @Schema(description = "선생님 ID", example = "1")
    private Long teacherId;

    @NotBlank
    @Size(max = 255, message = "과제 제목은 255자 이하이어야 합니다.")
    @Schema(description = "공지 제목", example = "noticeTitle")
    private String title;

    @NotBlank
    @Size(max = 1000, message = "과제 본문은 1000자 이하이어야 합니다.")
    @Schema(description = "공지 내용", example = "noticeContents")
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
