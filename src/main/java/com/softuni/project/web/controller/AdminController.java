package com.softuni.project.web.controller;

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

        User user = userService.getById(authenticationMetadata.getId());
        List<User> users = userService.getAllUsersExceptGiven(authenticationMetadata.getId());

        modelAndView.addObject("user", user);
        modelAndView.addObject("users", users);
        modelAndView.setViewName("admin/all-users");
        return modelAndView;
    }

    @PostMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserStatus(@PathVariable String id) {
        userService.updateUserStatus(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserRole(@PathVariable String id) {
        userService.updateUserRole(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/exercises/moderation")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView reviewExercises(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("admin/review-exercises");

        User user = userService.getById(authenticationMetadata.getId());

        modelAndView.addObject("user", user);
        modelAndView.addObject("pendingExercises", exerciseService.findAllPendingExercises());
        modelAndView.addObject("rejectedExercises", exerciseService.findAllRejectedExercises());
        modelAndView.addObject("approvedExercises", exerciseService.findAllApprovedExercises());

        log.info("Retrieved all exercises");
        return modelAndView;
    }


    @PostMapping("/exercises/{id}/approval")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveExercise(@PathVariable String id) {
        exerciseService.approveById(id);

        return "redirect:/admin/exercises/moderation";
    }

    @PostMapping("/exercises/{id}/rejection")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectExercise(@PathVariable String id) {
        exerciseService.rejectById(id);

        return "redirect:/admin/exercises/moderation";
    }

    @PostMapping("/exercises/{id}/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    public String revokeExercise(@PathVariable String id) {
        exerciseService.revokeById(id);

        return "redirect:/admin/exercises/moderation";
    }

}
