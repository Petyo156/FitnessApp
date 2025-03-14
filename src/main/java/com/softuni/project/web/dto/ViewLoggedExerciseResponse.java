package com.softuni.project.web.dto;

import lombok.Data;

@Data
public class ViewLoggedExerciseResponse {
    private String exerciseName;
    private Integer completedSets;
    private Integer completedReps;
    private Double addedWeight;
}