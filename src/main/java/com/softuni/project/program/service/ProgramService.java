package com.softuni.project.program.service;

import com.softuni.project.exception.ExceptionMessages;
import com.softuni.project.exception.ProgramDoesntExistException;
import com.softuni.project.mapper.Mapper;
import com.softuni.project.program.model.Program;
import com.softuni.project.program.repository.ProgramRepository;
import com.softuni.project.user.model.User;
import com.softuni.project.web.dto.ProgramFormRequest;
import com.softuni.project.web.dto.ViewProgramResponse;
import com.softuni.project.web.dto.ViewWorkoutResponse;
import com.softuni.project.web.dto.WorkoutExerciseEntry;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workout.service.WorkoutService;
import com.softuni.project.workoutschedule.model.WorkoutSchedule;
import com.softuni.project.workoutschedule.service.WorkoutScheduleService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ProgramService {
    private final ProgramRepository programRepository;
    private final WorkoutScheduleService workoutScheduleService;
    private final WorkoutService workoutService;
    private final Mapper<Program, ViewProgramResponse> programMapper;
    private final Mapper<ProgramFormRequest, Program> requestProgramMapper;

    @Autowired
    public ProgramService(ProgramRepository programRepository, WorkoutScheduleService workoutScheduleService, WorkoutService workoutService, Mapper<Program, ViewProgramResponse> programMapper, Mapper<ProgramFormRequest, Program> requestProgramMapper) {
        this.programRepository = programRepository;
        this.workoutScheduleService = workoutScheduleService;
        this.workoutService = workoutService;
        this.programMapper = programMapper;
        this.requestProgramMapper = requestProgramMapper;
    }

    @Transactional
    public void createProgram(User user, ProgramFormRequest programFormRequest) {
        Program program = requestProgramMapper.map(programFormRequest);
        program.setUser(user);
        programRepository.save(program);

        workoutScheduleService.createWorkoutSchedules(programFormRequest, program);
        log.info("New program created successfully");
    }

    public List<Program> getAllProgramsEntitiesByUser(User user){
        return programRepository.findAllByUser_Id(user.getId());
    }

    public List<ViewProgramResponse> getAllProgramsByUser(User user) {
        List<Program> programs = programRepository.findAllByUser_Id(user.getId());
        return getProgramsResponses(programs);
    }

    public List<ViewProgramResponse> getAllSharedProgramsByAllOtherUsers(User user) {
        log.info("Fetching all programs shared by all other users");

        List<Program> programs = programRepository.findSharedProgramsExcludingUser(user.getId());
        return getProgramsResponses(programs);
    }

    public List<ViewProgramResponse> getAllSharedProgramsByUser(User user) {
        log.info("Fetching all programs shared by given user");

        List<Program> programs = programRepository.getSharedProgramsByUser(user.getId());
        return getProgramsResponses(programs);
    }

    public Program getById(UUID id) {
        return programRepository.findById(id).orElseThrow(() -> {

            log.error("Program with ID '{}' does not exist", id);
            return new ProgramDoesntExistException(ExceptionMessages.PROGRAM_DOESNT_EXIST);
        });
    }

    public ViewProgramResponse getProgramResponseByProgram(Program program) {
        if (null == program) {
            return null;
        }
        ViewProgramResponse viewProgramResponse = programMapper.map(program);
        setWorkoutsAndExercisesForProgramResponse(program, viewProgramResponse);
        return viewProgramResponse;
    }

    public List<ViewProgramResponse> getAllLikedPrograms(List<String> allLikedProgramsIds) {
        List<Program> likedPrograms = new ArrayList<>();
        for (String id : allLikedProgramsIds) {
            programRepository.findById(UUID.fromString(id)).ifPresent(likedPrograms::add);
        }
        return getProgramsResponses(likedPrograms);
    }

    private List<ViewProgramResponse> getProgramsResponses(List<Program> programs) {
        List<ViewProgramResponse> responses = new ArrayList<>();

        for (Program program : programs) {
            ViewProgramResponse programResponse = programMapper.map(program);
            setWorkoutsAndExercisesForProgramResponse(program, programResponse);
            responses.add(programResponse);
        }

        return responses;
    }

    private void setWorkoutsAndExercisesForProgramResponse(Program program, ViewProgramResponse programResponse) {
        List<WorkoutSchedule> workoutSchedules = workoutScheduleService.getAllByProgramId(program.getId());
        List<ViewWorkoutResponse> workoutResponses = new ArrayList<>();

        for (WorkoutSchedule workoutSchedule : workoutSchedules) {
            Workout workout = workoutService.getById(workoutSchedule.getWorkout().getId());
            List<WorkoutExerciseEntry> workoutExerciseEntries = workoutService.getWorkoutExerciseEntries(workout);

            ViewWorkoutResponse workoutResponse = initializeWorkoutResponse(workoutSchedule, workout, workoutExerciseEntries);
            workoutResponses.add(workoutResponse);
        }

        workoutResponses.sort(Comparator.comparing(w -> w.getDayOfWeek().ordinal()));
        programResponse.setWorkouts(workoutResponses);
    }

    private ViewWorkoutResponse initializeWorkoutResponse(WorkoutSchedule workoutSchedule, Workout workout, List<WorkoutExerciseEntry> workoutExerciseEntries) {
        return ViewWorkoutResponse.builder()
                .workoutId(workout.getId().toString())
                .additionalInfo(workout.getAdditionalInfo())
                .approximateDuration(workout.getDuration())
                .dayOfWeek(workoutSchedule.getDayOfWeek())
                .exercises(workoutExerciseEntries)
                .build();
    }

    public List<Program> findAllPrograms() {
        return programRepository.findAll();
    }
}

