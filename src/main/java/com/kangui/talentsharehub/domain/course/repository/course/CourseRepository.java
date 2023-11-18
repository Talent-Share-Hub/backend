package com.kangui.talentsharehub.domain.course.repository.course;

import com.kangui.talentsharehub.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseCustomRepository {

    @Query("SELECT c FROM Course c JOIN FETCH c.user JOIN FETCH c.category JOIN FETCH c.courseImageFile WHERE c.id = :courseId")
    Optional<Course> findCourseWithUserAndCategoryAndCourseImageFileById(@Param("courseId") Long courseId);

    @Query("SELECT c FROM Course c JOIN FETCH c.courseImageFile WHERE c.id = :courseId")
    Optional<Course> findByIdWithCourseImageFile(@Param("courseId") Long courseId);

    boolean existsByCourseIdAndUserId(Long courseId, Long aLong);

    @Query("SELECT c FROM Course c JOIN FETCH c.user WHERE c.id = :courseId")
    Optional<Course> findByIdWithUser(@Param("courseId") Long courseId);
}
