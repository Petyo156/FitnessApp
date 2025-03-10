package com.softuni.project.program.service;

import com.softuni.project.program.model.Program;
import com.softuni.project.program.model.Status;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
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

    public List<ViewProgramResponse> getAllProgramsByUser(User user) {
        List<Program> programs = programRepository.findAllByUser_Id(user.getId());
        return getPrograms(programs);
    }

    public List<ViewProgramResponse> getAllSharedProgramsByAllOtherUsers(User user) {
        List<Program> programs = programRepository.findSharedProgramsExcludingUser(user.getId());
        return getPrograms(programs);
    }

    public List<ViewProgramResponse> getAllSharedProgramsByUser(User user) {
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
            Workout workout = workoutService.findById(workoutSchedule.getWorkout().getId());
            List<WorkoutExerciseEntry> workoutExerciseEntries = workoutService.getWorkoutExerciseEntries(workout);

            ViewWorkoutResponse workoutResponse = initializeWorkoutResponse(workoutSchedule, workout, workoutExerciseEntries);
            workoutResponses.add(workoutResponse);
        }

        workoutResponses.sort(Comparator.comparing(w -> w.getDayOfWeek().ordinal()));
        programResponse.setWorkouts(workoutResponses);
    }


    private ViewWorkoutResponse initializeWorkoutResponse(WorkoutSchedule workoutSchedule, Workout workout, List<WorkoutExerciseEntry> workoutExerciseEntries) {
        return ViewWorkoutResponse.builder()
                .workoutId(workout.getId())
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
                .status(program.getStatus())
                .createdOn(program.getCreatedOn())
                .sharedWithOthers(program.getSharedWithOthers())
                .workouts(new ArrayList<>())
                .addedByUsername(program.getUser().getUsername())
                .build();
    }
}

