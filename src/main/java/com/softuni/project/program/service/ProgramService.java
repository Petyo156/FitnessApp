package com.softuni.project.program.service;

import com.softuni.project.program.model.Program;
import com.softuni.project.program.model.Status;
import com.softuni.project.program.repository.ProgramRepository;
import com.softuni.project.user.model.User;
import com.softuni.project.web.dto.ProgramFormRequest;
import com.softuni.project.workoutschedule.service.WorkoutScheduleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProgramService {
    private final ProgramRepository programRepository;
    private final WorkoutScheduleService workoutScheduleService;

    @Autowired
    public ProgramService(ProgramRepository programRepository, WorkoutScheduleService workoutScheduleService) {
        this.programRepository = programRepository;
        this.workoutScheduleService = workoutScheduleService;
    }

    @Transactional
    public void createProgram(User user, ProgramFormRequest programFormRequest) {
        Program program = initializeProgram(user, programFormRequest);

        programRepository.save(program);

        workoutScheduleService.createWorkoutSchedules(programFormRequest, program);

    }

    private Program initializeProgram(User user, ProgramFormRequest programFormRequest) {
        return Program.builder()
                .createdOn(LocalDateTime.now())
                .name(programFormRequest.getName())
                .description(programFormRequest.getDescription())
                .difficulty(programFormRequest.getDifficulty())
                .status(Status.INACTIVE)
                .createdOn(LocalDateTime.now())
                .sharedWithOthers(programFormRequest.getShared())
                .user(user)
                .build();
    }
}
