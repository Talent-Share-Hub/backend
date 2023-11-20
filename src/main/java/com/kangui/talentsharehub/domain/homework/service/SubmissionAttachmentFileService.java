package com.kangui.talentsharehub.domain.homework.service;

import com.kangui.talentsharehub.domain.homework.entity.Submission;
import com.kangui.talentsharehub.domain.homework.entity.SubmissionAttachmentFile;
import com.kangui.talentsharehub.domain.homework.repository.SubmissionAttachmentFileRepository;
import com.kangui.talentsharehub.domain.homework.repository.SubmissionRepository;
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
public class SubmissionAttachmentFileService {

    @Value("${file.storage}")
    private String storage;
    @Value("${file.submission}")
    private String submissionPath;

    private final SubmissionRepository submissionRepository;
    private final SubmissionAttachmentFileRepository submissionAttachmentFileRepository;
    private final FileStore fileStore;

    @Transactional(readOnly = true)
    public Resource downloadAttachById(
            final HttpServletResponse response, final Principal principal, final Long fileId) {
        final SubmissionAttachmentFile submissionAttachmentFile = submissionAttachmentFileRepository.findById(fileId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.SUBMISSION_ATTACHMENT_FILE_NOT_FOUND,
                        "과제 첨부파일이 존재하지 않습니다."));

        if(!(submissionAttachmentFileRepository.validateStudentByFileIdAndUserId(fileId, principal.userId()) ||
                submissionAttachmentFileRepository.validateTeacherByFileIdAndUserId(fileId, principal.userId()))
        ) {
            throw new AppException(ErrorCode.FORBIDDEN, "첨부파일을 다운로드할 권한이 없습니다.");
        }

        final String uploadFileName = submissionAttachmentFile.getUploadFileName();
        final String storeFileName = submissionAttachmentFile.getStoreFileName();

        UrlResource resource = null;

        try {
            resource = new UrlResource(storage + fileStore.getFullPath(storeFileName, submissionPath));
        } catch (MalformedURLException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND, "파일을 찾을 수 없습니다.");
        }

        final String encodeUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        final String contentDisposition = "attachment; filename=\"" + encodeUploadFileName + "\"";

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return resource;
    }

    public Long addAttachmentFileBySubmissionId(
            final Principal principal, final MultipartFile attachmentFile, final Long submissionId
    ) {
        final Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_NOT_FOUND, "제출한 과제가 존재하지 않습니다."));

        if(!submissionRepository.validateStudentByIdAndUserId(submissionId, principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "첨부파일을 추가할 권한이 없습니다.");
        }

        final UploadFile uploadFile = fileStore.storeFile(attachmentFile, submissionPath);

        return submissionAttachmentFileRepository.save(uploadFile.toSubmissionAttachmentFile(submission)).getId();
    }

    public void deleteAttachmentFileById(final Principal principal, final Long fileId) {
        SubmissionAttachmentFile submissionAttachmentFile = submissionAttachmentFileRepository.findById(fileId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_ATTACHMENT_FILE_NOT_FOUND,
                        "과제 제출 첨부파일이 존재하지 않습니다."));

        if(!submissionAttachmentFileRepository.validateStudentByFileIdAndUserId(fileId, principal.userId())
        ) {
            throw new AppException(ErrorCode.FORBIDDEN, "첨부파일을 삭제할 권한이 없습니다.");
        }

        fileStore.deleteFile(submissionAttachmentFile.getStoreFileName(), submissionPath);

        submissionAttachmentFileRepository.delete(submissionAttachmentFile);
    }
}
