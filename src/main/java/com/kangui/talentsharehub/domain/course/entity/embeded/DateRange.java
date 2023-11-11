package com.kangui.talentsharehub.domain.course.entity.embeded;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Embeddable
@NoArgsConstructor
public class DateRange {

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder
    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
