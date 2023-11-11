package com.kangui.talentsharehub.domain.homework.service;

import com.kangui.talentsharehub.domain.course.entity.Course;
import com.kangui.talentsharehub.domain.course.entity.Student;
import com.kangui.talentsharehub.domain.course.repository.course.CourseRepository;
import com.kangui.talentsharehub.domain.course.repository.student.StudentRepository;
import com.kangui.talentsharehub.domain.homework.dto.request.CreateSubmissionForm;
import com.kangui.talentsharehub.domain.homework.dto.request.RequestSubmission;
import com.kangui.talentsharehub.domain.homework.dto.response.ResponseSubmission;
import com.kangui.talentsharehub.domain.homework.entity.Homework;
import com.kangui.talentsharehub.domain.homework.entity.Submission;
import com.kangui.talentsharehub.domain.homework.repository.HomeworkRepository;
import com.kangui.talentsharehub.domain.homework.repository.SubmissionRepository;
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
public class SubmissionService {

    @Value("${file.submission}")
    private String submissionPath;

    private final SubmissionRepository submissionRepository;
    private final HomeworkRepository homeworkRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final FileStore fileStore;

    public List<ResponseSubmission> getSubmissionsByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND, "존재 하지 않는 강의 입니다."));

        List<Submission> submissions = submissionRepository.findByCourseIdWithAttachmentFile(course.getId());

        return submissions.stream()
                .map(ResponseSubmission::new)
                .toList();
    }

    @Transactional
    public Long createSubmission(CreateSubmissionForm createSubmissionForm) {
        Homework homework = homeworkRepository.findByIdWithAttachmentFile(createSubmissionForm.getHomeworkId())
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_NOT_FOUND, "과제가 존재하지 않습니다."));

        Student student = studentRepository.findById(createSubmissionForm.getStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND, "존재하지 않는 수강생입니다."));

        List<UploadFile> uploadFiles = null;

        try {
            uploadFiles = fileStore.storeFiles(createSubmissionForm.getAttachmentFiles(), submissionPath);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "파일 업로드에 실패했습니다.");
        }

        Submission submission = createSubmissionForm.toEntity(homework, student);

        uploadFiles.stream()
                .map(UploadFile::toSubmissionAttachmentFile)
                .forEach(submission::addSubmissionAttachmentFile);

        return submissionRepository.save(submission).getId();
    }

    @Transactional
    public Long updateSubmission(Long submissionId, RequestSubmission requestSubmission) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_NOT_FOUND, "과제 제출이 존재하지 않습니다."));

        submission.updateSubmission(
                requestSubmission.getContents());

        return submission.getId();
    }

    @Transactional
    public void deleteSubmission(Long submissionId) {
        Submission submission = submissionRepository.findByIdWithAttachmentFile(submissionId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_NOT_FOUND, "과제 제출이 존재하지 않습니다."));

        submission.getSubmissionAttachmentFile().forEach(submissionAttachmentFile -> {
            try {
                fileStore.deleteFile(submissionAttachmentFile.getStoreFileName(), submissionPath);
            } catch (MalformedURLException e) {
                throw new AppException(ErrorCode.FILE_DELETE_FAILED, "과제 제출 첨부 파일 삭제에 실패했습니다.");
            }
        });

        submissionRepository.delete(submission);
    }
}

