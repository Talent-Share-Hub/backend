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
public class RequestNotice {

    @NotBlank
    @Size(max = 255, message = "과제 제목은 255자 이하이어야 합니다.")
    @Schema(description = "공지 제목", example = "noticeTitle")
    private final String title;

    @NotBlank
    @Size(max = 1000, message = "과제 본문은 1000자 이하이어야 합니다.")
    @Schema(description = "공지 내용", example = "noticeContents")
    private final String contents;

    public RequestNotice(final String title, final String contents) {
        this.title = title;
        this.contents = contents;
    }
}
