package com.softuni.project.config;

import com.softuni.project.like.service.LikeService;
import com.softuni.project.like.service.NotificationService;
import com.softuni.project.program.model.Program;
import com.softuni.project.program.service.ProgramService;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(6)
@Component
@Slf4j
public class InitializeLike implements CommandLineRunner {
    private final LikeService likeService;
    private final NotificationService notificationService;

    private final UserService userService;
    private final ProgramService programService;

    @Autowired
    public InitializeLike(LikeService likeService, NotificationService notificationService, UserService userService, ProgramService programService) {
        this.likeService = likeService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.programService = programService;
    }

    @Override
    public void run(String... args) throws Exception {

        User firstBaseUser = userService.getFirstBaseUser();
        User secondBaseUser = userService.getSecondBaseUser();

        List<Program> allProgramsByUser = programService.getAllProgramsEntitiesByUser(firstBaseUser);
        try {
            likeService.likeProgram(allProgramsByUser.getFirst().getId(),
                    secondBaseUser.getId(), firstBaseUser.getId());
            notificationService.notifyUserForLikedProgram(firstBaseUser.getId(), secondBaseUser.getId(),
                    allProgramsByUser.getFirst().getId(), secondBaseUser.getUsername());
        } catch (FeignException e) {
            log.error("Separate microservice for likes and notifications not turned on or already has sent a like and a notification demos");
        }
    }
}
