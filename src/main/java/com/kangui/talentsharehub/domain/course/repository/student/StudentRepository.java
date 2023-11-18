package com.kangui.talentsharehub.domain.course.repository.student;

import com.kangui.talentsharehub.domain.course.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s JOIN FETCH s.user WHERE s.course.id =: courseId")
    List<Student> findByCourseIdWithUser(@Param("courseId") Long courseId);

    Optional<Student> findByUserIdAndCourseId(Long userId, Long courseId);

    boolean existsByCourseIdAndUserId(Long courseId, Long userId);

    @Query("SELECT s FROM Student s WHERE s.course.id =: courseId AND s.user.id =: userId")
    Optional<Student> findByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);
}
