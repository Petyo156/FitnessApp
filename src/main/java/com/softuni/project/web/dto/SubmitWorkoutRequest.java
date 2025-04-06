package com.softuni.project.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitWorkoutRequest {
    @NotEmpty(message = "Enter at least one exercise")
    private List<WorkoutExerciseEntry> exercises;

    @Size(max = 50, message = "Additional info has to be maximum 50 symbols")
    private String additionalInfo;

    private Integer approximateDuration;
}
