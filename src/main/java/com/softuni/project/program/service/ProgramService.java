package com.softuni.project.program.service;

import com.softuni.project.exception.ExceptionMessages;
import com.softuni.project.exception.ProgramDoesntExistException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProgramService {
    private final ProgramRepository programRepository;
    private final WorkoutScheduleService workoutScheduleService;
    private final WorkoutService workoutService;

    @Autowired
    public ProgramService(ProgramRepository programRepository, WorkoutScheduleService workoutScheduleService, WorkoutService workoutService) {
        this.programRepository = programRepository;
        this.workoutScheduleService = workoutScheduleService;
        this.workoutService = workoutService;
    }

    @Transactional
    public void createProgram(User user, ProgramFormRequest programFormRequest) {
        Program program = initializeProgram(user, programFormRequest);

        programRepository.save(program);

        workoutScheduleService.createWorkoutSchedules(programFormRequest, program);
        log.info("New program created successfully");
    }

    private Program initializeProgram(User user, ProgramFormRequest programFormRequest) {
        return Program.builder()
                .createdOn(LocalDateTime.now())
                .name(programFormRequest.getName())
                .description(programFormRequest.getDescription())
                .difficulty(programFormRequest.getDifficulty())
                .createdOn(LocalDateTime.now())
                .sharedWithOthers(programFormRequest.getShared())
                .user(user)
                .build();
    }

    public List<ViewProgramResponse> getAllProgramsByUser(User user) {
        List<Program> programs = programRepository.findAllByUser_Id(user.getId());
        return getPrograms(programs);
    }

    public List<ViewProgramResponse> getAllSharedProgramsByAllOtherUsers(User user) {
        log.info("Fetching all programs shared by all other users");

        List<Program> programs = programRepository.findSharedProgramsExcludingUser(user.getId());
        return getPrograms(programs);
    }

    public List<ViewProgramResponse> getAllSharedProgramsByUser(User user) {
        log.info("Fetching all programs shared by given user");

        List<Program> programs = programRepository.getSharedProgramsByUser(user.getId());
        return getPrograms(programs);
    }

    private List<ViewProgramResponse> getPrograms(List<Program> programs) {
        List<ViewProgramResponse> responses = new ArrayList<>();

        for (Program program : programs) {
            ViewProgramResponse programResponse = initializeProgramResponse(program);
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

    private ViewProgramResponse initializeProgramResponse(Program program) {
        return ViewProgramResponse.builder()
                .name(program.getName())
                .description(program.getDescription())
                .difficulty(program.getDifficulty())
                .createdOn(program.getCreatedOn())
                .sharedWithOthers(program.getSharedWithOthers())
                .workouts(new ArrayList<>())
                .addedByUsername(program.getUser().getUsername())
                .id(program.getId().toString())
                .build();
    }

    public Program getProgramById(UUID id) {
        return programRepository.findById(id).orElseThrow(() -> {

            log.error("Program with ID '{}' does not exist", id);
            return new ProgramDoesntExistException(ExceptionMessages.PROGRAM_DOESNT_EXIST);
        });
    }

    public ViewProgramResponse getProgramResponseByProgram(Program program) {
        if(null == program){
            return null;
        }
        ViewProgramResponse viewProgramResponse = initializeProgramResponse(program);
        setWorkoutsAndExercisesForProgramResponse(program, viewProgramResponse);
        return viewProgramResponse;
    }

}

