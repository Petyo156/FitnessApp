package com.softuni.project.web.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class LogExerciseResponse {
    @Min(value = 1, message = "Reps > 0")
    private Integer reps;

    @Min(value = 1, message = "Sets > 0")
    private Integer sets;

    @Min(value = 0, message = "Added weight >= 0")
    private Double addedWeight;

    private String exerciseId;
}
