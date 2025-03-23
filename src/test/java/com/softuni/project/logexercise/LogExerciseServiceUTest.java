package com.softuni.project.logexercise;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.log.model.Log;
import com.softuni.project.logexercise.model.LogExercise;
import com.softuni.project.logexercise.repository.LogExerciseRepository;
import com.softuni.project.logexercise.service.LogExerciseService;
import com.softuni.project.web.dto.LogExerciseResponse;
import com.softuni.project.web.dto.ViewLoggedExerciseResponse;
import com.softuni.project.web.dto.WorkoutLogRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogExerciseServiceUTest {

    @InjectMocks
    private LogExerciseService logExerciseService;

    @Mock
    private LogExerciseRepository logExerciseRepository;

    @Mock
    private ExerciseService exerciseService;

    private static final String EXERCISE_ID = UUID.randomUUID().toString();
    private static final String LOG_ID = UUID.randomUUID().toString();

    @Test
    void logInfoForExercises_whenCalled_savesLogExercise() {
        // Given
        LogExerciseResponse response = new LogExerciseResponse();
        response.setExerciseId(EXERCISE_ID);
        response.setReps(10);
        response.setSets(3);
        response.setAddedWeight(50.0);

        WorkoutLogRequest workoutLogRequest = new WorkoutLogRequest();
        workoutLogRequest.setLoggedExercises(List.of(response));

        Log log = new Log();
        log.setId(UUID.fromString(LOG_ID));

        Exercise exercise = new Exercise();
        exercise.setId(UUID.fromString(EXERCISE_ID));

        when(exerciseService.getById(UUID.fromString(EXERCISE_ID))).thenReturn(exercise);

        logExerciseService.logInfoForExercises(log, workoutLogRequest);

        verify(logExerciseRepository, times(1)).save(any(LogExercise.class));
    }

    @Test
    void getLoggedExercisesForLog_shouldReturnViewLoggedExerciseResponse() {
        UUID logId = UUID.randomUUID();
        LogExercise logExercise = new LogExercise();
        logExercise.setExercise(new Exercise());
        logExercise.getExercise().setId(UUID.randomUUID());
        logExercise.setCompletedReps(10);
        logExercise.setCompletedSets(3);
        logExercise.setAddedWeight(50.0);

        when(logExerciseRepository.findLoggedExercisesByLogId(logId)).thenReturn(List.of(logExercise));

        Exercise exercise = new Exercise();
        exercise.setId(logExercise.getExercise().getId());
        exercise.setName("Push-up");

        when(exerciseService.getById(logExercise.getExercise().getId())).thenReturn(exercise);

        Log log = new Log();
        log.setId(logId);

        List<ViewLoggedExerciseResponse> result = logExerciseService.getLoggedExercisesForLog(log);

        verify(logExerciseRepository, times(1)).findLoggedExercisesByLogId(logId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Push-up", result.getFirst().getExerciseName());
        assertEquals(10, result.getFirst().getCompletedReps());
        assertEquals(3, result.getFirst().getCompletedSets());
        assertEquals(50.0, result.getFirst().getAddedWeight());
    }

}
