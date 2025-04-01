package com.softuni.project.web.dto;

import com.softuni.project.excersise.model.Difficulty;
import com.softuni.project.excersise.model.MuscleGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitExerciseRequest {
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 2, max = 20, message= "Name has to be least 2 and maximum 20 symbols")
    private String name;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 10, max = 50, message = "Description has to be least 10 and maximum 50 symbols")
    private String description;

    @NotNull(message = "Difficulty cannot be null")
    private Difficulty difficulty;

    @NotEmpty(message = "Choose muscle groups")
    private List<MuscleGroup> muscleGroups;

    private String mediaUrl;
}
