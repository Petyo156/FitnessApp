package com.softuni.project.like.client;

import com.softuni.project.like.client.dto.NotifyUserRequest;
import com.softuni.project.like.client.dto.NotifyUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "likes-notification-svc", url = "http://localhost:9091/api/v1/notifications")
public interface NotificationClient {

    @PostMapping()
    ResponseEntity<Void> notifyUserForLikedProgram(@RequestBody NotifyUserRequest notifyUserRequest);

    @GetMapping("/{userId}")
    ResponseEntity<List<NotifyUserResponse>> getNotificationsForUser(@PathVariable("userId") String userId);
}
