package com.kangui.talentsharehub.domain.course.repository.course;

import com.kangui.talentsharehub.domain.course.dto.CourseSearchCondition;
import com.kangui.talentsharehub.domain.course.dto.response.ResponseCoursePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseCustomRepository {

    Page<ResponseCoursePage> getCoursePage(CourseSearchCondition condition, Pageable pageable);
}
