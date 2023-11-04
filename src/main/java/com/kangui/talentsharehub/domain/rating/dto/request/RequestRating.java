package com.kangui.talentsharehub.domain.rating.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Category;
import com.kangui.talentsharehub.domain.rating.entity.Rating;
import com.kangui.talentsharehub.domain.user.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "별점 추가 요청")
@Getter
@AllArgsConstructor
public class RequestRating {

    private Long userId;
    private Long courseCategoryId;
    private int rating;

    public Rating toEntity(Users user, Category category) {
        return Rating.builder()
                .user(user)
                .category(category)
                .rating(rating)
                .build();
    }
}
