package com.kangui.talentsharehub.domain.rating.controller;

import com.kangui.talentsharehub.domain.rating.dto.request.RequestRating;
import com.kangui.talentsharehub.domain.rating.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "별점 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rating")
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "평균 별점 조회", description = "user-id에 해당 하는 평균 별점 조회")
    @GetMapping("/average/user/{user-id}/category/{category-id}")
    public ResponseEntity<Double> getAverageRatingByUserId(
                @PathVariable(name = "user-id") final Long userId,
                @PathVariable(name = "category-id") final Long categoryId
    ) {
        return ResponseEntity.status(OK)
                .body(ratingService.getAverageRatingByUserIdAndCategoryId(userId, categoryId));
    }

    @PostMapping("/user/{user-id}/category/{category-id}")
    public ResponseEntity<Void> addRating(
            @Valid @RequestBody final RequestRating requestRating,
            @PathVariable(name = "user-id") final Long userId,
            @PathVariable(name = "category-id") final Long categoryId
    ) {
        ratingService.addRating(requestRating, userId, categoryId);

        return ResponseEntity.noContent().build();
    }
}
