package com.kangui.talentsharehub.domain.homework.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Schema(description = "과제 수정 요청")
@Getter
@AllArgsConstructor
public class RequestUpdateHomework {

    @Schema(description = "과제 제목")
    private String title;

    @Schema(description = "과제 본문")
    private String contents;

    @Schema(description = "시작 시간")
    private LocalDateTime startDate;

    @Schema(description = "마감 시간")
    private LocalDateTime endDate;
}
