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
public class CreateSubmissionForm {

    @NotEmpty
    @Size(max = 100, message = "제출 코멘트는 100자 이하이어야 합니다.")
    @Schema(description = "제출 코멘트", example = "contents")
    private final String contents;

    private final List<MultipartFile> attachmentFiles;

    public CreateSubmissionForm(
            final String contents,
            final List<MultipartFile> attachmentFiles
    ) {
        this.contents = contents;
        this.attachmentFiles = attachmentFiles;
    }
}
