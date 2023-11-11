package com.kangui.talentsharehub.domain.course.dto.request;

import com.kangui.talentsharehub.global.valid.annotation.ValidImageFile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Schema(description = "강의 수정 요청")
@Getter
public class UpdateCourseForm {

    @NotNull
    @ValidImageFile
    @Schema(description = "강의 메인 이미지")
    private MultipartFile courseImage;

    @NotBlank
    @Schema(description = "제목", example = "title")
    private String title;

    @Schema(description = "설명", example = "description")
    @NotBlank
    private String description;

    @Schema(description = "참고 자료 링크", example = "reference")
    private String reference;

    @Schema(description = "강의 링크", example = "link")
    private String link;

    @Schema(description = "연락처", example = "contact")
    private String contact;

    @NotNull
    @Positive(message = "수용 인원은 양수여야 합니다.")
    @Schema(description = "수용 인원", example = "10")
    private int capacity;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "강의 시작일", example = "2023-12-14")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "강의 종료일", example = "2023-12-16")
    private LocalDate endDate;

}
