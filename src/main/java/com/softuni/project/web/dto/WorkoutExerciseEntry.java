package com.softuni.project.web.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutExerciseEntry {
    private String exerciseName;

    private String mediaUrl;

    @Min(value = 1, message = "Sets >= 1")
    private Integer sets;

    @Min(value = 1, message = "Reps >= 1")
    private Integer reps;

    @Min(value = 0, message = "Added weight cannot be negative")
    private Double addedWeight;
}