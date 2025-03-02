package com.softuni.project.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SubmitWorkoutRequest {
    @NotEmpty(message = "Enter at least one entry")
    private List<WorkoutExerciseEntry> exercises;

    private String additionalInfo;

    private Integer approximateDuration;
}
