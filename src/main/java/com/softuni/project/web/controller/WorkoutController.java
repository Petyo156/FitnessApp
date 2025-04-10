package com.softuni.project.web.controller;

import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.SubmitWorkoutRequest;
import com.softuni.project.web.dto.ViewWorkoutResponse;
import com.softuni.project.workout.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final UserService userService;
    private final ExerciseService exerciseService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService, ExerciseService exerciseService) {
        this.workoutService = workoutService;
        this.userService = userService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/personal")
    public ModelAndView getYourWorkouts(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/your-workouts");

        User user = userService.getById(authenticationMetadata.getId());
        List<ViewWorkoutResponse> workouts = workoutService.getWorkoutsForUser(user);

        modelAndView.addObject("workouts", workouts);
        modelAndView.addObject("user", userService.getById(authenticationMetadata.getId()));

        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView showCreateWorkoutForm(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/submit-workout");

        List<String> allExercises = exerciseService.findAllApprovedExercisesNames();

        modelAndView.addObject("submitWorkoutRequest", new SubmitWorkoutRequest());
        modelAndView.addObject("allExercises", allExercises);
        modelAndView.addObject("user", userService.getById(authenticationMetadata.getId()));

        return modelAndView;
    }

    @PostMapping()
    public ModelAndView createWorkout(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                      @Valid @ModelAttribute SubmitWorkoutRequest submitWorkoutRequest,
                                      BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView("user/submit-workout");

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("allExercises", exerciseService.findAllApprovedExercisesNames());
            modelAndView.addObject("user", userService.getById(authenticationMetadata.getId()));
            modelAndView.addObject("submitWorkoutRequest", submitWorkoutRequest);
            return modelAndView;
        }

        User user = userService.getById(authenticationMetadata.getId());
        workoutService.submitWorkout(submitWorkoutRequest, user);

        return new ModelAndView("redirect:/home");
    }

}
