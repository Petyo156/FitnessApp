package com.softuni.project.log.service;

import com.softuni.project.exception.DomainException;
import com.softuni.project.log.model.Log;
import com.softuni.project.log.repository.LogRepository;
import com.softuni.project.logexercise.service.LogExerciseService;
import com.softuni.project.user.model.User;
import com.softuni.project.web.dto.ViewLogResponse;
import com.softuni.project.web.dto.ViewLoggedExerciseResponse;
import com.softuni.project.web.dto.WorkoutLogRequest;
import com.softuni.project.workout.model.Workout;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LogService {
    private final LogRepository logRepository;
    private final LogExerciseService logExerciseService;

    @Autowired
    public LogService(LogRepository logRepository, LogExerciseService logExerciseService) {
        this.logRepository = logRepository;
        this.logExerciseService = logExerciseService;
    }

    @Transactional
    public void createLog(User user, Workout workout, WorkoutLogRequest workoutLogRequest) {
        Log newLog = initializeLog(user, workout);
        Log record = logRepository.save(newLog);
        log.info("New log created");

        logExerciseService.logInfoForExercises(record, workoutLogRequest);
    }

    public List<ViewLogResponse> getViewLogResponsesForUser(UUID userId) {
        List<Log> logs = logRepository.findByUserIdOrderByCompletionDateDesc(userId);
        return initializeViewLogResponses(logs);
    }

    private List<ViewLogResponse> initializeViewLogResponses(List<Log> logs) {
        return logs.stream().map(log -> {
            ViewLogResponse logResponse = new ViewLogResponse();
            logResponse.setCompletionDate(log.getCompletionDate());
            logResponse.setLogId(log.getId().toString());

            List<ViewLoggedExerciseResponse> loggedExercises = logExerciseService.getLoggedExercisesForLog(log);
            logResponse.setLoggedExercises(loggedExercises);

            return logResponse;
        }).toList();
    }

    private Log initializeLog(User user, Workout workout) {
        return Log.builder()
                .user(user)
                .workout(workout)
                .completionDate(LocalDateTime.now())
                .build();
    }

    public Log getById(UUID id) {
        return logRepository.findById(id).orElseThrow(() -> {
            log.error("Exercise with ID '{}' does not exist", id);

            return new DomainException("Exercise with this id does not exist");
        });
    }

    public void deleteLogById(UUID uuid) {
        Log record = getById(uuid);

        log.info("Log with id '{}' has been deleted", uuid);
        logRepository.deleteById(record.getId());
    }
}
