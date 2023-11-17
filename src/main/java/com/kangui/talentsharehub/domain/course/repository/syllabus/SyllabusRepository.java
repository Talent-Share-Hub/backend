package com.kangui.talentsharehub.domain.course.repository.syllabus;

import com.kangui.talentsharehub.domain.course.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {
    List<Syllabus> findByCourseId(Long courseId);

    boolean existsByWeek(int week);
}
