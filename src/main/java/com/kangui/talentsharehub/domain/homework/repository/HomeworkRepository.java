package com.kangui.talentsharehub.domain.homework.repository;

import com.kangui.talentsharehub.domain.homework.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    @Query("SELECT h FROM Homework h WHERE h.course.id = :courseId")
    List<Homework> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT h FROM Homework h JOIN FETCH h.homeworkAttachmentFile WHERE h.course.id = :courseId")
    List<Homework> findByCourseIdWithAttachmentFile(@Param("courseId") Long courseId);
}
