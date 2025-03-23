package com.softuni.project.like;

import com.softuni.project.exception.ExceptionMessages;
import com.softuni.project.exception.UnauthorizedNotificationAccessException;
import com.softuni.project.like.client.NotificationClient;
import com.softuni.project.like.client.dto.NotifyUserRequest;
import com.softuni.project.like.client.dto.NotifyUserResponse;
import com.softuni.project.like.service.NotificationService;
import com.softuni.project.program.model.Program;
import com.softuni.project.program.service.ProgramService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceUTest {

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private ProgramService programService;

    @InjectMocks
    private NotificationService notificationService;

    private static final String RECEIVER_ID = "receiver-1";
    private static final String SENDER_ID = "sender-1";
    private static final String PROGRAM_ID = UUID.randomUUID().toString();
    private static final String LIKED_BY_USERNAME = "john_doe";
    private static final String PROGRAM_NAME = "Strength Training";

    @Test
    void notifyUserForLikedProgram_whenResponseIs2xx_thenLogsSuccess() {
        Program program = Program.builder()
                .id(UUID.fromString(PROGRAM_ID))
                .name(PROGRAM_NAME)
                .build();

        NotifyUserRequest request = NotifyUserRequest.builder()
                .receiverId(RECEIVER_ID)
                .senderId(SENDER_ID)
                .message(String.format("Your program '%s' was liked by %s", PROGRAM_NAME, LIKED_BY_USERNAME))
                .build();

        when(programService.getProgramById(UUID.fromString(PROGRAM_ID))).thenReturn(program);
        when(notificationClient.notifyUserForLikedProgram(request))
                .thenReturn(ResponseEntity.ok().build());

        notificationService.notifyUserForLikedProgram(RECEIVER_ID, SENDER_ID, PROGRAM_ID, LIKED_BY_USERNAME);

        verify(programService, times(1)).getProgramById(UUID.fromString(PROGRAM_ID));
        verify(notificationClient, times(1)).notifyUserForLikedProgram(any(NotifyUserRequest.class));
    }

    @Test
    void notifyUserForLikedProgram_whenResponseIsNot2xx_thenDoesNotLogSuccess() {
        Program program = Program.builder()
                .id(UUID.fromString(PROGRAM_ID))
                .name(PROGRAM_NAME)
                .build();

        when(programService.getProgramById(UUID.fromString(PROGRAM_ID))).thenReturn(program);
        when(notificationClient.notifyUserForLikedProgram(any(NotifyUserRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        notificationService.notifyUserForLikedProgram(RECEIVER_ID, SENDER_ID, PROGRAM_ID, LIKED_BY_USERNAME);

        verify(notificationClient, times(1)).notifyUserForLikedProgram(any(NotifyUserRequest.class));
    }

    @Test
    void getAllNotificationsForUser_whenUserIdMatchesLoggedUser_thenReturnsNotifications() {
        List<NotifyUserResponse> notifications = Arrays.asList(
                NotifyUserResponse.builder()
                        .message("New like on your program")
                        .receiverId(RECEIVER_ID)
                        .senderId(SENDER_ID)
                        .date("2024-03-22 10:15:30")
                        .build(),
                NotifyUserResponse.builder()
                        .message("New like on your second program")
                        .receiverId(RECEIVER_ID)
                        .senderId("sender-2")
                        .date("2024-03-21 08:45:00")
                        .build()
        );

        ResponseEntity<List<NotifyUserResponse>> responseEntity = ResponseEntity.ok(notifications);

        when(notificationClient.getNotificationsForUser(RECEIVER_ID)).thenReturn(responseEntity);

        List<NotifyUserResponse> result = notificationService.getAllNotificationsForUser(RECEIVER_ID, RECEIVER_ID);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(notifications, result);

        verify(notificationClient, times(1)).getNotificationsForUser(RECEIVER_ID);
    }

    @Test
    void getAllNotificationsForUser_whenUserIdDoesNotMatchLoggedUser_thenThrowsException() {
        UnauthorizedNotificationAccessException exception = assertThrows(
                UnauthorizedNotificationAccessException.class,
                () -> notificationService.getAllNotificationsForUser(RECEIVER_ID, "another-user")
        );

        assertEquals(ExceptionMessages.UNAUTHORIZED_NOTIFICATIONS_ACCESS, exception.getMessage());
        verify(notificationClient, never()).getNotificationsForUser(anyString());
    }
}
