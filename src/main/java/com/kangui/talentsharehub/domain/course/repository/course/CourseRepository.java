package com.kangui.talentsharehub.domain.course.repository.course;

import com.kangui.talentsharehub.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseCustomRepository {

    @Query("SELECT c FROM Course c JOIN FETCH c.user JOIN FETCH c.category WHERE c.id = :courseId")
    Course findCourseWithUserAndCategoryById(Long courseId);
}
