package com.softuni.project.web.dto;

import com.softuni.project.program.model.Difficulty;
import com.softuni.project.program.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ViewProgramResponse {
    private String name;

    private String description;

    private Difficulty difficulty;

    private LocalDateTime createdOn;

    private Status status;

    private Boolean sharedWithOthers;

    private List<ViewWorkoutResponse> workouts;

    private String addedByUsername;
}
