package com.softuni.project.mapper;

import com.softuni.project.web.dto.SubmitWorkoutRequest;
import com.softuni.project.workout.model.Workout;
import org.springframework.stereotype.Component;

@Component
public class SubmitWorkoutRequestToWorkoutMapper implements Mapper<SubmitWorkoutRequest, Workout> {
    @Override
    public Workout map(SubmitWorkoutRequest submitWorkoutRequest) {
        return Workout.builder()
                .additionalInfo(submitWorkoutRequest.getAdditionalInfo())
                .duration(submitWorkoutRequest.getApproximateDuration())
                .build();
    }
}
