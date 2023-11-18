package com.kangui.talentsharehub.domain.homework.service;

import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.course.repository.student.StudentRepository;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import com.kangui.talentsharehub.domain.homework.entity.HomeworkAttachmentFile;
import com.kangui.talentsharehub.domain.homework.repository.HomeworkAttachmentFileRepository;
import com.kangui.talentsharehub.domain.homework.repository.HomeworkRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.file.FileStore;
import com.kangui.talentsharehub.global.file.UploadFile;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HomeworkAttachmentFileService {

    @Value("${file.storage}")
    private String storage;
    @Value("${file.homework}")
    private String homeworkPath;

    private final HomeworkRepository homeworkRepository;
    private final HomeworkAttachmentFileRepository homeworkAttachmentFileRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final FileStore fileStore;

    @Transactional(readOnly = true)
    public Resource downloadAttachById(
            final HttpServletResponse response, final Principal principal, final Long fileId
    ) {
        Long courseId = homeworkAttachmentFileRepository.findCourseIdByHomeworkAttachmentFileId(fileId);

        if(!(courseRepository.existsByCourseIdAndUserId(courseId, principal.userId()) ||
            studentRepository.existsByCourseIdAndUserId(courseId, principal.userId()))) {
            throw new AppException(ErrorCode.FORBIDDEN, "강의 관계자만 다운로드가 가능합니다.");
        }

        final HomeworkAttachmentFile homeworkAttachmentFile = homeworkAttachmentFileRepository.findById(fileId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_ATTACHMENT_FILE_NOT_FOUND
                                                        , fileId + "과제 첨부파일이 존재하지 않습니다."));

        final String uploadFileName = homeworkAttachmentFile.getUploadFileName();
        final String storeFileName = homeworkAttachmentFile.getStoreFileName();

        UrlResource resource = null;

        try {
            resource = new UrlResource(storage + fileStore.getFullPath(storeFileName, homeworkPath));
        } catch (MalformedURLException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND, "파일을 찾을 수 없습니다.");
        }

        final String encodeUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        final String contentDisposition = "attachment; filename=\"" + encodeUploadFileName + "\"";

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return resource;
    }

    public Long addAttachmentFileByHomeworkId(
            final Principal principal, final Long homeworkId, final MultipartFile attachmentFile
    ) {
        if(homeworkRepository.validateTeacherByIdAndUserId(homeworkId, principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "선생님만 첨부파일 추가가 가능합니다.");
        }

        final Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_NOT_FOUND, "과제가 존재하지 않습니다."));

        UploadFile uploadFile = fileStore.storeFile(attachmentFile, homeworkPath);

        return homeworkAttachmentFileRepository.save(uploadFile.toHomeworkAttachmentFile(homework)).getId();
    }

    public void deleteAttachmentFileById(final Principal principal, final Long fileId) {
        Long courseId = homeworkAttachmentFileRepository.findCourseIdByHomeworkAttachmentFileId(fileId);

        if(courseRepository.existsByCourseIdAndUserId(courseId, principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "선생님만 첨부파일 삭제가 가능합니다.");
        }

        HomeworkAttachmentFile homeworkAttachmentFile = homeworkAttachmentFileRepository.findById(fileId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_ATTACHMENT_FILE_NOT_FOUND
                                                        , fileId + "과제 첨부파일이 존재하지 않습니다."));

        fileStore.deleteFile(homeworkAttachmentFile.getStoreFileName(), homeworkPath);

        homeworkAttachmentFileRepository.delete(homeworkAttachmentFile);
    }
}
