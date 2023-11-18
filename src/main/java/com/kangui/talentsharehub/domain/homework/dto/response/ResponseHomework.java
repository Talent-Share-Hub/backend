package com.kangui.talentsharehub.domain.homework.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kangui.talentsharehub.domain.homework.dto.AttachmentFile;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "course-id에 해당 하는 과제 응답")
@Getter
@RequiredArgsConstructor
public class ResponseHomework {

    @Schema(description = "과제 번호")
    private final Long homeworkId;

    @Schema(description = "과제 제목")
    private final String title;

    @Schema(description = "과제 본문")
    private final String contents;

    @Schema(description = "과제 첨부 파일")
    private final List<AttachmentFile> attachmentFiles;

    @Schema(description = "시작 시간")
    private final LocalDateTime startDate;

    @Schema(description = "마감 시간")
    private final LocalDateTime endDate;

    public static ResponseHomework of(Homework homework) {
        return new ResponseHomework(
                homework.getId(),
                homework.getTitle(),
                homework.getContents(),
                homework.getHomeworkAttachmentFile()
                        .stream()
                        .map(AttachmentFile::of)
                        .toList(),
                homework.getStartDate(),
                homework.getEndDate()
        );
    }
}
