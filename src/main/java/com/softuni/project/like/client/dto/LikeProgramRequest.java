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
public class LikeProgramRequest {
    @NotNull
    private String programId;

    @NotNull
    private String likedByUserId;

    @NotNull
    private String programOwnerId;
}