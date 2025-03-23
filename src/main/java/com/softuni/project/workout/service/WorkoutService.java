package com.softuni.project.workout.service;

import com.softuni.project.exception.ExceptionMessages;
import com.softuni.project.exception.WorkoutDoesntExistException;
import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.mapper.Mapper;
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

@Service
@Slf4j
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseService workoutExerciseService;
    private final ExerciseService exerciseService;

    private final Mapper<SubmitWorkoutRequest, Workout> workoutMapper;
    private final Mapper<Workout, ViewWorkoutResponse> workoutResponseMapper;
    private final Mapper<WorkoutExercise, WorkoutExerciseEntry> workoutExerciseEntryMapper;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutExerciseService workoutExerciseService, ExerciseService exerciseService, Mapper<SubmitWorkoutRequest, Workout> workoutMapper, Mapper<Workout, ViewWorkoutResponse> workoutResponseMapper, Mapper<WorkoutExercise, WorkoutExerciseEntry> workoutExerciseEntryMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.exerciseService = exerciseService;
        this.workoutMapper = workoutMapper;
        this.workoutResponseMapper = workoutResponseMapper;
        this.workoutExerciseEntryMapper = workoutExerciseEntryMapper;
    }

    @Transactional
    public void submitWorkout(SubmitWorkoutRequest submitWorkoutRequest, User user) {
        List<WorkoutExerciseEntry> enteredExercises = submitWorkoutRequest.getExercises();

        Workout workout = workoutMapper.map(submitWorkoutRequest);
        workout.setAddedBy(user);
        workoutRepository.save(workout);
        log.info("Workout entity saved");

        for (WorkoutExerciseEntry enteredExercise : enteredExercises) {
            Exercise exercise = exerciseService.findByName(enteredExercise.getExerciseName());

            workoutExerciseService.saveEntry(enteredExercise.getReps(), enteredExercise.getSets(), enteredExercise.getAddedWeight(), exercise, workout);
            log.info("Given exercise info saved");
        }

        log.info("Workout submitted successfully");
    }

    public List<ViewWorkoutResponse> getWorkoutsForUser(User user) {
        List<Workout> workoutsAddedBy = workoutRepository.findAllByAddedBy(user);
        log.info("Fetched workouts added by logged in user");

        List<ViewWorkoutResponse> responses = new ArrayList<>();

        for (Workout workout : workoutsAddedBy) {

            ViewWorkoutResponse viewWorkoutResponse = getViewWorkoutResponseByWorkout(workout);
            responses.add(viewWorkoutResponse);
        }
        log.info("Responses for every workout retrieved");

        return responses;
    }

    public ViewWorkoutResponse getViewWorkoutResponseByWorkout(Workout workout) {
        List<WorkoutExerciseEntry> workoutExerciseEntries = getWorkoutExerciseEntries(workout);
        ViewWorkoutResponse viewWorkoutResponse = workoutResponseMapper.map(workout);
        viewWorkoutResponse.setExercises(workoutExerciseEntries);

        return viewWorkoutResponse;
    }

    public List<WorkoutExerciseEntry> getWorkoutExerciseEntries(Workout workout) {
        List<WorkoutExerciseEntry> workoutExerciseEntries = new ArrayList<>();

        List<WorkoutExercise> allWorkoutExerciseByWorkoutId =
                workoutExerciseService.findAllByWorkoutId(workout.getId());

        for (WorkoutExercise workoutExercise : allWorkoutExerciseByWorkoutId) {
            WorkoutExerciseEntry workoutExerciseEntry = workoutExerciseEntryMapper.map(workoutExercise);
            workoutExerciseEntries.add(workoutExerciseEntry);
        }
        return workoutExerciseEntries;
    }

    public Workout getById(UUID uuid) {

        return workoutRepository.findById(uuid).orElseThrow(() -> {
            log.error("Workout with ID '{}' does not exist", uuid);

            return new WorkoutDoesntExistException(ExceptionMessages.WORKOUT_DOESNT_EXIST);
        });
    }

    public WorkoutLogRequest initializeWorkoutLogRequest(ViewWorkoutResponse workoutResponse) {
        WorkoutLogRequest workoutLogRequest = new WorkoutLogRequest();
        List<LogExerciseResponse> loggedExercises = new ArrayList<>();

        workoutResponse.getExercises().forEach(exercise -> {
            LogExerciseResponse logExerciseRequest = new LogExerciseResponse();
            logExerciseRequest.setExerciseId(exercise.getExerciseId());
            loggedExercises.add(logExerciseRequest);
        });
        workoutLogRequest.setLoggedExercises(loggedExercises);

        log.info("WorkoutLogRequest created");
        return workoutLogRequest;
    }
}
