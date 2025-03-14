package com.softuni.project.excersise.service;

import com.softuni.project.exception.DomainException;
import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.model.ExerciseStatus;
import com.softuni.project.excersise.repository.ExerciseRepository;
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
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, UserService userService) {
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
    }

    public void submitExercise(SubmitExerciseRequest submitExerciseRequest, AuthenticationMetadata authenticationMetadata) {
        log.info("Attempting to submit exercise: {}", submitExerciseRequest.getName());

        Optional<Exercise> exerciseOptional = exerciseRepository.findByName(submitExerciseRequest.getName());
        if (exerciseOptional.isPresent()) {
            log.warn("Exercise with name '{}' already exists", submitExerciseRequest.getName());

            throw new DomainException("Exercise with this name already exists");
        }

        User user = userService.getById(authenticationMetadata.getId());
        Exercise exercise = initializeExercise(submitExerciseRequest, user);
        exerciseRepository.save(exercise);

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

        return exerciseRepository.findAll();
    }

    public Exercise getById(UUID uuid) {

        return exerciseRepository.findById(uuid).orElseThrow(() -> {
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

        return exerciseRepository.findByStatus(ExerciseStatus.PENDING);
    }

    public List<Exercise> findAllRejectedExercises() {

        return exerciseRepository.findByStatus(ExerciseStatus.REJECTED);
    }

    public List<Exercise> findAllApprovedExercises() {

        return exerciseRepository.findByStatus(ExerciseStatus.APPROVED);
    }

    public void approveById(UUID uuid) {
        Exercise exercise = getById(uuid);
        exercise.setStatus(ExerciseStatus.APPROVED);
        exerciseRepository.save(exercise);

        log.info("Approved exercise with ID: {}", uuid);
    }

    public void rejectById(UUID uuid) {
        Exercise exercise = getById(uuid);
        exercise.setStatus(ExerciseStatus.REJECTED);
        exerciseRepository.save(exercise);

        log.info("Rejected exercise with ID: {}", uuid);
    }

    public List<Exercise> findAllApprovedExercisesByUserId(UUID id) {
        return exerciseRepository.findByStatusAndCreatedBy_Id(ExerciseStatus.APPROVED, id);
    }

    public List<Exercise> findAllPendingExercisesByUserId(UUID id) {
        return exerciseRepository.findByStatusAndCreatedBy_Id(ExerciseStatus.PENDING, id);
    }

    public List<Exercise> findAllRejectedExercisesByUserId(UUID id) {
        return exerciseRepository.findByStatusAndCreatedBy_Id(ExerciseStatus.REJECTED, id);
    }

    public Exercise findByName(String name) {
        return exerciseRepository.findByName(name).orElseThrow(
                () -> new DomainException("Exercise with name '" + name + "' does not exist")
        );
    }

    public List<String> findAllApprovedExercisesNames() {
        return exerciseRepository.findAllExercisesNamesByStatus(ExerciseStatus.APPROVED);
    }
}

