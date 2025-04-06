package com.softuni.project.config;

import com.softuni.project.program.model.Difficulty;
import com.softuni.project.program.service.ProgramService;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.ProgramFormRequest;
import com.softuni.project.workout.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(5)
@Component
public class InitializeProgram implements CommandLineRunner {
    private final UserService userService;

    private final ProgramService programService;

    private final WorkoutService workoutService;

    @Autowired
    public InitializeProgram(UserService userService, ProgramService programService, WorkoutService workoutService) {
        this.userService = userService;
        this.programService = programService;
        this.workoutService = workoutService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!programService.findAllPrograms().isEmpty()) {
            return;
        }

        List<String> workoutIdsByFirstBaseUser = workoutService.getWorkoutIdsByFirstBaseUser();
        List<String> workoutIdsBySecondBaseUser = workoutService.getWorkoutIdsBySecondBaseUser();

        //For first user
        ProgramFormRequest request1 = ProgramFormRequest.builder()
                .name("First base program")
                .description("This is a full body program following push-pull-legs.")
                .difficulty(Difficulty.MEDIUM)
                .shared(true)
                .mondayWorkoutId(workoutIdsByFirstBaseUser.get(0))
                .wednesdayWorkoutId(workoutIdsByFirstBaseUser.get(1))
                .fridayWorkoutId(workoutIdsByFirstBaseUser.get(2))
                .build();

        //For second user
        ProgramFormRequest request2 = ProgramFormRequest.builder()
                .name("Second base program")
                .description("This is not a copy of first user's program.")
                .difficulty(Difficulty.HARD)
                .shared(true)
                .mondayWorkoutId(workoutIdsBySecondBaseUser.get(0))
                .wednesdayWorkoutId(workoutIdsBySecondBaseUser.get(1))
                .fridayWorkoutId(workoutIdsBySecondBaseUser.get(2))
                .build();

        //For first user
        programService.createProgram(userService.getFirstBaseUser(), request1);
        //For second user
        programService.createProgram(userService.getSecondBaseUser(), request2);
    }
}
