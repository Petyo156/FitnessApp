package com.softuni.project.web.controllers;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExercisesService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final UserService userService;
    private final ExercisesService exercisesService;

    @Autowired
    public AdminController(UserService userService, ExercisesService exercisesService) {
        this.userService = userService;
        this.exercisesService = exercisesService;
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

    @DeleteMapping("/users/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable String id) {

        userService.deleteUserById(UUID.fromString(id));

        return "redirect:/admin/users";
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

        List<Exercise> pendingExercises = exercisesService.findAllPendingExercises();
        List<Exercise> rejectedExercises = exercisesService.findAllRejectedExercises();
        List<Exercise> approvedExercises = exercisesService.findAllApprovedExercises();
        log.info("Retrieved all exercises");

        modelAndView.addObject("pendingExercises", pendingExercises);
        modelAndView.addObject("rejectedExercises", rejectedExercises);
        modelAndView.addObject("approvedExercises", approvedExercises);

        return modelAndView;
    }

    @DeleteMapping("/exercises/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteExercise(@PathVariable String id) {
        exercisesService.deleteById(UUID.fromString(id));

        return "redirect:/admin/exercises/review";
    }

    @PostMapping("/exercises/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveExercise(@PathVariable String id) {
        exercisesService.approveById(UUID.fromString(id));

        return "redirect:/admin/exercises/review";
    }

    @PostMapping("/exercises/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectExercise(@PathVariable String id) {
        exercisesService.rejectById(UUID.fromString(id));

        return "redirect:/admin/exercises/review";
    }

}
