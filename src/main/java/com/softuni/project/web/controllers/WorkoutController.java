package com.softuni.project.web.controllers;

import com.softuni.project.excersise.service.ExercisesService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.SubmitExerciseRequest;
import com.softuni.project.web.dto.SubmitWorkoutRequest;
import com.softuni.project.web.dto.ViewWorkoutResponse;
import com.softuni.project.workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/workouts")
@Slf4j
public class WorkoutController {
    private final WorkoutService workoutService;
    private final UserService userService;
    private final ExercisesService exercisesService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService, ExercisesService exercisesService) {
        this.workoutService = workoutService;
        this.userService = userService;
        this.exercisesService = exercisesService;
    }

    @GetMapping("/your-workouts")
    public ModelAndView yourWorkouts(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/your-workouts");

        User user = userService.getById(authenticationMetadata.getId());
        List<ViewWorkoutResponse> workouts = workoutService.viewYourWorkouts(user);

        modelAndView.addObject("workouts", workouts);

        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView submitWorkout() {
        ModelAndView modelAndView = new ModelAndView("user/submit-workout");

        List<String> allExercises = exercisesService.findAllApprovedExercisesNames();

        modelAndView.addObject("submitWorkoutRequest", new SubmitWorkoutRequest());
        modelAndView.addObject("allExercises", allExercises);

        return modelAndView;
    }

    @PostMapping("/submit")
    public String submitWorkout(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Valid SubmitWorkoutRequest submitWorkoutRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/submit-workout";
        }

        User user = userService.getById(authenticationMetadata.getId());
        workoutService.submitWorkout(submitWorkoutRequest, user);

        return "redirect:/home";
    }

}
