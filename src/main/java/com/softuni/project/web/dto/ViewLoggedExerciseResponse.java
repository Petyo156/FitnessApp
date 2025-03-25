package com.softuni.project.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewLoggedExerciseResponse {
    private String exerciseName;
    private Integer completedSets;
    private Integer completedReps;
    private Double addedWeight;
}