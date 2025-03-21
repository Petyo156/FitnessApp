package com.softuni.project.web.controller;

import com.softuni.project.like.client.dto.NotifyUserResponse;
import com.softuni.project.like.service.LikeService;
import com.softuni.project.like.service.NotificationService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.ViewProgramResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/like")
@Slf4j
public class LikeController {
    private final LikeService likeService;
    private final NotificationService notificationService;
    private final UserService userService;

    public LikeController(LikeService likeService, NotificationService notificationService, UserService userService) {
        this.likeService = likeService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping("/{programId}/{programOwnerId}")
    public String likeProgram(
            @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
            @PathVariable("programId") String programId,
            @PathVariable("programOwnerId") String programOwnerId) {
        User user = userService.getById(authenticationMetadata.getId());
        String likedByUserId = user.getId().toString();

        likeService.likeProgram(programId, likedByUserId, programOwnerId);
        notificationService.notifyUserForLikedProgram(programOwnerId, user.getId().toString(), programId, user.getUsername());
        return "redirect:/home";
    }

    @GetMapping("/programs/{userId}")
    public ModelAndView getLikedPrograms(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                         @PathVariable("userId") String userId) {
        ModelAndView modelAndView = new ModelAndView("user/liked-programs");
        User user = userService.getById(authenticationMetadata.getId());
        List<ViewProgramResponse> allLikedPrograms = likeService.getAllLikedPrograms(userId);

        modelAndView.addObject("user", user);
        modelAndView.addObject("allLikedPrograms", allLikedPrograms);
        return modelAndView;
    }

    @GetMapping("/notifications/{userId}")
    public ModelAndView getLikesNotifications(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                              @PathVariable("userId") String userId) {
        ModelAndView modelAndView = new ModelAndView("user/notifications");
        User user = userService.getById(authenticationMetadata.getId());
        List<NotifyUserResponse> userNotifications = notificationService.getAllNotificationsForUser(userId, user.getId().toString());

        modelAndView.addObject("user", user);
        modelAndView.addObject("userNotifications", userNotifications);
        return modelAndView;
    }
}
