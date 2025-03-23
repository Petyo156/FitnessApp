package com.softuni.project.program;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.softuni.project.exception.ProgramDoesntExistException;
import com.softuni.project.mapper.Mapper;
import com.softuni.project.program.model.Program;
import com.softuni.project.program.repository.ProgramRepository;
import com.softuni.project.program.service.ProgramService;
import com.softuni.project.user.model.User;
import com.softuni.project.web.dto.ProgramFormRequest;
import com.softuni.project.web.dto.ViewProgramResponse;
import com.softuni.project.workout.service.WorkoutService;
import com.softuni.project.workoutschedule.service.WorkoutScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class ProgramServiceUTest {

    @Mock
    private ProgramRepository programRepository;

    @InjectMocks
    private ProgramService programService;

    @Test
    void getProgramById_shouldReturnProgramIfExists() {
        UUID programId = UUID.randomUUID();
        Program program = new Program();
        when(programRepository.findById(programId)).thenReturn(Optional.of(program));

        Program result = programService.getProgramById(programId);

        assertEquals(program, result);
        verify(programRepository, times(1)).findById(programId);
    }

    @Test
    void getProgramById_shouldThrowExceptionIfNotFound() {
        UUID programId = UUID.randomUUID();
        when(programRepository.findById(programId)).thenReturn(Optional.empty());

        assertThrows(ProgramDoesntExistException.class, () -> programService.getProgramById(programId));

        verify(programRepository, times(1)).findById(programId);
    }

    @Test
    void getProgramResponseByProgram_shouldReturnNullIfProgramIsNull() {
        ViewProgramResponse result = programService.getProgramResponseByProgram(null);

        assertNull(result);
    }


}
