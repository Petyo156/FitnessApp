package com.softuni.project.web.controllers;

import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.EditProfileRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/profile/edit")
    public ModelAndView editProfile(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/edit-profile");

        User user = userService.getById(authenticationMetadata.getId());

        modelAndView.addObject("user", user);
        modelAndView.addObject("editProfileRequest", new EditProfileRequest());

        return modelAndView;
    }

    @PutMapping("/profile/edit")
    public ModelAndView editProfile(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Valid EditProfileRequest editProfileRequest, BindingResult bindingResult) {
        User user = userService.getById(authenticationMetadata.getId());

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("user/edit-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("editProfileRequest", editProfileRequest);
            return modelAndView;
        }

        userService.editUserProfile(user, editProfileRequest);

        return new ModelAndView("redirect:/home");
    }


    @GetMapping("/search")
    public String search(@RequestParam("username") String username) {
        User user = userService.getByUsername(username);

        return "redirect:/users/" + user.getId() + "/profile";
    }
}
