package com.softuni.project.web;


import com.softuni.project.program.service.ProgramService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.controller.UserController;
import com.softuni.project.web.dto.ViewProgramResponse;
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

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ProgramService programService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getEditProfilePage_shouldReturnEditProfilePage() throws Exception {
        AuthenticationMetadata authenticationMetadata = userMetadata();
        User user = aRandomUser();
        user.setId(authenticationMetadata.getId());

        when(userService.getById(authenticationMetadata.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/users/profile/edit")
                .with(user(authenticationMetadata));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit-profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("editProfileRequest"));

        verify(userService, times(1)).getById(user.getId()); // FIXED
    }


    @Test
    void putEditProfile_shouldRedirectToHome() throws Exception {
        User user = aRandomUser();

        when(userService.getById(UUID.randomUUID())).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/users/profile/edit")
                .with(user(userMetadata()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(userService, times(1)).editUserProfile(any(), any());
    }

    @Test
    void searchUser_shouldRedirectToUserProfile() throws Exception {
        User user = aRandomUser();
        String username = user.getUsername();

        when(userService.getByUsername(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/users/search")
                .param("username", username)
                .with(user(userMetadata()));

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/" + user.getId() + "/profile"));

        verify(userService, times(1)).getByUsername(any());
    }
}
