package com.kangui.talentsharehub.domain.homework.repository;

import com.kangui.talentsharehub.domain.homework.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s FROM Submission s JOIN FETCH s.submissionAttachmentFile WHERE s.course.id =: courseId")
    List<Submission> findByCourseIdWithAttachmentFile(@Param("courseId") Long courseId);

    @Query("SELECT s FROM Submission s JOIN FETCH s.submissionAttachmentFile WHERE s.id =: submissionId")
    Optional<Submission> findByIdWithAttachmentFile(@Param("submissionId") Long submissionId);
}
