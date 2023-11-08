package com.kangui.talentsharehub.domain.homework.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Long courseId;

    @NotBlank
    private String title;

    @NotBlank
    private String contents;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
