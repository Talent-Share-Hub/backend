package com.kangui.talentsharehub.domain.homework.service;

import com.kangui.talentsharehub.domain.course.entity.Student;
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
public class SubmissionService {

    @Value("${file.submission}")
    private String submissionPath;

    private final SubmissionRepository submissionRepository;
    private final HomeworkRepository homeworkRepository;
    private final StudentRepository studentRepository;
    private final FileStore fileStore;

    @Transactional(readOnly = true)
    public ResponseSubmission getSubmissionsById(final Principal principal, final Long submissionId) {
        final Submission submission = submissionRepository.findByIdWithStudentAndAttachmentFile(submissionId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_NOT_FOUND, "존재 하지 않는 제출 과제 입니다."));

        if (!submission.getStudent().getId().equals(principal.userId())) {
           throw new AppException(ErrorCode.FORBIDDEN, "본인만 제출 과제 조회가 가능 합니다.");
        }

        return ResponseSubmission.of(submission);
    }

    public Long createSubmission(
            final Principal principal,
            final CreateSubmissionForm createSubmissionForm,
            final Long courseId,
            final Long homeworkId
    ) {
        final Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new AppException(ErrorCode.HOMEWORK_NOT_FOUND, "과제가 존재 하지 않습니다."));

        final Student student = studentRepository.findByCourseIdAndUserId(courseId, principal.userId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND, "존재 하지 않는 수강생 입니다."));

        final List<UploadFile> uploadFiles = fileStore.storeFiles(createSubmissionForm.getAttachmentFiles(), submissionPath);

        Submission submission = new Submission(
                student,
                homework,
                createSubmissionForm.getContents(),
                null
        );

        uploadFiles.stream()
                .map(UploadFile::toSubmissionAttachmentFile)
                .forEach(submission::addSubmissionAttachmentFile);

        return submissionRepository.save(submission).getId();
    }

    @Transactional
    public Long updateSubmission(
            final Principal principal,
            final Long submissionId,
            final RequestSubmission requestSubmission
    ) {
        Submission submission = submissionRepository.findByIdWithStudent(submissionId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_NOT_FOUND, "과제 제출이 존재하지 않습니다."));

        if (!submission.getStudent().getId().equals(principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "본인만 제출 과제 수정이 가능 합니다.");
        }

        submission.updateSubmission(requestSubmission.getContents());

        return submission.getId();
    }

    @Transactional
    public void deleteSubmission(final Principal principal, final Long submissionId) {
        Submission submission = submissionRepository.findByIdWithStudentAndAttachmentFile(submissionId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_NOT_FOUND, "과제 제출이 존재하지 않습니다."));

        if (!submission.getStudent().getId().equals(principal.userId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "본인만 제출 과제 수정이 가능 합니다.");
        }

        submission.getSubmissionAttachmentFile().forEach(submissionAttachmentFile -> {
            fileStore.deleteFile(submissionAttachmentFile.getStoreFileName(), submissionPath);
        });

        submissionRepository.delete(submission);
    }
}

