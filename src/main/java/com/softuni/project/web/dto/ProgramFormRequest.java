package com.softuni.project.web.dto;

import com.softuni.project.program.model.Difficulty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramFormRequest {
    @Nullable
    private String mondayWorkoutId;

    @Nullable
    private String tuesdayWorkoutId;

    @Nullable
    private String wednesdayWorkoutId;

    @Nullable
    private String thursdayWorkoutId;

    @Nullable
    private String fridayWorkoutId;

    @Nullable
    private String saturdayWorkoutId;

    @Nullable
    private String sundayWorkoutId;

    @Size(min = 3, max = 20, message = "Program name must be 3-20 chars length")
    private String name;

    @Size(min = 10, max = 50, message = "Program description must be 10-50 chars length")
    private String description;

    @NotNull
    private Boolean shared;

    @NotNull(message = "Difficulty cannot be null")
    private Difficulty difficulty;
}
