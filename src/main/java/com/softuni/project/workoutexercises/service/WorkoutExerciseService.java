package com.softuni.project.workoutexercises.service;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workoutexercises.model.WorkoutExercise;
import com.softuni.project.workoutexercises.repository.WorkoutExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WorkoutExerciseService {
    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    public WorkoutExerciseService(WorkoutExerciseRepository workoutExerciseRepository) {
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    public void saveEntry(int reps, int sets, double addedWeight, Exercise exercise, Workout workout) {

        WorkoutExercise workoutExercise = initializeWorkoutExercise(reps, sets, addedWeight, exercise, workout);

        workoutExerciseRepository.save(workoutExercise);
    }

    private WorkoutExercise initializeWorkoutExercise(int reps, int sets, double addedWeight, Exercise exercise, Workout workout) {
        return WorkoutExercise.builder()
                .reps(reps)
                .sets(sets)
                .addedWeight(addedWeight)
                .exercise(exercise)
                .workout(workout)
                .build();
    }

    public List<WorkoutExercise> findAllByWorkoutId(UUID id) {
        return workoutExerciseRepository.findAllByWorkout_Id(id);
    }
}
