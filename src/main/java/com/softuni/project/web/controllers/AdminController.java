package com.softuni.project.web.controllers;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final UserService userService;
    private final ExerciseService exerciseService;

    @Autowired
    public AdminController(UserService userService, ExerciseService exerciseService) {
        this.userService = userService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView allUsersPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView();

        List<User> users = userService.getAllUsersExceptGiven(authenticationMetadata.getId());

        modelAndView.addObject("users", users);
        modelAndView.setViewName("admin/all-users");
        return modelAndView;
    }

    @PostMapping("/users/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public String activateUser(@PathVariable String id) {

        userService.changeUserStatus(UUID.fromString(id));

        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public String deactivateUser(@PathVariable String id) {

        userService.changeUserStatus(UUID.fromString(id));

        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/reprioritize")
    @PreAuthorize("hasRole('ADMIN')")
    public String reprioritizeUser(@PathVariable String id) {

        userService.changeUserRole(UUID.fromString(id));

        return "redirect:/admin/users";
    }

    @GetMapping("/exercises/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView review() {

        ModelAndView modelAndView = new ModelAndView("admin/review-exercises");

        List<Exercise> pendingExercises = exerciseService.findAllPendingExercises();
        List<Exercise> rejectedExercises = exerciseService.findAllRejectedExercises();
        List<Exercise> approvedExercises = exerciseService.findAllApprovedExercises();
        log.info("Retrieved all exercises");

        modelAndView.addObject("pendingExercises", pendingExercises);
        modelAndView.addObject("rejectedExercises", rejectedExercises);
        modelAndView.addObject("approvedExercises", approvedExercises);

        return modelAndView;
    }

    @PostMapping("/exercises/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveExercise(@PathVariable String id) {
        exerciseService.approveById(UUID.fromString(id));

        return "redirect:/admin/exercises/review";
    }

    @PostMapping("/exercises/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectExercise(@PathVariable String id) {
        exerciseService.rejectById(UUID.fromString(id));

        return "redirect:/admin/exercises/review";
    }

}
