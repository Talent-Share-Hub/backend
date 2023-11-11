package com.kangui.talentsharehub.domain.homework.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "과제 생성 요청")
@Getter
@AllArgsConstructor
public class CreateHomeworkForm {

    @NotNull
    @Schema(description = "강의 ID", example = "1")
    private Long courseId;

    @NotBlank
    @Size(max = 255, message = "과제 제목은 255자 이하이어야 합니다.")
    @Schema(description = "과제 제목", example = "homeworkTitle")
    private String title;

    @NotBlank
    @Size(max = 1000, message = "과제 본문은 1000자 이하이어야 합니다.")
    @Schema(description = "과제 본문", example = "homeworkContents")
    private String contents;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "과제 시작일", example = "2023-12-15 11:22:24")
    private LocalDateTime startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "올바른 날짜를 입력 하세요.")
    @Schema(description = "과제 종료일", example = "2023-12-17 12:12:12")
    private LocalDateTime endDate;

    private List<MultipartFile> attachmentFiles;

    public Homework toEntity(Course course) {
        return Homework.builder()
                .course(course)
                .title(title)
                .contents(contents)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
