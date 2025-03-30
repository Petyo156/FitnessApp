package com.softuni.project.web.controller;

import com.softuni.project.program.model.Program;
import com.softuni.project.program.service.ProgramService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.ProgramFormRequest;
import com.softuni.project.web.dto.ViewProgramResponse;
import com.softuni.project.web.dto.ViewWorkoutResponse;
import com.softuni.project.workout.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/programs")
public class ProgramController {
    private final ProgramService programService;
    private final UserService userService;
    private final WorkoutService workoutService;


    public ProgramController(ProgramService programService, UserService userService, WorkoutService workoutService) {
        this.programService = programService;
        this.userService = userService;
        this.workoutService = workoutService;
    }

    @GetMapping("/new")
    public ModelAndView showProgramForm(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getId());

        ModelAndView modelAndView = new ModelAndView("user/submit-program");
        List<ViewWorkoutResponse> workouts = workoutService.getWorkoutsForUser(user);

        modelAndView.addObject("user", user);
        modelAndView.addObject("workouts", workouts);
        modelAndView.addObject("programFormRequest", new ProgramFormRequest());
        return modelAndView;
    }

    @PostMapping()
    public ModelAndView createProgram(
            @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
            @Valid @ModelAttribute ProgramFormRequest programFormRequest,
            BindingResult bindingResult) {

        User user = userService.getById(authenticationMetadata.getId());
        ModelAndView modelAndView = new ModelAndView("user/submit-program");
        modelAndView.addObject("user", user);

        if(bindingResult.hasErrors()) {
            modelAndView.addObject("programFormRequest", programFormRequest);
            modelAndView.addObject("user", user);
            modelAndView.addObject("workouts", workoutService.getWorkoutsForUser(user));
            return modelAndView;
        }

        programService.createProgram(user, programFormRequest);

        return new ModelAndView("redirect:/programs/personal");
    }

    @GetMapping("/personal")
    public ModelAndView getUserPrograms(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/your-programs");

        modelAndView.addObject("user", userService.getById(authenticationMetadata.getId()));
        modelAndView.addObject("programs", programService.getAllProgramsByUser(userService.getById(authenticationMetadata.getId())));

        return modelAndView;
    }

    @GetMapping("/browse")
    public ModelAndView browseSharedPrograms(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getId());
        List<ViewProgramResponse> programs = programService.getAllSharedProgramsByAllOtherUsers(user);

        ModelAndView modelAndView = new ModelAndView("user/browse-programs");
        modelAndView.addObject("programs", programs);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @PostMapping("/{programId}/activate")
    public String activateProgram(@PathVariable("programId") UUID programId, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getId());
        Program program = programService.getById(programId);

        userService.setActiveProgramForUser(user, program);

        return "redirect:/home";
    }

    @PostMapping("/deactivate")
    public String deactivateProgram(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getId());
        userService.deactivateProgramForUser(user);

        return "redirect:/home";
    }
}
