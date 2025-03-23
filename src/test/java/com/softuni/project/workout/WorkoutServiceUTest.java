package com.softuni.project.workout;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.softuni.project.exception.WorkoutDoesntExistException;
import com.softuni.project.web.dto.*;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workout.repository.WorkoutRepository;
import com.softuni.project.workout.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceUTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

    @Test
    void getById_shouldReturnWorkoutIfExists() {
        UUID workoutId = UUID.randomUUID();
        Workout workout = new Workout();
        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(workout));

        Workout result = workoutService.getById(workoutId);

        assertEquals(workout, result);
        verify(workoutRepository, times(1)).findById(workoutId);
    }

    @Test
    void getById_shouldThrowExceptionIfNotFound() {
        UUID workoutId = UUID.randomUUID();
        when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        assertThrows(WorkoutDoesntExistException.class, () -> workoutService.getById(workoutId));
    }

    @Test
    void initializeWorkoutLogRequest_shouldReturnCorrectLogRequest() {
        ViewWorkoutResponse workoutResponse = new ViewWorkoutResponse();
        WorkoutExerciseEntry entry = new WorkoutExerciseEntry();
        entry.setExerciseId(UUID.randomUUID().toString());
        workoutResponse.setExercises(List.of(entry));

        WorkoutLogRequest logRequest = workoutService.initializeWorkoutLogRequest(workoutResponse);

        assertEquals(1, logRequest.getLoggedExercises().size());
        assertEquals(entry.getExerciseId(), logRequest.getLoggedExercises().getFirst().getExerciseId());
    }
}
