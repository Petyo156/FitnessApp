package com.softuni.project.web.controllers;

import com.softuni.project.program.service.ProgramService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.EditProfileRequest;
import com.softuni.project.web.dto.ViewProgramResponse;
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
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final ProgramService programService;

    @Autowired
    public UserController(UserService userService, ProgramService programService) {
        this.userService = userService;
        this.programService = programService;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getProfile(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView();

        User user = userService.getById(id);
        List<ViewProgramResponse> programs = programService.getAllSharedProgramsByUser(user);

        modelAndView.setViewName("user/profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("loggedUserId", authenticationMetadata.getId());
        modelAndView.addObject("programs", programs);

        log.info("View user profile with username {}", user.getUsername());
        return modelAndView;
    }

    @GetMapping("/profile/edit")
    public ModelAndView showEditProfileForm(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
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
