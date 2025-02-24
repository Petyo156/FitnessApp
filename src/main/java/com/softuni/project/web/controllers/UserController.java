package com.softuni.project.web.controllers;

import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView viewProfile(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView();

        User user = userService.getById(id);

        modelAndView.setViewName("user/profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("loggedUserId", authenticationMetadata.getId());

        log.info("View user profile with username {}", user.getUsername());
        return modelAndView;
    }

    @GetMapping("/search")
    public String search(@RequestParam("username") String username) {
        User user = userService.getByUsername(username);

        return "redirect:/users/" + user.getId() + "/profile";
    }
}
