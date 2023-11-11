package com.kangui.talentsharehub.domain.notice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.notice.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "공지 조회 응답")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseNoticeById {

    @Schema(description = "선생님 이름")
    private final String teacherName;

    @Schema(description = "공지 제목")
    private final String title;

    @Schema(description = "공지 내용")
    private final String contents;

    @Schema(description = "조회수")
    private final int views;

    public ResponseNoticeById(Notice notice) {
        this.teacherName = notice.getUser().getUserProfile().getName();
        this.title = notice.getTitle();
        this.contents = notice.getContents();
        this.views = notice.getViews();
    }
}
