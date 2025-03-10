package com.softuni.project.web.dto;

import com.softuni.project.common.DayOfWeek;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ViewWorkoutResponse {
    private List<WorkoutExerciseEntry> exercises;

    private UUID workoutId;

    private String additionalInfo;

    private Integer approximateDuration;

    private DayOfWeek dayOfWeek;
}
