package com.softuni.project.workout.service;

import com.softuni.project.exception.DomainException;
import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.user.model.User;
import com.softuni.project.web.dto.*;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workout.repository.WorkoutRepository;
import com.softuni.project.workoutexercise.model.WorkoutExercise;
import com.softuni.project.workoutexercise.service.WorkoutExerciseService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.softuni.project.workout.model.Workout.*;

@Service
@Slf4j
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseService workoutExerciseService;
    private final ExerciseService exerciseService;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutExerciseService workoutExerciseService, ExerciseService exerciseService) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.exerciseService = exerciseService;
    }

    @Transactional
    public void submitWorkout(SubmitWorkoutRequest submitWorkoutRequest, User user) {
        List<WorkoutExerciseEntry> enteredExercises = submitWorkoutRequest.getExercises();

        Workout workout = initializeWorkout(submitWorkoutRequest, user);
        workoutRepository.save(workout);
        log.info("Workout entity saved");

        for (WorkoutExerciseEntry enteredExercise : enteredExercises) {
            Exercise exercise = exerciseService.findByName(enteredExercise.getExerciseName());

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

    public List<ViewWorkoutResponse> getYourWorkouts(User user) {
        List<Workout> workoutsAddedBy = workoutRepository.findAllByAddedBy(user);
        log.info("Fetched workouts added by logged in user");

        List<ViewWorkoutResponse> responses = new ArrayList<>();

        for (Workout workout : workoutsAddedBy) {

            ViewWorkoutResponse viewWorkoutResponse = getViewWorkoutResponse(workout);
            responses.add(viewWorkoutResponse);
        }
        log.info("Responses for every workout retrieved");

        return responses;
    }

    public ViewWorkoutResponse getViewWorkoutResponse(Workout workout) {
        List<WorkoutExerciseEntry> workoutExerciseEntries = getWorkoutExerciseEntries(workout);
        return initializeViewWorkoutResponse(workout, workoutExerciseEntries);
    }

    public List<WorkoutExerciseEntry> getWorkoutExerciseEntries(Workout workout) {
        List<WorkoutExerciseEntry> workoutExerciseEntries = new ArrayList<>();

        List<WorkoutExercise> allWorkoutExerciseByWorkoutId =
                workoutExerciseService.findAllByWorkoutId(workout.getId());

        for (WorkoutExercise workoutExercise : allWorkoutExerciseByWorkoutId) {
            WorkoutExerciseEntry workoutExerciseEntry = initializeWorkoutExerciseEntry(workoutExercise);
            workoutExerciseEntries.add(workoutExerciseEntry);
        }
        return workoutExerciseEntries;
    }

    public Workout getById(UUID uuid) {

        return workoutRepository.findById(uuid).orElseThrow(() -> {
            log.error("Workout with ID '{}' does not exist", uuid);

            return new DomainException("Workout with this id does not exist");
        });
    }

    private ViewWorkoutResponse initializeViewWorkoutResponse(Workout workout, List<WorkoutExerciseEntry> workoutExerciseEntries) {
        return ViewWorkoutResponse.builder()
                .additionalInfo(workout.getAdditionalInfo())
                .approximateDuration(workout.getDuration())
                .exercises(workoutExerciseEntries)
                .workoutId(workout.getId().toString())
                .additionalInfo(workout.getAdditionalInfo())
                .approximateDuration(workout.getDuration())
                .build();
    }

    private WorkoutExerciseEntry initializeWorkoutExerciseEntry(WorkoutExercise workoutExercise) {
        return WorkoutExerciseEntry.builder()
                .exerciseName(workoutExercise.getExercise().getName())
                .sets(workoutExercise.getSets())
                .reps(workoutExercise.getReps())
                .addedWeight(workoutExercise.getAddedWeight())
                .mediaUrl(workoutExercise.getExercise().getMediaUrl())
                .exerciseId(workoutExercise.getExercise().getId().toString())
                .build();
    }

    public WorkoutLogRequest initializeWorkoutLogRequest(ViewWorkoutResponse workoutResponse) {
        WorkoutLogRequest workoutLogRequest = new WorkoutLogRequest();
        List<LogExerciseRequest> loggedExercises = new ArrayList<>();

        workoutResponse.getExercises().forEach(exercise -> {
            LogExerciseRequest logExerciseRequest = new LogExerciseRequest();
            logExerciseRequest.setExerciseId(exercise.getExerciseId());
            loggedExercises.add(logExerciseRequest);
        });
        workoutLogRequest.setLoggedExercises(loggedExercises);

        log.info("WorkoutLogRequest created");
        return workoutLogRequest;
    }
}
