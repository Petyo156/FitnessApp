package com.softuni.project.like.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyUserResponse {
    private String receiverId;

    private String senderId;

    private String message;

    private String date;
}
