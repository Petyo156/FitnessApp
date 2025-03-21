package com.softuni.project.like.client.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyUserRequest {
    @NotNull
    private String receiverId;

    @NotNull
    private String senderId;

    @NotNull
    private String message;
}
