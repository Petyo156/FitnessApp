package com.softuni.project.web.controllers;

import com.softuni.project.program.service.ProgramService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.ProgramFormRequest;
import com.softuni.project.web.dto.ViewProgramResponse;
import com.softuni.project.web.dto.ViewWorkoutResponse;
import com.softuni.project.workout.service.WorkoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/programs")
@Slf4j
public class ProgramController {
    private final ProgramService programService;
    private final UserService userService;
    private final WorkoutService workoutService;


    public ProgramController(ProgramService programService, UserService userService, WorkoutService workoutService) {
        this.programService = programService;
        this.userService = userService;
        this.workoutService = workoutService;
    }

    @GetMapping("/create")
    public ModelAndView submitProgram(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getId());

        ModelAndView modelAndView = new ModelAndView("user/submit-program");
        List<ViewWorkoutResponse> workouts = workoutService.getYourWorkouts(user);

        modelAndView.addObject("user", user);
        modelAndView.addObject("workouts", workouts);
        modelAndView.addObject("programFormRequest", new ProgramFormRequest());
        return modelAndView;
    }

    @PostMapping("/submit")
    public String submitProgram(
            @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
            @ModelAttribute ProgramFormRequest programFormRequest) {

        log.info("Received ProgramFormRequest: {}", programFormRequest);

        User user = userService.getById(authenticationMetadata.getId());
        programService.createProgram(user, programFormRequest);

        log.info("New program created successfully!");
        return "redirect:/programs/create";
    }

    @GetMapping("/your-programs")
    public ModelAndView yourPrograms(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getId());

        List<ViewProgramResponse> programs = programService.getAllProgramsByUser(user);
        log.info("Fetching all programs by logged user");

        ModelAndView modelAndView = new ModelAndView("user/your-programs");

        modelAndView.addObject("user", user);
        modelAndView.addObject("programs", programs);

        return modelAndView;
    }

    @GetMapping("/browse-programs")
    public ModelAndView browsePrograms(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getId());
        List<ViewProgramResponse> programs = programService.getAllSharedProgramsByAllOtherUsers(user);
        log.info("Fetching all programs shared by all other users");

        ModelAndView modelAndView = new ModelAndView("user/browse-programs");
        modelAndView.addObject("programs", programs);

        return modelAndView;
    }
}
