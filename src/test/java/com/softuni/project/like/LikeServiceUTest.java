package com.softuni.project.like;

import com.softuni.project.like.client.LikeClient;
import com.softuni.project.like.client.dto.LikeProgramRequest;
import com.softuni.project.like.service.LikeService;
import com.softuni.project.program.service.ProgramService;
import com.softuni.project.web.dto.ViewProgramResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceUTest {
    @Mock
    private LikeClient likeClient;

    @Mock
    private ProgramService programService;

    @InjectMocks
    private LikeService likeService;

    private static final UUID PROGRAM_ID = UUID.randomUUID();
    private static final UUID LIKED_BY_USER_ID = UUID.randomUUID();
    private static final UUID PROGRAM_OWNER_ID = UUID.randomUUID();

    @Test
    void likeProgram_whenResponseIs2xx_thenLogsSuccess() {
        ResponseEntity<Void> successResponse = ResponseEntity.ok().build();

        when(likeClient.likeProgram(any(LikeProgramRequest.class))).thenReturn(successResponse);

        likeService.likeProgram(PROGRAM_ID, LIKED_BY_USER_ID, PROGRAM_OWNER_ID);

        verify(likeClient, times(1)).likeProgram(any(LikeProgramRequest.class));
    }

    @Test
    void likeProgram_whenResponseIsNot2xx_thenDoesNotLogSuccess() {
        ResponseEntity<Void> errorResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(likeClient.likeProgram(any(LikeProgramRequest.class))).thenReturn(errorResponse);

        likeService.likeProgram(PROGRAM_ID, LIKED_BY_USER_ID, PROGRAM_OWNER_ID);

        verify(likeClient, times(1)).likeProgram(any(LikeProgramRequest.class));
    }

    @Test
    void getAllLikedPrograms_whenProgramsExist_thenReturnList() {
        String programId1 = UUID.randomUUID().toString();
        String programId2 = UUID.randomUUID().toString();

        List<String> likedProgramIds = Arrays.asList(programId1, programId2);
        List<ViewProgramResponse> expectedPrograms = Arrays.asList(
                ViewProgramResponse.builder().id(programId1).build(),
                ViewProgramResponse.builder().id(programId2).build()
        );

        when(likeClient.getAllLikedPrograms(LIKED_BY_USER_ID.toString())).thenReturn(ResponseEntity.ok(likedProgramIds));
        when(programService.getAllLikedPrograms(likedProgramIds)).thenReturn(expectedPrograms);

        List<ViewProgramResponse> result = likeService.getAllLikedPrograms(LIKED_BY_USER_ID);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedPrograms, result);

        verify(likeClient, times(1)).getAllLikedPrograms(LIKED_BY_USER_ID.toString());
        verify(programService, times(1)).getAllLikedPrograms(likedProgramIds);
    }
}
