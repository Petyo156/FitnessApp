package com.softuni.project.web.dto;

import com.softuni.project.excersise.model.Difficulty;
import com.softuni.project.excersise.model.MuscleGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SubmitExerciseRequest {
    @NotEmpty(message = "Name must not be empty")
    private String name;

    @NotEmpty(message = "Description must not be empty")
    private String description;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private List<MuscleGroup> muscleGroups;

    private String mediaUrl;
}
