package com.kangui.talentsharehub.domain.rating.repository;

import com.kangui.talentsharehub.domain.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Rating r WHERE r.user.id = :userId AND r.category.id = :categoryId")
    double getAverageRatingByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
}

