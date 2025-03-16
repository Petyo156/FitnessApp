package com.softuni.project.mapper;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.model.ExerciseStatus;
import com.softuni.project.web.dto.SubmitExerciseRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SubmitExerciseRequestToExerciseMapper implements Mapper<SubmitExerciseRequest, Exercise> {

    @Override
    public Exercise map(SubmitExerciseRequest submitExerciseRequest) {
        return Exercise.builder()
                .name(submitExerciseRequest.getName())
                .description(submitExerciseRequest.getDescription())
                .muscleGroups(submitExerciseRequest.getMuscleGroups())
                .mediaUrl(submitExerciseRequest.getMediaUrl())
                .difficulty(submitExerciseRequest.getDifficulty())
                .status(ExerciseStatus.PENDING)
                .createdOn(LocalDateTime.now())
                .build();
    }
}
