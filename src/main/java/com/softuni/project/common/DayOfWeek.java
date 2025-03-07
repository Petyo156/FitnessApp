package com.softuni.project.common;

import com.softuni.project.exception.DomainException;
import lombok.Getter;

@Getter
public enum DayOfWeek {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String displayName;

    DayOfWeek(String displayName) {
        this.displayName = displayName;
    }

    public DayOfWeek getNextDay() {
        return switch (this) {
            case MONDAY -> TUESDAY;
            case TUESDAY -> WEDNESDAY;
            case WEDNESDAY -> THURSDAY;
            case THURSDAY -> FRIDAY;
            case FRIDAY -> SATURDAY;
            case SATURDAY -> SUNDAY;
            default -> throw new DomainException("Invalid day");
        };
    }

    @Override
    public String toString() {
        return displayName;
    }
}
