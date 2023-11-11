package com.kangui.talentsharehub.domain.homework.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.homework.dto.AttachmentFile;
import com.kangui.talentsharehub.domain.homework.entity.Submission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "course-id에 해당 하는 과제 제출 응답")
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSubmission {

    @Schema(description = "과제 제출 번호", example = "1")
    private Long submissionId;

    @Schema(description = "제출 코멘트", example = "contents")
    private String contents;

    @Schema(description = "과제 제출 첨부 파일")
    private List<AttachmentFile> attachmentFiles;

    public ResponseSubmission(Submission submission) {
        this.submissionId = submission.getId();
        this.contents = submission.getContents();
        this.attachmentFiles = submission.getSubmissionAttachmentFile()
                .stream()
                .map(AttachmentFile::new)
                .toList();
    }
}
