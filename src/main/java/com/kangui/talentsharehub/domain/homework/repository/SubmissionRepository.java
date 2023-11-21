package com.kangui.talentsharehub.domain.homework.repository;

import com.kangui.talentsharehub.domain.homework.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s FROM Submission s JOIN FETCH s.student JOIN FETCH s.submissionAttachmentFile WHERE s.id = :submissionId")
    Optional<Submission> findByIdWithStudentAndAttachmentFile(@Param("submissionId") Long submissionId);

    @Query("SELECT s FROM Submission s JOIN FETCH s.student WHERE s.id = :submissionId")
    Optional<Submission> findByIdWithStudent(@Param("submissionId") Long submissionId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM Submission s " +
            "JOIN s.student st " +
            "JOIN st.user u " +
            "WHERE s.id = :submissionId AND u.id = :userId")
    boolean validateStudentByIdAndUserId(Long submissionId, Long userId);
}
