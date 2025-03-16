package com.softuni.project.mapper;

import com.softuni.project.web.dto.WorkoutExerciseEntry;
import com.softuni.project.workoutexercise.model.WorkoutExercise;
import org.springframework.stereotype.Component;

@Component
public class WorkoutExerciseToWorkoutExerciseEntryMapper implements Mapper<WorkoutExercise, WorkoutExerciseEntry> {
    @Override
    public WorkoutExerciseEntry map(WorkoutExercise workoutExercise) {
        return WorkoutExerciseEntry.builder()
                .exerciseName(workoutExercise.getExercise().getName())
                .sets(workoutExercise.getSets())
                .reps(workoutExercise.getReps())
                .addedWeight(workoutExercise.getAddedWeight())
                .mediaUrl(workoutExercise.getExercise().getMediaUrl())
                .exerciseId(workoutExercise.getExercise().getId().toString())
                .build();
    }
}
