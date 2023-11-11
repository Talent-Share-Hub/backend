package com.kangui.talentsharehub.domain.homework.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Student;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import com.kangui.talentsharehub.domain.homework.entity.Submission;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(description = "과제 제출 생성 요청")
@Getter
@AllArgsConstructor
public class CreateSubmissionForm {

    @NotNull
    @Schema(description = "수강생 ID", example = "1")
    private Long studentId;

    @NotNull
    @Schema(description = "과제 ID", example = "1")
    private Long homeworkId;

    @NotEmpty
    @Size(max = 100, message = "제출 코멘트는 100자 이하이어야 합니다.")
    @Schema(description = "제출 코멘트", example = "contents")
    private String contents;

    private List<MultipartFile> attachmentFiles;

    public Submission toEntity(Homework homework, Student student) {
        return Submission.builder()
                .homework(homework)
                .student(student)
                .contents(contents)
                .build();
    }
}
