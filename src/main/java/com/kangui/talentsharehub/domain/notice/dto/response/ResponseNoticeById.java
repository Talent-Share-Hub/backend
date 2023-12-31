package com.kangui.talentsharehub.domain.notice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.notice.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "공지 조회 응답")
@Getter
@RequiredArgsConstructor
public class ResponseNoticeById {

    @Schema(description = "선생님 이름")
    private final String teacherName;

    @Schema(description = "공지 제목")
    private final String title;

    @Schema(description = "공지 내용")
    private final String contents;

    @Schema(description = "조회수")
    private final int views;

    public static ResponseNoticeById of(final Notice notice) {
        return new ResponseNoticeById(
                notice.getUser().getUserProfile().getName(),
                notice.getTitle(),
                notice.getContents(),
                notice.getViews()
        );
    }
}
