package com.softuni.project.web.controllers;

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

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
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

        log.info("User with id {} was deleted", id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public String activateUser(@PathVariable String id) {

        userService.changeUserStatus(UUID.fromString(id));

        log.info("User with id {} was activated", id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public String deactivateUser(@PathVariable String id) {

        userService.changeUserStatus(UUID.fromString(id));

        log.info("User with id {} was deactivated", id);
        return "redirect:/admin/users";
    }

}
