package com.softuni.project.web.controller;

import com.softuni.project.program.service.ProgramService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.EditProfileRequest;
import com.softuni.project.web.dto.ViewProgramResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ProgramService programService;

    @Autowired
    public UserController(UserService userService, ProgramService programService) {
        this.userService = userService;
        this.programService = programService;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getProfile(@PathVariable String id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView();

        User user = userService.getById(id);
        User loggedUser = userService.getById(authenticationMetadata.getId());
        List<ViewProgramResponse> programs = programService.getAllSharedProgramsByUser(user);

        modelAndView.setViewName("user/profile");
        modelAndView.addObject("pathUser", user);
        modelAndView.addObject("user", loggedUser);
        modelAndView.addObject("programs", programs);

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
    public ModelAndView editProfile(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                    @Valid @ModelAttribute EditProfileRequest editProfileRequest,
                                    BindingResult bindingResult) {
        User user = userService.getById(authenticationMetadata.getId());
        ModelAndView modelAndView = new ModelAndView("user/edit-profile");

        if (bindingResult.hasErrors()) {
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
