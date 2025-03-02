package com.softuni.project.workout.service;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExercisesService;
import com.softuni.project.user.model.User;
import com.softuni.project.web.dto.SubmitWorkoutRequest;
import com.softuni.project.web.dto.ViewWorkoutResponse;
import com.softuni.project.web.dto.WorkoutExerciseEntry;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workout.repository.WorkoutRepository;
import com.softuni.project.workoutexercises.model.WorkoutExercise;
import com.softuni.project.workoutexercises.service.WorkoutExerciseService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.softuni.project.workout.model.Workout.*;

@Slf4j
@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseService workoutExerciseService;
    private final ExercisesService exercisesService;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutExerciseService workoutExerciseService, ExercisesService exercisesService) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.exercisesService = exercisesService;
    }

    @Transactional
    public void submitWorkout(SubmitWorkoutRequest submitWorkoutRequest, User user) {
        List<WorkoutExerciseEntry> enteredExercises = submitWorkoutRequest.getExercises();

        Workout workout = initializeWorkout(submitWorkoutRequest, user);
        workoutRepository.save(workout);
        log.info("Workout entity saved");

        for (WorkoutExerciseEntry enteredExercise : enteredExercises) {
            Exercise exercise = exercisesService.findByName(enteredExercise.getExerciseName());

            workoutExerciseService.saveEntry(enteredExercise.getReps(), enteredExercise.getSets(), enteredExercise.getAddedWeight(), exercise, workout);
            log.info("Given exercise info saved");
        }

        log.info("Workout submitted successfully");
    }

    private Workout initializeWorkout(SubmitWorkoutRequest submitWorkoutRequest, User user) {
        return builder()
                .additionalInfo(submitWorkoutRequest.getAdditionalInfo())
                .duration(submitWorkoutRequest.getApproximateDuration())
                .addedBy(user)
                .build();
    }

    public List<ViewWorkoutResponse> viewYourWorkouts(User user) {
        List<Workout> workoutsAddedBy = workoutRepository.findAllByAddedBy(user);
        log.info("Fetched workouts added by logged in user");

        List<ViewWorkoutResponse> responses = new ArrayList<>();

        for (Workout workout : workoutsAddedBy) {

            List<WorkoutExerciseEntry> workoutExerciseEntries = new ArrayList<>();

            List<WorkoutExercise> allWorkoutExerciseByWorkoutId =
                    workoutExerciseService.findAllByWorkoutId(workout.getId());

            for (WorkoutExercise workoutExercise : allWorkoutExerciseByWorkoutId) {
                WorkoutExerciseEntry workoutExerciseEntry = initializeWorkoutExerciseEntry(workoutExercise);
                workoutExerciseEntries.add(workoutExerciseEntry);
            }

            ViewWorkoutResponse viewWorkoutResponse = initializeViewWorkoutResponse(workout, workoutExerciseEntries);

            responses.add(viewWorkoutResponse);
        }
        log.info("Responses for every workout retrieved");

        return responses;
    }

    private ViewWorkoutResponse initializeViewWorkoutResponse(Workout workout, List<WorkoutExerciseEntry> workoutExerciseEntries) {
        return ViewWorkoutResponse.builder()
                .additionalInfo(workout.getAdditionalInfo())
                .approximateDuration(workout.getDuration())
                .exercises(workoutExerciseEntries)
                .build();
    }

    private WorkoutExerciseEntry initializeWorkoutExerciseEntry(WorkoutExercise workoutExercise) {
        return WorkoutExerciseEntry.builder()
                .exerciseName(workoutExercise.getExercise().getName())
                .sets(workoutExercise.getSets())
                .reps(workoutExercise.getReps())
                .addedWeight(workoutExercise.getAddedWeight())
                .mediaUrl(workoutExercise.getExercise().getMediaUrl())
                .build();
    }
}
