package com.kangui.talentsharehub.domain.rating.dto.request;

import com.kangui.talentsharehub.domain.course.entity.Category;
import com.kangui.talentsharehub.domain.rating.entity.Rating;
import com.kangui.talentsharehub.domain.user.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "별점 추가 요청")
@Getter
@AllArgsConstructor
public class RequestRating {

    @NotNull
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @NotNull
    @Schema(description = "강의 카테고리 ID", example = "1")
    private Long courseCategoryId;

    @NotNull
    @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하이어야 합니다.")
    @Schema(description = "별점", example = "1")
    private int rating;

    public Rating toEntity(Users user, Category category) {
        return Rating.builder()
                .user(user)
                .category(category)
                .rating(rating)
                .build();
    }
}
