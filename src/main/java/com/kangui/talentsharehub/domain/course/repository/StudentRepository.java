package com.kangui.talentsharehub.domain.course.repository;

import com.kangui.talentsharehub.domain.course.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
