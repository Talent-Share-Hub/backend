package com.kangui.talentsharehub.domain.course.repository;

import com.kangui.talentsharehub.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
