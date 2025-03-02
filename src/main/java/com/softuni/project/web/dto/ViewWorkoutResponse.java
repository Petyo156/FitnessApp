package com.softuni.project.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ViewWorkoutResponse {
    private List<WorkoutExerciseEntry> exercises;

    private String additionalInfo;

    private Integer approximateDuration;
}
