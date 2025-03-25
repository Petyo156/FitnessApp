package com.softuni.project.web;

import com.softuni.project.user.service.UserService;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.web.controller.AdminController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static com.softuni.project.web.TestBuilder.adminMetadata;
import static com.softuni.project.web.TestBuilder.userMetadata;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ExerciseService exerciseService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUnauthorizedRequestToUsersPage_shouldReturn404AndNotFoundView() throws Exception {
        MockHttpServletRequestBuilder request = get("/admin/users")
                .with(user(userMetadata()));

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("exception/not-found"));
    }

    @Test
    void getAuthorizedRequestToUsersPage_shouldReturnUsersPage() throws Exception {

        MockHttpServletRequestBuilder request = get("/admin/users")
                .with(user(adminMetadata()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("admin/all-users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void postRequestToUpdateUserStatus_shouldRedirectToUsers() throws Exception {

        UUID userId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = post("/admin/users/{id}/status", userId)
                .with(user(adminMetadata()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService, times(1)).updateUserStatus(any());
    }

    @Test
    void postRequestToUpdateUserRole_shouldRedirectToUsers() throws Exception {

        UUID userId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = post("/admin/users/{id}/role", userId)
                .with(user(adminMetadata()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService, times(1)).updateUserRole(any());
    }

    @Test
    void postRequestToApproveExercise_shouldRedirectToModeration() throws Exception {

        UUID exerciseId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = post("/admin/exercises/{id}/approval", exerciseId)
                .with(user(adminMetadata()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/exercises/moderation"));

        verify(exerciseService, times(1)).approveById(any());
    }

    @Test
    void postRequestToRejectExercise_shouldRedirectToModeration() throws Exception {

        UUID exerciseId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = post("/admin/exercises/{id}/rejection", exerciseId)
                .with(user(adminMetadata()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/exercises/moderation"));

        verify(exerciseService, times(1)).rejectById(any());
    }
}
