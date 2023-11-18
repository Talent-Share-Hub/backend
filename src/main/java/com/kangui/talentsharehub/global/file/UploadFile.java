package com.kangui.talentsharehub.global.file;

import com.kangui.talentsharehub.domain.course.entity.CourseImageFile;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import com.kangui.talentsharehub.domain.homework.entity.HomeworkAttachmentFile;
import com.kangui.talentsharehub.domain.homework.entity.Submission;
import com.kangui.talentsharehub.domain.homework.entity.SubmissionAttachmentFile;
import com.kangui.talentsharehub.domain.user.entity.UserImageFile;
import lombok.Getter;

@Getter
public class UploadFile {

    private final String uploadFileName;
    private final String storeFileName;
    private final String fileUrl;

    public UploadFile(final String uploadFileName, final String storeFileName, final String fileUrl) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fileUrl = fileUrl;
    }

    public UserImageFile toUserImageFile() {
        return new UserImageFile(
                null,
                uploadFileName,
                storeFileName,
                fileUrl
        );
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
        return new HomeworkAttachmentFile(
                null,
                uploadFileName,
                storeFileName,
                fileUrl
        );
    }

    public HomeworkAttachmentFile toHomeworkAttachmentFile(Homework homework) {
        return new HomeworkAttachmentFile(
                homework,
                uploadFileName,
                storeFileName,
                fileUrl
        );
    }

    public SubmissionAttachmentFile toSubmissionAttachmentFile() {
        return new SubmissionAttachmentFile(
                null,
                uploadFileName,
                storeFileName,
                fileUrl
        );
    }

    public SubmissionAttachmentFile toSubmissionAttachmentFile(Submission submission) {
        return new SubmissionAttachmentFile(
                submission,
                uploadFileName,
                storeFileName,
                fileUrl
        );
    }
}
