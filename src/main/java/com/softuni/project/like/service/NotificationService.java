package com.softuni.project.like.service;

import com.softuni.project.exception.ExceptionMessages;
import com.softuni.project.exception.UnauthorizedNotificationAccessException;
import com.softuni.project.like.client.NotificationClient;
import com.softuni.project.like.client.dto.NotifyUserRequest;
import com.softuni.project.like.client.dto.NotifyUserResponse;
import com.softuni.project.program.model.Program;
import com.softuni.project.program.service.ProgramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class NotificationService {
    private final NotificationClient notificationClient;
    private final ProgramService programService;

    @Autowired
    public NotificationService(NotificationClient notificationClient, ProgramService programService) {
        this.notificationClient = notificationClient;
        this.programService = programService;
    }

    public void notifyUserForLikedProgram(String receiverId, String senderId, String programId, String likedByUserUsername) {
        NotifyUserRequest notifyUserRequest = initializeNotifyUserRequest(receiverId, senderId, programId, likedByUserUsername);

        ResponseEntity<Void> response = notificationClient.notifyUserForLikedProgram(notifyUserRequest);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return;
        }
        log.info("[FEIGN CALL SUCCESS] Sending notification was successful.");
    }

    private NotifyUserRequest initializeNotifyUserRequest(String receiverId, String senderId, String programId, String likedByUserUsername) {
        Program program = programService.getProgramById(UUID.fromString(programId));
        String message = String.format("Your program '%s' was liked by %s", program.getName(), likedByUserUsername);

        return NotifyUserRequest.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .message(message)
                .build();
    }

    public List<NotifyUserResponse> getAllNotificationsForUser(String userId, String loggedUserId) {
        if(!userId.equals(loggedUserId)) {
            throw new UnauthorizedNotificationAccessException(ExceptionMessages.UNAUTHORIZED_NOTIFICATIONS_ACCESS);
        }
        ResponseEntity<List<NotifyUserResponse>> notificationsForUser = notificationClient.getNotificationsForUser(userId);

        log.info("[FEIGN CALL SUCCESS] Retrieved all notifications for user.");
        return notificationsForUser.getBody();
    }
}
