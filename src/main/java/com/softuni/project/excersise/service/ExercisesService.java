package com.softuni.project.excersise.service;

import com.softuni.project.exception.DomainException;
import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.model.ExerciseStatus;
import com.softuni.project.excersise.repository.ExercisesRepository;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.SubmitExerciseRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ExercisesService {
    private final ExercisesRepository exercisesRepository;
    private final UserService userService;

    @Autowired
    public ExercisesService(ExercisesRepository exercisesRepository, UserService userService) {
        this.exercisesRepository = exercisesRepository;
        this.userService = userService;
    }

    public void submitExercise(SubmitExerciseRequest submitExerciseRequest, AuthenticationMetadata authenticationMetadata) {
        log.info("Attempting to submit exercise: {}", submitExerciseRequest.getName());

        Optional<Exercise> exerciseOptional = exercisesRepository.findByName(submitExerciseRequest.getName());
        if (exerciseOptional.isPresent()) {
            log.warn("Exercise with name '{}' already exists", submitExerciseRequest.getName());

            throw new DomainException("Exercise with this name already exists");
        }

        User user = userService.getById(authenticationMetadata.getId());
        Exercise exercise = initializeExercise(submitExerciseRequest, user);
        exercisesRepository.save(exercise);

        log.info("Successfully submitted exercise: {}", submitExerciseRequest.getName());
    }

    private Exercise initializeExercise(SubmitExerciseRequest submitExerciseRequest, User user) {
        return Exercise.builder()
                .name(submitExerciseRequest.getName())
                .description(submitExerciseRequest.getDescription())
                .muscleGroups(submitExerciseRequest.getMuscleGroups())
                .mediaUrl(submitExerciseRequest.getMediaUrl())
                .difficulty(submitExerciseRequest.getDifficulty())
                .createdBy(user)
                .status(ExerciseStatus.PENDING)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public List<Exercise> findAll() {
        log.info("Retrieving all exercises");

        return exercisesRepository.findAll();
    }

    public Exercise findById(UUID uuid) {
        log.info("Fetching exercise with ID: {}", uuid);

        return exercisesRepository.findById(uuid).orElseThrow(() -> {
            log.error("Exercise with ID '{}' does not exist", uuid);

            return new DomainException("Exercise with this id does not exist");
        });
    }

    public void throwIfNotApproved(Exercise selectedExercise) {
        log.info("Checking approval status for exercise ID: {}", selectedExercise.getId());

        if (selectedExercise.getStatus() != ExerciseStatus.APPROVED) {
            log.warn("Exercise ID '{}' is not approved", selectedExercise.getId());

            throw new DomainException("You cannot visit this exercise page");
        }
    }

    public List<Exercise> findAllPendingExercises() {

        return exercisesRepository.findByStatus(ExerciseStatus.PENDING);
    }

    public List<Exercise> findAllRejectedExercises(){

        return exercisesRepository.findByStatus(ExerciseStatus.REJECTED);
    }

    public List<Exercise> findAllApprovedExercises() {

        return exercisesRepository.findByStatus(ExerciseStatus.APPROVED);
    }


    public void deleteById(UUID uuid) {
        exercisesRepository.deleteById(uuid);

        log.info("Deleted exercise with ID: {}", uuid);
    }

    public void approveById(UUID uuid) {
        Exercise exercise = findById(uuid);
        exercise.setStatus(ExerciseStatus.APPROVED);
        exercisesRepository.save(exercise);

        log.info("Approved exercise with ID: {}", uuid);
    }

    public void rejectById(UUID uuid) {
        Exercise exercise = findById(uuid);
        exercise.setStatus(ExerciseStatus.REJECTED);
        exercisesRepository.save(exercise);

        log.info("Rejected exercise with ID: {}", uuid);
    }
}

