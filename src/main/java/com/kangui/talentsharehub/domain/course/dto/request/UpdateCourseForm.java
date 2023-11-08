package com.kangui.talentsharehub.domain.course.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Schema(description = "강의 수정 요청")
@Getter
public class UpdateCourseForm {

    @Schema(description = "강의 메인 이미지")
    @NotNull
    private MultipartFile courseImage;

    @Schema(description = "제목")
    @NotBlank
    private String title;

    @Schema(description = "설명")
    @NotBlank
    private String description;

    @Schema(description = "참고 자료 링크")
    private String reference;

    @Schema(description = "강의 링크")
    private String link;

    @Schema(description = "연락처")
    private String contact;

    @Schema(description = "수용 인원")
    @NotNull
    private int capacity;

    @Schema(description = "강의 시작일")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate startDate;

    @Schema(description = "강의 종료일")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate endDate;

}
