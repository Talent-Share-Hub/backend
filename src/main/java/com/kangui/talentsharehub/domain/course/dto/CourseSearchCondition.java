package com.kangui.talentsharehub.domain.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CourseSearchCondition {

    final String search;
    final String category;
}
