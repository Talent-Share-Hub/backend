package com.kangui.talentsharehub.domain.homework.repository;

import com.kangui.talentsharehub.domain.homework.entity.SubmissionAttachmentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubmissionAttachmentFileRepository extends JpaRepository<SubmissionAttachmentFile, Long> {

    @Query(
            "SELECT CASE WHEN COUNT(saf) > 0 THEN true ELSE false END " +
            "FROM SubmissionAttachmentFile saf " +
            "JOIN saf.submission s " +
            "JOIN s.student st " +
            "JOIN st.user u " +
            "WHERE saf.id = :fileId AND u.id = :userId"
    )
    boolean validateStudentByFileIdAndUserId(@Param("fileId") Long fileId, @Param("userId") Long userId);

    @Query(
            "SELECT CASE WHEN COUNT(saf) > 0 THEN true ELSE false END " +
            "FROM SubmissionAttachmentFile saf " +
            "JOIN saf.submission s " +
            "JOIN s.student st " +
            "JOIN st.course c " +
            "JOIN c.user u " +
            "WHERE saf.id = :fileId AND u.id = :userId"
    )
    boolean validateTeacherByFileIdAndUserId(@Param("fileId") Long fileId, @Param("userId") Long userId);
}
