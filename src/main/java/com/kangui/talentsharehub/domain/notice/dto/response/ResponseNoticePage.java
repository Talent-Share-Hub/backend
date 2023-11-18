package com.kangui.talentsharehub.domain.notice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "공지 응답")
@Getter
public class ResponseNoticePage {

    @Schema(description = "공지 번호")
    private final Long id;

    @Schema(description = "선생님 이름")
    private final String teacherName;

    @Schema(description = "공지 제목")
    private final String title;

    @Schema(description = "공지 조회수")
    private final int views;

    @QueryProjection
    public ResponseNoticePage(final Long id, final String teacherName, final String title, final int views) {
        this.id = id;
        this.teacherName = teacherName;
        this.title = title;
        this.views = views;
    }
}
