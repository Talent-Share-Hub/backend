package com.kangui.talentsharehub.domain.course.entity.embeded;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@Embeddable
@RequiredArgsConstructor
public class DateRange {

    private final LocalDate startDate;

    private final LocalDate endDate;

    public DateRange(final LocalDate startDate, final LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
