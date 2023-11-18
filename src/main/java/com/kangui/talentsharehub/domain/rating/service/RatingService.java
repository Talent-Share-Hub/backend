package com.kangui.talentsharehub.domain.rating.service;

import com.kangui.talentsharehub.domain.course.entity.Category;
import com.kangui.talentsharehub.domain.course.repository.category.CategoryRepository;
import com.kangui.talentsharehub.domain.rating.dto.request.RequestRating;
import com.kangui.talentsharehub.domain.rating.entity.Rating;
import com.kangui.talentsharehub.domain.rating.repository.RatingRepository;
import com.kangui.talentsharehub.domain.user.dto.response.ResponseUserById;
import com.kangui.talentsharehub.domain.user.entity.Users;
import com.kangui.talentsharehub.domain.user.repository.UserRepository;
import com.kangui.talentsharehub.domain.user.service.UserService;
import com.kangui.talentsharehub.global.exception.AppException;
import com.kangui.talentsharehub.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public double getAverageRatingByUserIdAndCategoryId(final Long userId, final Long categoryId) {
        if(!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다.");
        }

        if(!categoryRepository.existsById(categoryId)) {
            throw new AppException(ErrorCode.COURSE_CATEGORY_NOT_FOUND, "일치하는 카테고리가 없습니다.");
        }

        return ratingRepository.getAverageRatingByUserIdAndCategoryId(userId, categoryId);
    }

    public void addRating(final RequestRating requestRating, final Long userId, final Long categoryId) {
        final Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "일치하는 회원이 없습니다."));
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_CATEGORY_NOT_FOUND, "일치하는 카테고리가 없습니다."));

        final Rating rating = new Rating(
                user,
                category,
                requestRating.getRating()
        );

        ratingRepository.save(rating);
    }
}
