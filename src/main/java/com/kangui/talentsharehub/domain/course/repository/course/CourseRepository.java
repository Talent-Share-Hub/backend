package com.kangui.talentsharehub.domain.course.repository.course;

import com.kangui.talentsharehub.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseCustomRepository {

    @Query("SELECT c FROM Course c JOIN FETCH c.user JOIN FETCH c.category JOIN FETCH c.courseImageFile WHERE c.id = :courseId")
    Optional<Course> findByIdWithUserAndCategoryAndCourseImageFile(@Param("courseId") Long courseId);

    @Query("SELECT c FROM Course c JOIN FETCH c.courseImageFile WHERE c.id = :courseId")
    Optional<Course> findByIdWithCourseImageFile(@Param("courseId") Long courseId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Course c " +
            "WHERE c.id = :courseId AND c.user.id = :teacherId")
    boolean existsByCourseIdAndTeacherId(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId);

    @Query("SELECT c FROM Course c JOIN FETCH c.user WHERE c.id = :courseId")
    Optional<Course> findByIdWithUser(@Param("courseId") Long courseId);
}
