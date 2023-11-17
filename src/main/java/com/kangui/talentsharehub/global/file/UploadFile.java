package com.kangui.talentsharehub.global.file;

import com.kangui.talentsharehub.domain.course.entity.CourseImageFile;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import com.kangui.talentsharehub.domain.homework.entity.HomeworkAttachmentFile;
import com.kangui.talentsharehub.domain.homework.entity.SubmissionAttachmentFile;
import com.kangui.talentsharehub.domain.user.entity.UserImageFile;
import lombok.Getter;

@Getter
public class UploadFile {

    private final String uploadFileName;
    private final String storeFileName;
    private final String fileUrl;

    public UploadFile(String uploadFileName, String storeFileName, String fileUrl) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fileUrl = fileUrl;
    }

    public UserImageFile toUserImageFile() {
        return UserImageFile.builder()
                .uploadFileName(uploadFileName)
                .storeFileName(storeFileName)
                .fileUrl(fileUrl)
                .build();
    }

    public CourseImageFile toCourseImageFile() {
        return new CourseImageFile(
                null,
                uploadFileName,
                storeFileName,
                fileUrl
        );
    }

    public HomeworkAttachmentFile toHomeworkAttachmentFile() {
        return HomeworkAttachmentFile.builder()
                .uploadFileName(uploadFileName)
                .storeFileName(storeFileName)
                .fileUrl(fileUrl)
                .build();
    }

    public HomeworkAttachmentFile toHomeworkAttachmentFile(Homework homework) {
        return HomeworkAttachmentFile.builder()
                .homework(homework)
                .uploadFileName(uploadFileName)
                .storeFileName(storeFileName)
                .fileUrl(fileUrl)
                .build();
    }

    public SubmissionAttachmentFile toSubmissionAttachmentFile() {
        return SubmissionAttachmentFile.builder()
                .uploadFileName(uploadFileName)
                .storeFileName(storeFileName)
                .fileUrl(fileUrl)
                .build();
    }
}
