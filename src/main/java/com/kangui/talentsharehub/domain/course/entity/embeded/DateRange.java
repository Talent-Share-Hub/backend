package com.kangui.talentsharehub.domain.course.entity.embeded;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateRange {

    private LocalDate startDate;

    private LocalDate endDate;

    public DateRange(final LocalDate startDate, final LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
