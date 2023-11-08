package com.kangui.talentsharehub.domain.homework.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.homework.dto.AttachmentFile;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "course-id에 해당 하는 과제 응답")
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseHomework {

    @Schema(description = "과제 번호")
    private Long homeworkId;

    @Schema(description = "과제 제목")
    private String title;

    @Schema(description = "과제 본문")
    private String contents;

    @Schema(description = "과제 첨부 파일")
    private List<AttachmentFile> attachmentFiles;

    @Schema(description = "시작 시간")
    private LocalDateTime startDate;

    @Schema(description = "마감 시간")
    private LocalDateTime endDate;

    public ResponseHomework(Homework homework) {
        this.homeworkId = homework.getId();
        this.title = homework.getTitle();
        this.contents = homework.getContents();
        this.attachmentFiles = homework.getHomeworkAttachmentFile()
                                .stream()
                                .map(AttachmentFile::new)
                                .toList();
        this.startDate = homework.getStartDate();
        this.endDate = homework.getEndDate();
    }
}
