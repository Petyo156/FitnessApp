package com.softuni.project.config;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.SubmitWorkoutRequest;
import com.softuni.project.web.dto.WorkoutExerciseEntry;
import com.softuni.project.workout.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Order(4)
@Component
public class InitializeWorkout implements CommandLineRunner {
    private final UserService userService;

    private final WorkoutService workoutService;

    private final ExerciseService exerciseService;

    @Autowired
    public InitializeWorkout(UserService userService, WorkoutService workoutService, ExerciseService exerciseService) {
        this.userService = userService;
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!workoutService.findAllWorkouts().isEmpty()) {
            return;
        }

        submitWorkout("Workout for pushing movements", 40, getPushingExercises());
        submitWorkout("Workout for pulling movements", 50, getPullingExercises());
        submitWorkout("Workout for leg movements", 30, getLegExercises());
    }

    private void submitWorkout(String additionalInfo, Integer approximateTime, List<WorkoutExerciseEntry> exercises) {
        SubmitWorkoutRequest submitWorkoutRequest = SubmitWorkoutRequest.builder()
                .additionalInfo(additionalInfo)
                .approximateDuration(approximateTime)
                .exercises(exercises)
                .build();

        //For first user
        workoutService.submitWorkout(submitWorkoutRequest, userService.getFirstBaseUser());
        //For second user
        workoutService.submitWorkout(submitWorkoutRequest, userService.getSecondBaseUser());
    }

    private List<WorkoutExerciseEntry> getPushingExercises() {
        List<WorkoutExerciseEntry> entries = new ArrayList<>();
        entries.add(createExerciseEntry("Bench Press", 60.0, 8, 4));
        entries.add(createExerciseEntry("Shoulder Press", 40.0, 10, 3));
        entries.add(createExerciseEntry("Dips", 0.0, 12, 3));
        return entries;
    }

    private List<WorkoutExerciseEntry> getPullingExercises() {
        List<WorkoutExerciseEntry> entries = new ArrayList<>();
        entries.add(createExerciseEntry("Pull-Up", 0.0, 10, 4));
        entries.add(createExerciseEntry("Deadlift", 80.0, 6, 3));
        entries.add(createExerciseEntry("Bicep Curls", 20.0, 12, 3));
        return entries;
    }

    private List<WorkoutExerciseEntry> getLegExercises() {
        List<WorkoutExerciseEntry> entries = new ArrayList<>();
        entries.add(createExerciseEntry("Squat", 70.0, 8, 4));
        entries.add(createExerciseEntry("Lunges", 20.0, 12, 3));
        entries.add(createExerciseEntry("Burpees", 0.0, 20, 3));
        return entries;
    }

    private WorkoutExerciseEntry createExerciseEntry(String exerciseName, double weight, int reps, int sets) {
        Exercise exercise = exerciseService.findByName(exerciseName);
        return WorkoutExerciseEntry.builder()
                .exerciseId(exercise.getId().toString())
                .exerciseName(exerciseName)
                .addedWeight(weight)
                .reps(reps)
                .sets(sets)
                .mediaUrl(exercise.getMediaUrl())
                .build();
    }


}
