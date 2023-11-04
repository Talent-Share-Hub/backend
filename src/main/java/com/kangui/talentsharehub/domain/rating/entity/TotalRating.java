package com.kangui.talentsharehub.domain.rating.entity;

import com.kangui.talentsharehub.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "total_rating")
public class TotalRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "total_rating_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user; // 사용자 ID

    private double totalRating; // 총 별점

    @Builder
    public TotalRating(Long id, Users user, double totalRating) {
        this.id = id;
        this.user = user;
        this.totalRating = totalRating;
    }
}
