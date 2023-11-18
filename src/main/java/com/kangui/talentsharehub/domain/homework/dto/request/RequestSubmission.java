package com.kangui.talentsharehub.domain.homework.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "과제 제출 수정 요청")
@Getter
public class RequestSubmission {

    @NotEmpty
    @Size(max = 100, message = "제출 코멘트는 100자 이하이어야 합니다.")
    @Schema(description = "제출 코멘트", example = "contents")
    private final String contents;

    public RequestSubmission(final String contents) {
        this.contents = contents;
    }
}
