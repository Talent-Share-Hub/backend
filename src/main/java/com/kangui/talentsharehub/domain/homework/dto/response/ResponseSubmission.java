package com.kangui.talentsharehub.domain.homework.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.homework.dto.AttachmentFile;
import com.kangui.talentsharehub.domain.homework.entity.Submission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Schema(description = "course-id에 해당 하는 과제 제출 응답")
@Getter
@RequiredArgsConstructor
public class ResponseSubmission {

    @Schema(description = "과제 제출 번호", example = "1")
    private final Long submissionId;

    @Schema(description = "제출 코멘트", example = "contents")
    private final String contents;

    @Schema(description = "과제 제출 첨부 파일")
    private final List<AttachmentFile> attachmentFiles;

    public static ResponseSubmission of(Submission submission) {
        return new ResponseSubmission(
                submission.getId(),
                submission.getContents(),
                submission.getSubmissionAttachmentFile()
                        .stream()
                        .map(AttachmentFile::of)
                        .toList()
        );
    }
}
