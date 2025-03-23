package com.softuni.project.logexercise.service;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.log.model.Log;
import com.softuni.project.logexercise.model.LogExercise;
import com.softuni.project.logexercise.repository.LogExerciseRepository;
import com.softuni.project.web.dto.LogExerciseResponse;
import com.softuni.project.web.dto.ViewLoggedExerciseResponse;
import com.softuni.project.web.dto.WorkoutLogRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LogExerciseService {
    private final LogExerciseRepository logExerciseRepository;
    private final ExerciseService exerciseService;

    @Autowired
    public LogExerciseService(LogExerciseRepository logExerciseRepository, ExerciseService exerciseService) {
        this.logExerciseRepository = logExerciseRepository;
        this.exerciseService = exerciseService;
    }

    public void logInfoForExercises(Log record, WorkoutLogRequest workoutLogRequest) {
        for (LogExerciseResponse exercise : workoutLogRequest.getLoggedExercises()) {
            LogExercise logExercise = LogExercise.builder()
                    .log(record)
                    .exercise(exerciseService.getById(UUID.fromString(exercise.getExerciseId())))
                    .addedWeight(exercise.getAddedWeight())
                    .completedReps(exercise.getReps())
                    .completedSets(exercise.getSets())
                    .build();
            logExerciseRepository.save(logExercise);
        }
        log.info("Logged info for all exercises");
    }

    private List<LogExercise> getLogExerciseByLogId(UUID logId){
        return logExerciseRepository.findLoggedExercisesByLogId(logId);
    }

    public List<ViewLoggedExerciseResponse> getLoggedExercisesForLog(Log log) {
        List<LogExercise> logExercises = getLogExerciseByLogId(log.getId());

        List<ViewLoggedExerciseResponse> responses = new ArrayList<>();
        for(LogExercise logExercise : logExercises){
            ViewLoggedExerciseResponse exerciseResponse = new ViewLoggedExerciseResponse();

            Exercise exercise = exerciseService.getById(logExercise.getExercise().getId());
            exerciseResponse.setExerciseName(exercise.getName());

            exerciseResponse.setCompletedSets(logExercise.getCompletedSets());
            exerciseResponse.setCompletedReps(logExercise.getCompletedReps());
            exerciseResponse.setAddedWeight(logExercise.getAddedWeight());

            responses.add(exerciseResponse);
        }
        return responses;
    }
}
