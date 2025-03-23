package com.softuni.project.web.dto;

import com.softuni.project.common.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewWorkoutResponse {
    private List<WorkoutExerciseEntry> exercises;

    private String workoutId;

    private String additionalInfo;

    private Integer approximateDuration;

    private DayOfWeek dayOfWeek;
}
