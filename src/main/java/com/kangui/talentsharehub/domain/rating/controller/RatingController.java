package com.kangui.talentsharehub.domain.rating.controller;

import com.kangui.talentsharehub.domain.rating.dto.request.RequestRating;
import com.kangui.talentsharehub.domain.rating.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "별점 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rating")
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "평균 별점 조회", description = "user-id에 해당 하는 평균 별점 조회")
    @Parameters( {
            @Parameter(name = "user-id", description = "사용자 ID", example = "1"),
            @Parameter(name = "category-id", description = "카테고리 ID", example = "1"),
    })
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageRatingByUserId(@RequestParam(name = "user-id") Long userId, @RequestParam(name = "category-id") Long categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(ratingService.getAverageRatingByUserIdAndCategoryId(userId, categoryId));
    }

    @PostMapping
    public ResponseEntity<Void> addRating(@RequestBody RequestRating requestRating) {
        ratingService.addRating(requestRating);

        return ResponseEntity.noContent().build();
    }
}
