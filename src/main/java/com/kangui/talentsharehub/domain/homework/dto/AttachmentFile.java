package com.kangui.talentsharehub.domain.homework.dto;

import com.kangui.talentsharehub.domain.homework.entity.HomeworkAttachmentFile;
import com.kangui.talentsharehub.domain.homework.entity.SubmissionAttachmentFile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AttachmentFile {

    private final Long id;

    private final String uploadFileName;

    private final String fileUrl;

    public static AttachmentFile of(final HomeworkAttachmentFile homeworkAttachmentFile) {
        return new AttachmentFile(
                homeworkAttachmentFile.getId(),
                homeworkAttachmentFile.getUploadFileName(),
                homeworkAttachmentFile.getFileUrl()
        );
    }

    public static AttachmentFile of(final SubmissionAttachmentFile submissionAttachmentFile) {
        return new AttachmentFile(
                submissionAttachmentFile.getId(),
                submissionAttachmentFile.getUploadFileName(),
                submissionAttachmentFile.getFileUrl()
        );
    }
}
