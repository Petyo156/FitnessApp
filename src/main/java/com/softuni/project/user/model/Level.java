package com.softuni.project.user.model;

import lombok.Getter;

@Getter
public enum Level {
    NOVICE( "A complete beginner who is new to calisthenics. Focused on learning foundational movements, improving mobility, and building basic strength."),
    BEGINNER("Has a basic understanding of calisthenics techniques and can perform fundamental exercises like push-ups, squats, and planks with decent form."),
    COMPETENT("Demonstrates solid strength and control over intermediate exercises like pull-ups, dips, and progressions for more complex movements like L-sits."),
    PROFICIENT("Skilled in advanced calisthenics exercises like muscle-ups, pistol squats, and handstands. Training involves refining techniques and building endurance."),
    EXPERT("A master of calisthenics, capable of performing elite-level skills like planches, one-arm pull-ups, and human flags with precision and control. Focuses on pushing physical limits."),
    PREFER_NOT_TO_SAY("Skip for now");

    private String description;

    private Level(String description) {
        this.description = description;
    }

}
