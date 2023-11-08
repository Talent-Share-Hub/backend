package com.kangui.talentsharehub.domain.homework.dto;

import com.kangui.talentsharehub.domain.homework.entity.HomeworkAttachmentFile;
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
}
