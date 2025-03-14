package com.softuni.project.web.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ViewLogResponse {
    private LocalDateTime completionDate;
    private List<ViewLoggedExerciseResponse> loggedExercises;
    private String logId;
}