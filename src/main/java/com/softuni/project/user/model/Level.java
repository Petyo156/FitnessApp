package com.softuni.project.user.model;

import lombok.Getter;

@Getter
public enum Level {
    NOVICE( "A complete beginner."),
    BEGINNER("Has basic understanding of fitness."),
    COMPETENT("Demonstrates solid strength."),
    PROFICIENT("Skilled in advanced exercises."),
    EXPERT("Focuses on pushing physical limits."),
    PREFER_NOT_TO_SAY("Skip for now");

    private String description;

    private Level(String description) {
        this.description = description;
    }

}
