package com.softuni.project.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutLogRequest {
    @NotEmpty(message = "Please log the exercises.")
    private List<LogExerciseRequest> loggedExercises;
}
