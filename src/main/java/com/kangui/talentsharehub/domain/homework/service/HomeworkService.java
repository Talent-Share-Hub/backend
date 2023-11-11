package com.kangui.talentsharehub.domain.homework.service;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.homework.dto.request.CreateHomeworkForm;
import com.kangui.talentsharehub.domain.homework.dto.request.RequestUpdateHomework;
import com.kangui.talentsharehub.domain.homework.dto.response.ResponseHomework;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
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

import java.io.IOException;
import java.net.MalformedURLException;
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

    public List<ResponseHomework> getHomeworksByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의 입니다."));

        List<Homework> homeworks = homeworkRepository.findByCourseIdWithAttachmentFile(course.getId());

        return homeworks.stream()
                .map(ResponseHomework::new)
                .toList();
    }

    @Transactional
    public Long createHomework(CreateHomeworkForm createHomeworkForm) {
        if (!createHomeworkForm.getStartDate().isAfter(createHomeworkForm.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE, "종료 일이 시작 일보다 빠를 수 없습니다.");
        }

        Course course = courseRepository.findById(createHomeworkForm.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의입니다."));

        List<UploadFile> uploadFiles = null;

        try {
            uploadFiles = fileStore.storeFiles(createHomeworkForm.getAttachmentFiles(), homeworkPath);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "파일 업로드에 실패했습니다.");
        }

        Homework homework = createHomeworkForm.toEntity(course);

        uploadFiles.stream()
                .map(UploadFile::toHomeworkAttachmentFile)
                .forEach(homework::addHomeworkAttachmentFile);

        return homeworkRepository.save(homework).getId();
    }

    @Transactional
    public Long updateHomework(Long homeworkId, RequestUpdateHomework requestUpdateHomework) {
        if (!requestUpdateHomework.getStartDate().isAfter(requestUpdateHomework.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE, "종료 일이 시작 일보다 빠를 수 없습니다.");
        }
        
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_NOT_FOUND, "과제가 존재하지 않습니다."));

        homework.updateHomework(
                requestUpdateHomework.getTitle(),
                requestUpdateHomework.getContents(),
                requestUpdateHomework.getStartDate(),
                requestUpdateHomework.getEndDate());

        return homework.getId();
    }

    @Transactional
    public void deleteHomework(Long homeworkId) {
        Homework homework = homeworkRepository.findByIdWithAttachmentFile(homeworkId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_NOT_FOUND, "과제가 존재하지 않습니다."));

        homework.getHomeworkAttachmentFile().forEach(homeworkAttachmentFile -> {
            try {
                fileStore.deleteFile(homeworkAttachmentFile.getStoreFileName(), homeworkPath);
            } catch (MalformedURLException e) {
                throw new AppException(ErrorCode.FILE_DELETE_FAILED, "과제 첨부 파일 삭제에 실패했습니다.");
            }
        });

        homeworkRepository.delete(homework);
    }
}
