package com.softuni.project.workoutschedule;

import com.softuni.project.exception.NoSetWorkoutForProgramException;
import com.softuni.project.program.model.Program;
import com.softuni.project.web.dto.ProgramFormRequest;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workout.service.WorkoutService;
import com.softuni.project.workoutschedule.model.WorkoutSchedule;
import com.softuni.project.workoutschedule.repository.WorkoutScheduleRepository;
import com.softuni.project.workoutschedule.service.WorkoutScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutScheduleServiceUTest {

    @Mock
    private WorkoutScheduleRepository workoutScheduleRepository;

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private WorkoutScheduleService workoutScheduleService;

    @Test
    void createWorkoutSchedules_shouldCreateSchedulesSuccessfully() {
        ProgramFormRequest programFormRequest = new ProgramFormRequest();
        programFormRequest.setMondayWorkoutId(UUID.randomUUID().toString());
        programFormRequest.setTuesdayWorkoutId(UUID.randomUUID().toString());

        Program program = new Program();
        Workout workout1 = new Workout();
        Workout workout2 = new Workout();

        when(workoutService.getById(any(UUID.class))).thenReturn(workout1, workout2);

        workoutScheduleService.createWorkoutSchedules(programFormRequest, program);

        verify(workoutService, times(2)).getById(any(UUID.class));
        verify(workoutScheduleRepository, times(2)).save(any(WorkoutSchedule.class));
    }

    @Test
    void createWorkoutSchedules_shouldThrowExceptionWhenNoWorkoutIsSet() {
        ProgramFormRequest programFormRequest = new ProgramFormRequest();
        Program program = new Program();

        assertThrows(NoSetWorkoutForProgramException.class, () -> workoutScheduleService.createWorkoutSchedules(programFormRequest, program));
        verify(workoutScheduleRepository, never()).save(any(WorkoutSchedule.class));
    }

    @Test
    void getAllByProgramId_shouldReturnWorkoutSchedules() {
        UUID programId = UUID.randomUUID();
        List<WorkoutSchedule> mockSchedules = List.of(new WorkoutSchedule());

        when(workoutScheduleRepository.findAllByProgram_Id(programId)).thenReturn(mockSchedules);

        List<WorkoutSchedule> result = workoutScheduleService.getAllByProgramId(programId);

        assertEquals(1, result.size());
        verify(workoutScheduleRepository, times(1)).findAllByProgram_Id(programId);
    }
}
