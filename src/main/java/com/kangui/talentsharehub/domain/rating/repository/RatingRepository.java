package com.kangui.talentsharehub.domain.rating.repository;

import com.kangui.talentsharehub.domain.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
