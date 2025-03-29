package com.softuni.project.log;

import com.softuni.project.exception.ExceptionMessages;
import com.softuni.project.exception.LogDoesntExistException;
import com.softuni.project.log.model.Log;
import com.softuni.project.log.repository.LogRepository;
import com.softuni.project.log.service.LogService;
import com.softuni.project.logexercise.service.LogExerciseService;
import com.softuni.project.user.model.User;
import com.softuni.project.web.dto.ViewLogResponse;
import com.softuni.project.web.dto.ViewLoggedExerciseResponse;
import com.softuni.project.web.dto.WorkoutLogRequest;
import com.softuni.project.workout.model.Workout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogServiceUTest {

    @Mock
    private LogRepository logRepository;

    @Mock
    private LogExerciseService logExerciseService;

    @InjectMocks
    private LogService logService;

    private UUID logId;
    private UUID userId;
    private User user;
    private Workout workout;
    private Log log;

    @BeforeEach
    void setUp() {
        logId = UUID.randomUUID();
        userId = UUID.randomUUID();
        user = User.builder().id(userId).build();
        workout = Workout.builder().id(UUID.randomUUID()).build();

        log = Log.builder()
                .id(logId)
                .user(user)
                .workout(workout)
                .completionDate(LocalDateTime.now())
                .build();
    }

    @Test
    void createLog_whenCalled_thenLogIsSavedAndExercisesAreLogged() {
        WorkoutLogRequest workoutLogRequest = new WorkoutLogRequest();

        when(logRepository.save(any(Log.class))).thenReturn(log);

        logService.createLog(user, workout, workoutLogRequest);

        verify(logRepository, times(1)).save(any(Log.class));
        verify(logExerciseService, times(1)).logInfoForExercises(any(Log.class), eq(workoutLogRequest));
    }

    @Test
    void getViewLogResponsesForUser_whenLogsExist_thenReturnList() {
        List<Log> logs = List.of(log);
        List<ViewLoggedExerciseResponse> loggedExercises = Arrays.asList(
                new ViewLoggedExerciseResponse(),
                new ViewLoggedExerciseResponse()
        );

        when(logRepository.findByUserIdOrderByCompletionDateDesc(userId)).thenReturn(logs);
        when(logExerciseService.getLoggedExercisesForLog(log)).thenReturn(loggedExercises);

        List<ViewLogResponse> responses = logService.getViewLogResponsesForUser(userId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(log.getId().toString(), responses.getFirst().getLogId());
        assertEquals(2, responses.getFirst().getLoggedExercises().size());

        verify(logRepository, times(1)).findByUserIdOrderByCompletionDateDesc(userId);
        verify(logExerciseService, times(1)).getLoggedExercisesForLog(log);
    }

    @Test
    void getById_whenLogExists_thenReturnLog() {
        when(logRepository.findById(logId)).thenReturn(Optional.of(log));

        Log result = logService.getById(logId);

        assertNotNull(result);
        assertEquals(logId, result.getId());

        verify(logRepository, times(1)).findById(logId);
    }

    @Test
    void getById_whenLogDoesNotExist_thenThrowException() {
        when(logRepository.findById(logId)).thenReturn(Optional.empty());

        LogDoesntExistException exception = assertThrows(
                LogDoesntExistException.class,
                () -> logService.getById(logId)
        );

        assertEquals(ExceptionMessages.LOG_DOESNT_EXIST, exception.getMessage());
        verify(logRepository, times(1)).findById(logId);
    }

    @Test
    void deleteLogById_whenLogExists_thenDeleteIt() {
        when(logRepository.findById(logId)).thenReturn(Optional.of(log));

        logService.deleteLogById(logId.toString());

        verify(logRepository, times(1)).deleteById(logId);
    }

    @Test
    void deleteLogById_whenLogDoesNotExist_thenThrowException() {
        when(logRepository.findById(logId)).thenReturn(Optional.empty());

        LogDoesntExistException exception = assertThrows(
                LogDoesntExistException.class,
                () -> logService.deleteLogById(logId.toString())
        );

        assertEquals(ExceptionMessages.LOG_DOESNT_EXIST, exception.getMessage());
        verify(logRepository, times(1)).findById(logId);
        verify(logRepository, never()).deleteById(any());
    }
}

