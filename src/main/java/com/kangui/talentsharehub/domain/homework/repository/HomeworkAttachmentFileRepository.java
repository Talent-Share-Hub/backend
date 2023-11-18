package com.kangui.talentsharehub.domain.homework.repository;

import com.kangui.talentsharehub.domain.homework.entity.HomeworkAttachmentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HomeworkAttachmentFileRepository extends JpaRepository<HomeworkAttachmentFile, Long> {

    @Query("SELECT h.homework.course.id FROM HomeworkAttachmentFile h WHERE h.id = :homeworkAttachmentFileId")
    Long findCourseIdByHomeworkAttachmentFileId(@Param("homeworkAttachmentFileId") Long homeworkAttachmentFileId);
}
