package com.kangui.talentsharehub.domain.notice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "공지 응답")
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseNoticePage {

    @Schema(description = "공지 번호")
    private Long id;

    @Schema(description = "선생님 이름")
    private String teacherName;

    @Schema(description = "공지 제목")
    private String title;

    @Schema(description = "공지 조회수")
    private int views;

    @QueryProjection
    public ResponseNoticePage(Long id, String teacherName, String title, int views) {
        this.id = id;
        this.teacherName = teacherName;
        this.title = title;
        this.views = views;
    }
}
