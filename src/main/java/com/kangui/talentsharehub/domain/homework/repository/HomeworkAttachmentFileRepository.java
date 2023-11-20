package com.kangui.talentsharehub.domain.homework.repository;

import com.kangui.talentsharehub.domain.homework.entity.HomeworkAttachmentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HomeworkAttachmentFileRepository extends JpaRepository<HomeworkAttachmentFile, Long> {

    @Query("SELECT haf.homework.course.id FROM HomeworkAttachmentFile haf WHERE haf.id = :homeworkAttachmentFileId")
    Long findCourseIdByHomeworkAttachmentFileId(@Param("homeworkAttachmentFileId") Long homeworkAttachmentFileId);

    @Query(
            "SELECT CASE WHEN COUNT(haf) > 0 THEN true ELSE false END " +
                    "FROM HomeworkAttachmentFile haf " +
                    "JOIN haf.homework h " +
                    "JOIN h.course c " +
                    "JOIN c.user u " +
                    "WHERE haf.id = :fileId AND u.id = :userId"
    )
    boolean validateTeacherByIdAndUserId(@Param("fileId") Long fileId, @Param("userId") Long userId);
}
