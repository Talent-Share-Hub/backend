package com.kangui.talentsharehub.domain.course.repository;

import com.kangui.talentsharehub.domain.course.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
