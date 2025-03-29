package com.softuni.project.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogExerciseResponse {
    @Min(value = 1, message = "Reps > 0")
    @NotNull
    private Integer reps;

    @Min(value = 1, message = "Sets > 0")
    @NotNull
    private Integer sets;

    @Min(value = 0, message = "Added weight >= 0")
    @NotNull
    private Double addedWeight;

    private String exerciseId;
}
