package com.kangui.talentsharehub.domain.homework.dto;

import com.kangui.talentsharehub.domain.homework.entity.HomeworkAttachmentFile;
import com.kangui.talentsharehub.domain.homework.entity.SubmissionAttachmentFile;
import lombok.Getter;

@Getter
public class AttachmentFile {

    private final Long id;

    private final String uploadFileName;

    private final String fileUrl;

    public AttachmentFile(HomeworkAttachmentFile homeworkAttachmentFile) {
        this.id = homeworkAttachmentFile.getId();
        this.uploadFileName = homeworkAttachmentFile.getUploadFileName();
        this.fileUrl = homeworkAttachmentFile.getFileUrl();
    }

    public AttachmentFile(SubmissionAttachmentFile submissionAttachmentFile) {
        this.id = submissionAttachmentFile.getId();
        this.uploadFileName = submissionAttachmentFile.getUploadFileName();
        this.fileUrl = submissionAttachmentFile.getFileUrl();
    }
}
