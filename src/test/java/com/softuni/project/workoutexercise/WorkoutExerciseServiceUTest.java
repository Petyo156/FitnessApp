package com.softuni.project.workoutexercise;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workoutexercise.model.WorkoutExercise;
import com.softuni.project.workoutexercise.repository.WorkoutExerciseRepository;
import com.softuni.project.workoutexercise.service.WorkoutExerciseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WorkoutExerciseServiceUTest {

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @InjectMocks
    private WorkoutExerciseService workoutExerciseService;

    @Test
    void saveEntry_shouldSaveWorkoutExercise() {
        int reps = 10;
        int sets = 3;
        double addedWeight = 20.0;
        Exercise exercise = new Exercise();
        Workout workout = new Workout();

        workoutExerciseService.saveEntry(reps, sets, addedWeight, exercise, workout);

        verify(workoutExerciseRepository, times(1)).save(any(WorkoutExercise.class));
    }

    @Test
    void findAllByWorkoutId_shouldReturnWorkoutExercises() {
        UUID workoutId = UUID.randomUUID();
        List<WorkoutExercise> mockWorkoutExercises = List.of(new WorkoutExercise());
        when(workoutExerciseRepository.findAllByWorkout_Id(workoutId)).thenReturn(mockWorkoutExercises);

        List<WorkoutExercise> result = workoutExerciseService.findAllByWorkoutId(workoutId);

        assertEquals(1, result.size());
        verify(workoutExerciseRepository, times(1)).findAllByWorkout_Id(workoutId);
    }
}
