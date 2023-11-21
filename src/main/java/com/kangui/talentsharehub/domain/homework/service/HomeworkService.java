package com.kangui.talentsharehub.domain.homework.service;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.course.repository.student.StudentRepository;
import com.kangui.talentsharehub.domain.homework.dto.request.CreateHomeworkForm;
import com.kangui.talentsharehub.domain.homework.dto.request.RequestUpdateHomework;
import com.kangui.talentsharehub.domain.homework.dto.response.ResponseHomework;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import com.kangui.talentsharehub.domain.homework.repository.HomeworkRepository;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import com.kangui.talentsharehub.global.file.FileStore;
import com.kangui.talentsharehub.global.file.UploadFile;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HomeworkService {

    @Value("${file.homework}")
    private String homeworkPath;

    private final HomeworkRepository homeworkRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final FileStore fileStore;

    @Transactional(readOnly = true)
    public List<ResponseHomework> getHomeworksByCourseId(final Principal principal, final Long courseId) {
        if(!courseRepository.existsById(courseId)) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의 입니다.");
        }

        if(!(courseRepository.existsByCourseIdAndTeacherId(courseId, principal.userId()) ||
                studentRepository.existsByCourseIdAndUserId(courseId, principal.userId()))) {
            throw new AppException(ErrorCode.FORBIDDEN, "강의 관계자만 과제 조회가 가능합니다.");
        }

        final List<Homework> homeworks = homeworkRepository.findByCourseIdWithAttachmentFile(courseId);

        return homeworks.stream()
                .map(ResponseHomework::of)
                .toList();
    }

    public Long createHomework(
            final CreateHomeworkForm createHomeworkForm,
            final Principal principal,
            final Long courseId
    ) {
        final Course course = courseRepository.findByIdWithUser(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의입니다."));

        if(!course.getUser().getId().equals(principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "선생님만 과제 생성이 가능합니다.");
        }

        final List<UploadFile> uploadFiles = fileStore.storeFiles(createHomeworkForm.getAttachmentFiles(), homeworkPath);

        final Homework homework = new Homework(
                course,
                createHomeworkForm.getTitle(),
                createHomeworkForm.getContents(),
                createHomeworkForm.getStartDate(),
                createHomeworkForm.getEndDate()
        );

        uploadFiles.stream()
                .map(UploadFile::toHomeworkAttachmentFile)
                .forEach(homework::addHomeworkAttachmentFile);

        return homeworkRepository.save(homework).getId();
    }

    public Long updateHomework(
            final RequestUpdateHomework requestUpdateHomework,
            final Principal principal,
            final Long homeworkId
    ) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_NOT_FOUND, "과제가 존재하지 않습니다."));

        if(!homeworkRepository.validateTeacherByIdAndUserId(homeworkId, principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "선생님만 과제 수정이 가능합니다.");

        }

        homework.updateHomework(requestUpdateHomework);

        return homework.getId();
    }

    public void deleteHomework(final Principal principal, final Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_NOT_FOUND, "과제가 존재하지 않습니다."));

        if(!homeworkRepository.validateTeacherByIdAndUserId(homeworkId, principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "선생님만 과제 수정이 가능합니다.");

        }

        homework.getHomeworkAttachmentFile().forEach(homeworkAttachmentFile -> {
            fileStore.deleteFile(homeworkAttachmentFile.getStoreFileName(), homeworkPath);
        });

        homeworkRepository.delete(homework);
    }
}
