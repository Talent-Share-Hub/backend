package com.kangui.talentsharehub.domain.course.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Student;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "강의 수강생 생성 요청")
@Getter
@AllArgsConstructor
public class RequestEnrollStudent {

    @NotNull
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @NotNull
    @Schema(description = "강의 ID", example = "1")
    private Long courseId;

}
