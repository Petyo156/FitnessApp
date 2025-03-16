package com.softuni.project.mapper;

import com.softuni.project.web.dto.ViewWorkoutResponse;
import com.softuni.project.workout.model.Workout;
import org.springframework.stereotype.Component;

@Component
public class WorkoutToViewWorkoutResponseMapper implements Mapper<Workout, ViewWorkoutResponse> {
    @Override
    public ViewWorkoutResponse map(Workout workout) {
        return ViewWorkoutResponse.builder()
                .additionalInfo(workout.getAdditionalInfo())
                .approximateDuration(workout.getDuration())
                .workoutId(workout.getId().toString())
                .additionalInfo(workout.getAdditionalInfo())
                .approximateDuration(workout.getDuration())
                .build();
    }
}
