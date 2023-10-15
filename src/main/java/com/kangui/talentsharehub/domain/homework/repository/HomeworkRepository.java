package com.kangui.talentsharehub.domain.homework.repository;

import com.kangui.talentsharehub.domain.homework.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
}
