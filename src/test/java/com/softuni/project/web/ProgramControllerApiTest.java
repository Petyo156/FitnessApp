package com.softuni.project.web;

import com.softuni.project.common.DayOfWeek;
import com.softuni.project.program.model.Difficulty;
import com.softuni.project.program.model.Program;
import com.softuni.project.program.service.ProgramService;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.controller.ProgramController;
import com.softuni.project.web.dto.ProgramFormRequest;
import com.softuni.project.web.dto.ViewProgramResponse;
import com.softuni.project.web.dto.ViewWorkoutResponse;
import com.softuni.project.web.dto.WorkoutExerciseEntry;
import com.softuni.project.workout.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static com.softuni.project.web.TestBuilder.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProgramController.class)
public class ProgramControllerApiTest {

    @MockitoBean
    private ProgramService programService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private WorkoutService workoutService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postCreateProgram_shouldRedirectToPersonalPrograms() throws Exception {
        ProgramFormRequest programFormRequest = new ProgramFormRequest();
        programFormRequest.setName("Program Name");
        programFormRequest.setMondayWorkoutId(UUID.randomUUID().toString());
        programFormRequest.setDescription("Description");
        programFormRequest.setShared(true);
        programFormRequest.setDifficulty(Difficulty.HARD);

        when(userService.getById(any())).thenReturn(aRandomUser());

        MockHttpServletRequestBuilder request = post("/programs")
                .with(user(userMetadata()))
                .with(csrf())
                .flashAttr("programFormRequest", programFormRequest);

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/programs/personal"));

        verify(programService, times(1)).createProgram(any(), any());
    }

    @Test
    void getUserPrograms_shouldReturnPersonalProgramsPage() throws Exception {
        User user = aRandomUser();

        when(userService.getById(any())).thenReturn(user);

        WorkoutExerciseEntry exerciseEntry = WorkoutExerciseEntry.builder()
                .exerciseId(UUID.randomUUID().toString())
                .sets(2)
                .reps(2)
                .addedWeight(2.0)
                .exerciseName("Pushing up")
                .build();

        ViewWorkoutResponse viewWorkoutResponse = ViewWorkoutResponse.builder()
                .workoutId(UUID.randomUUID().toString())
                .additionalInfo("Additional info")
                .exercises(List.of(exerciseEntry))
                .dayOfWeek(DayOfWeek.MONDAY)
                .approximateDuration(60)
                .build();

        ViewProgramResponse viewProgramResponse = ViewProgramResponse.builder()
                .addedById(user.getId().toString())
                .id(UUID.randomUUID().toString())
                .workouts(List.of(viewWorkoutResponse))
                .sharedWithOthers(true)
                .build();

        List<ViewProgramResponse> programsResponses = List.of(viewProgramResponse);

        when(programService.getAllProgramsByUser(any())).thenReturn(programsResponses);

        MockHttpServletRequestBuilder request = get("/programs/personal")
                .with(user(userMetadata()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/your-programs"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("programs"));

        verify(programService, times(1)).getAllProgramsByUser(any());
    }

    @Test
    void browseSharedPrograms_shouldReturnBrowseProgramsPage() throws Exception {
        User user = aRandomUser();
        List<ViewProgramResponse> programs = List.of(new ViewProgramResponse());

        when(userService.getById(any())).thenReturn(user);
        when(programService.getAllSharedProgramsByAllOtherUsers(any())).thenReturn(programs);

        MockHttpServletRequestBuilder request = get("/programs/browse")
                .with(user(userMetadata()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/browse-programs"))
                .andExpect(model().attributeExists("programs"))
                .andExpect(model().attributeExists("user"));

        verify(programService, times(1)).getAllSharedProgramsByAllOtherUsers(any());
    }

    @Test
    void postActivateProgram_shouldRedirectToHome() throws Exception {
        UUID programId = UUID.randomUUID();
        User user = aRandomUser();
        Program program = new Program();

        when(userService.getById(any())).thenReturn(user);
        when(programService.getProgramById(any())).thenReturn(program);

        MockHttpServletRequestBuilder request = post("/programs/{programId}/activate", programId)
                .with(user(userMetadata()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(userService, times(1)).setActiveProgramForUser(any(), any());
    }

    @Test
    void postDeactivateProgram_shouldRedirectToHome() throws Exception {
        User user = aRandomUser();

        when(userService.getById(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = post("/programs/deactivate")
                .with(user(userMetadata()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(userService, times(1)).deactivateProgramForUser(any());
    }
}
