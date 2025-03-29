package com.softuni.project.excersise.service;

import com.softuni.project.exception.*;
import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.model.ExerciseStatus;
import com.softuni.project.excersise.repository.ExerciseRepository;
import com.softuni.project.mapper.Mapper;
import com.softuni.project.user.model.User;
import com.softuni.project.user.model.UserRole;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.SubmitExerciseRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;
    private final Mapper<SubmitExerciseRequest, Exercise> exerciseMapper;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, UserService userService, Mapper<SubmitExerciseRequest, Exercise> exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
        this.exerciseMapper = exerciseMapper;
    }

    public Exercise submitExercise(SubmitExerciseRequest submitExerciseRequest, User createdBy) {
        Optional<Exercise> exerciseOptional = exerciseRepository.findByName(submitExerciseRequest.getName());
        if (exerciseOptional.isPresent()) {
            log.warn("Exercise with name '{}' already exists", submitExerciseRequest.getName());

            throw new ExerciseAlreadyExistsException(ExceptionMessages.EXERCISE_ALREADY_EXISTS);
        }

        User user = userService.getById(createdBy.getId());
        Exercise exercise = exerciseMapper.map(submitExerciseRequest);
        exercise.setCreatedBy(user);

        if(user.getUserRole() == UserRole.ADMIN){
            exercise.setStatus(ExerciseStatus.APPROVED);
            exercise.setApprovedBy(user);
        }

        exerciseRepository.save(exercise);

        log.info("Successfully submitted exercise: {}", submitExerciseRequest.getName());
        return exercise;
    }

    public Exercise getById(UUID uuid) {

        return exerciseRepository.findById(uuid).orElseThrow(() -> {
            log.error("Exercise with ID '{}' does not exist", uuid);

            return new ExerciseDoesntExistException(ExceptionMessages.EXERCISE_DOESNT_EXIST);
        });
    }

    public Exercise getById(String uuid) {
        UUID exerciseId;
        try {
            exerciseId = UUID.fromString(uuid);
        } catch (Exception e) {
            throw new InvalidUuidFormatException(ExceptionMessages.INVALID_UUID_FORMAT);
        }
        return getById(exerciseId);
    }

    public void throwIfNotApproved(Exercise selectedExercise) {
        log.info("Checking approval status for exercise ID: {}", selectedExercise.getId());

        if (selectedExercise.getStatus() != ExerciseStatus.APPROVED) {
            log.warn("Exercise ID '{}' is not approved", selectedExercise.getId());

            throw new ExerciseNotApprovedException(ExceptionMessages.EXERCISE_NOT_APPROVED);
        }
    }

    public List<Exercise> findAllPendingExercises() {
        log.info("Fetch all pending exercises");
        return exerciseRepository.findByStatus(ExerciseStatus.PENDING);
    }

    public List<Exercise> findAllRejectedExercises() {
        log.info("Fetch all rejected exercises");
        return exerciseRepository.findByStatus(ExerciseStatus.REJECTED);
    }

    public List<Exercise> findAllApprovedExercises() {
        log.info("Fetch all approved exercises");
        return exerciseRepository.findByStatus(ExerciseStatus.APPROVED);
    }

    public void approveById(String uuid) {
        Exercise exercise = getById(uuid);
        exercise.setStatus(ExerciseStatus.APPROVED);
        exerciseRepository.save(exercise);

        log.info("Approved exercise with ID: {}", uuid);
    }

    public void rejectById(String uuid) {
        Exercise exercise = getById(uuid);
        exercise.setStatus(ExerciseStatus.REJECTED);
        exerciseRepository.save(exercise);

        log.info("Rejected exercise with ID: {}", uuid);
    }

    public void revokeById(String uuid) {
        Exercise exercise = getById(uuid);
        exercise.setStatus(ExerciseStatus.PENDING);
        exerciseRepository.save(exercise);

        log.info("Revoked exercise with ID: {}", uuid);
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
                () -> new ExerciseDoesntExistException(ExceptionMessages.EXERCISE_DOESNT_EXIST)
        );
    }

    public List<String> findAllApprovedExercisesNames() {
        return exerciseRepository.findAllExercisesNamesByStatus(ExerciseStatus.APPROVED);
    }

    @Transactional
    public void deleteAllRejectedExercises() {
        exerciseRepository.deleteAllByStatus(ExerciseStatus.REJECTED);
    }

    public List<Exercise> findAllExercises() {
        return exerciseRepository.findAll();
    }
}

