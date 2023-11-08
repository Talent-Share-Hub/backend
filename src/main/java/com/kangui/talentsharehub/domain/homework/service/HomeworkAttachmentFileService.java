package com.kangui.talentsharehub.domain.homework.service;

import com.kangui.talentsharehub.domain.homework.entity.Homework;
import com.kangui.talentsharehub.domain.homework.entity.HomeworkAttachmentFile;
import com.kangui.talentsharehub.domain.homework.repository.HomeworkAttachmentFileRepository;
import com.kangui.talentsharehub.domain.homework.repository.HomeworkRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.file.FileStore;
import com.kangui.talentsharehub.global.file.UploadFile;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class HomeworkAttachmentFileService {

    @Value("${file.storage}")
    private String storage;
    @Value("${file.homework}")
    private String homeworkPath;

    private final HomeworkRepository homeworkRepository;
    private final HomeworkAttachmentFileRepository homeworkAttachmentFileRepository;
    private final FileStore fileStore;

    public Resource downloadAttachById(HttpServletResponse response, Long fileId) {

        HomeworkAttachmentFile homeworkAttachmentFile = homeworkAttachmentFileRepository.findById(fileId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_ATTACHMENT_FILE_NOT_FOUND, fileId + "과제 첨부파일이 존재하지 않습니다."));

        String uploadFileName = homeworkAttachmentFile.getUploadFileName();
        String storeFileName = homeworkAttachmentFile.getStoreFileName();

        UrlResource resource = null;

        try {
            resource = new UrlResource(storage + fileStore.getFullPath(storeFileName, homeworkPath));
        } catch (MalformedURLException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND, "파일을 찾을 수 없습니다.");
        }

        String encodeUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodeUploadFileName + "\"";

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return resource;
    }

    @Transactional
    public Long addAttachmentFileByHomeworkId(Long homeworkId, MultipartFile attachmentFile) {

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_NOT_FOUND, "과제가 존재하지 않습니다."));

        UploadFile uploadFile = null;

        try {
            uploadFile = fileStore.storeFile(attachmentFile, homeworkPath);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "파일 업로드에 실패했습니다.");
        }

        return homeworkAttachmentFileRepository.save(HomeworkAttachmentFile.builder()
                                                        .homework(homework)
                                                        .uploadFileName(uploadFile.getUploadFileName())
                                                        .storeFileName(uploadFile.getStoreFileName())
                                                        .fileUrl(uploadFile.getFileUrl())
                                                        .build()).getId();
    }

    @Transactional
    public void deleteAttachmentFileById(Long fileId) {

        HomeworkAttachmentFile homeworkAttachmentFile = homeworkAttachmentFileRepository.findById(fileId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_ATTACHMENT_FILE_NOT_FOUND, fileId + "과제 첨부파일이 존재하지 않습니다."));

        try {
            fileStore.deleteFile(homeworkAttachmentFile.getStoreFileName(), homeworkPath);
        } catch (MalformedURLException e) {
            log.error("과제 첨부 파일 삭제에 실패 했습니다. fileId: {}", fileId, e);
            throw new AppException(ErrorCode.FILE_DELETE_FAILED, "과제 첨부 파일 삭제에 실패 했습니다.");
        }

        homeworkAttachmentFileRepository.delete(homeworkAttachmentFile);
    }
}
