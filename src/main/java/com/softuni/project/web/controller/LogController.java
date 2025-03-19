package com.softuni.project.web.controller;

import com.softuni.project.log.service.LogService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.*;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workout.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
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

    @PostMapping("/{dayOfWeek}/{workoutId}")
    public String createWorkoutLog(
            @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
            @PathVariable String dayOfWeek,
            @PathVariable String workoutId,
            @Valid WorkoutLogRequest workoutLogRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Sets > 0 / Reps > 0 / Added weight >= 0");

            return "redirect:/logs/" + dayOfWeek + "/" + workoutId;
        }

        User user = userService.getById(authenticationMetadata.getId());
        Workout workout = workoutService.getById(UUID.fromString(workoutId));

        logService.createLog(user, workout, workoutLogRequest);

        return "redirect:/home";
    }

    @GetMapping("/{dayOfWeek}/{workoutId}")
    public ModelAndView showLogWorkoutPage(
            @PathVariable String dayOfWeek,
            @PathVariable String workoutId) {

        ModelAndView modelAndView = new ModelAndView("user/log-workout");

        Workout workout = workoutService.getById(UUID.fromString(workoutId));
        ViewWorkoutResponse workoutResponse = workoutService.getViewWorkoutResponseByWorkout(workout);
        WorkoutLogRequest workoutLogRequest = workoutService.initializeWorkoutLogRequest(workoutResponse);

        modelAndView.addObject("workoutResponse", workoutResponse);
        modelAndView.addObject("workoutLogRequest", workoutLogRequest);
        modelAndView.addObject("dayOfWeek", dayOfWeek);

        return modelAndView;
    }

    @GetMapping()
    public ModelAndView getUserLogs(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/your-logs");
        UUID userId = authenticationMetadata.getId();

        List<ViewLogResponse> logs = logService.getViewLogResponsesForUser(userId);
        modelAndView.addObject("logs", logs);

        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public String deleteLog(@PathVariable String id) {
        logService.deleteLogById(UUID.fromString(id));

        return "redirect:/logs";
    }
}