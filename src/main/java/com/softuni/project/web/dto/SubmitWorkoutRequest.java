package com.softuni.project.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SubmitWorkoutRequest {
    @NotEmpty(message = "Enter at least one exercise")
    private List<WorkoutExerciseEntry> exercises;

    @Size(max = 50, message = "Additional info has to be maximum 50 symbols")
    private String additionalInfo;

    private Integer approximateDuration;
}
