package com.softuni.project.web;

import com.softuni.project.like.service.LikeService;
import com.softuni.project.like.service.NotificationService;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.controller.LikeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static com.softuni.project.web.TestBuilder.aRandomUser;
import static com.softuni.project.web.TestBuilder.userMetadata;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikeController.class)
public class LikeControllerApiTest {

    @MockitoBean
    private LikeService likeService;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postLikeProgram_shouldRedirectToHome() throws Exception {
        when(userService.getById(any())).thenReturn(aRandomUser());

        MockHttpServletRequestBuilder request = post("/like/{programId}/{programOwnerId}", UUID.randomUUID(), UUID.randomUUID())
                .with(user(userMetadata()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(likeService, times(1)).likeProgram(any(), any(), any());
        verify(notificationService, times(1)).notifyUserForLikedProgram(any(), any(), any(), any());
    }

    @Test
    void getLikedPrograms_shouldReturnLikedProgramsPage() throws Exception {
        User user = aRandomUser();
        when(userService.getById(any())).thenReturn(user);
        when(likeService.getAllLikedPrograms(any())).thenReturn(List.of());

        MockHttpServletRequestBuilder request = get("/like/programs/{userId}", user.getId())
                .with(user(userMetadata()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/liked-programs"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("allLikedPrograms"));

        verify(likeService, times(1)).getAllLikedPrograms(any());
    }

    @Test
    void getLikeNotifications_shouldReturnNotificationsPage() throws Exception {
        User user = aRandomUser();
        when(userService.getById(any())).thenReturn(user);
        when(notificationService.getAllNotificationsForUser(any(), any())).thenReturn(List.of());

        MockHttpServletRequestBuilder request = get("/like/notifications/{userId}", user.getId())
                .with(user(userMetadata()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/notifications"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userNotifications"));

        verify(notificationService, times(1)).getAllNotificationsForUser(any(), any());
    }
}
