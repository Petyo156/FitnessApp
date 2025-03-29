package com.softuni.project.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutLogRequest {
    @Valid
    private List<LogExerciseResponse> loggedExercises;
}
