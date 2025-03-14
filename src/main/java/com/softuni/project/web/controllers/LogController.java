package com.softuni.project.web.controllers;

import com.softuni.project.exception.DomainException;
import com.softuni.project.log.service.LogService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.*;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/logs")
public class LogController {
    private final UserService userService;
    private final WorkoutService workoutService;
    private final LogService logService;

    @Autowired
    public LogController(UserService userService, WorkoutService workoutService, LogService logService) {
        this.userService = userService;
        this.workoutService = workoutService;
        this.logService = logService;
    }

    @PostMapping("/{id}")
    public String logWorkout(
            @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
            @PathVariable String id,
            @Valid WorkoutLogRequest workoutLogRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new DomainException("Invalid input for creating a log: " + bindingResult.getAllErrors());
        }

        User user = userService.getById(authenticationMetadata.getId());
        Workout workout = workoutService.getById(UUID.fromString(id));

        logService.createLog(user, workout, workoutLogRequest);

        return "redirect:/home";
    }

    @GetMapping("/{dayOfWeek}/{workoutId}")
    public ModelAndView logWorkoutPage(@PathVariable String dayOfWeek, @PathVariable String workoutId) {
        ModelAndView modelAndView = new ModelAndView("user/log-workout");
        Workout workout = workoutService.getById(UUID.fromString(workoutId));
        ViewWorkoutResponse workoutResponse = workoutService.getViewWorkoutResponse(workout);
        WorkoutLogRequest workoutLogRequest = workoutService.initializeWorkoutLogRequest(workoutResponse);

        modelAndView.addObject("workoutLogRequest", workoutLogRequest);
        modelAndView.addObject("workoutResponse", workoutResponse);
        modelAndView.addObject("dayOfWeek", dayOfWeek);

        return modelAndView;
    }

    @GetMapping("/view")
    public ModelAndView viewLogs(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/your-logs");
        UUID userId = authenticationMetadata.getId();

        List<ViewLogResponse> logs = logService.getViewLogResponsesForUser(userId);
        modelAndView.addObject("logs", logs);

        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public String deleteLog(@PathVariable String id) {
        logService.deleteLogById(UUID.fromString(id));

        log.info("Log deleted successfully");
        return "redirect:/logs/view";
    }
}