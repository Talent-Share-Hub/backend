package com.kangui.talentsharehub.domain.homework.service;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.homework.dto.request.CreateHomeworkForm;
import com.kangui.talentsharehub.domain.homework.dto.request.RequestUpdateHomework;
import com.kangui.talentsharehub.domain.homework.dto.response.ResponseHomework;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import com.kangui.talentsharehub.domain.homework.entity.HomeworkAttachmentFile;
import com.kangui.talentsharehub.domain.homework.repository.HomeworkRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.file.FileStore;
import com.kangui.talentsharehub.global.file.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class HomeworkService {

    @Value("${file.homework}")
    private String homeworkPath;

    private final HomeworkRepository homeworkRepository;
    private final CourseRepository courseRepository;
    private final FileStore fileStore;

    @Transactional
    public Long createHomework(CreateHomeworkForm requestCreateHomework) {
        Course course = courseRepository.findById(requestCreateHomework.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의입니다."));

        List<UploadFile> uploadFiles = null;

        Homework homework = requestCreateHomework.toEntity(course);

        try {
            uploadFiles = fileStore.storeFiles(requestCreateHomework.getAttachmentFiles(), homeworkPath);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "파일 업로드에 실패했습니다.");
        }

        for (UploadFile uploadFile : uploadFiles) {
            HomeworkAttachmentFile homeworkAttachmentFile = HomeworkAttachmentFile.builder()
                    .uploadFileName(uploadFile.getUploadFileName())
                    .storeFileName(uploadFile.getStoreFileName())
                    .fileUrl(uploadFile.getFileUrl())
                    .build();

            homework.addHomeworkAttachmentFile(homeworkAttachmentFile);
        }

        return homeworkRepository.save(homework).getId();
    }

    public List<ResponseHomework> getHomeworksByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의 입니다."));

        List<Homework> homeworks = homeworkRepository.findByCourseIdWithAttachmentFile(course.getId());

        return homeworks.stream()
                .map(ResponseHomework::new)
                .toList();
    }

    @Transactional
    public Long updateHomework(Long homeworkId, RequestUpdateHomework requestUpdateHomework) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_NOT_FOUND, "과제가 존재하지 않습니다."));

        homework.updateHomework(
                requestUpdateHomework.getTitle(),
                requestUpdateHomework.getContents(),
                requestUpdateHomework.getStartDate(),
                requestUpdateHomework.getEndDate());

        return homework.getId();
    }
}
