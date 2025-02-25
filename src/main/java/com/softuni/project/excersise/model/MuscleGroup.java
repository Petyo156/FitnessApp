package com.softuni.project.excersise.model;

public enum MuscleGroup {
    CHEST("Chest"),
    BACK("Back"),
    BICEPS("Biceps"),
    TRICEPS("Triceps"),
    SHOULDERS("Shoulders"),
    FOREARMS("Forearms"),

    // Major core muscle groups
    ABS("Abdominals"),
    OBLIQUES("Obliques"),
    LOWER_BACK("Lower Back"),

    // Major lower body muscle groups
    QUADS("Quadriceps"),
    HAMSTRINGS("Hamstrings"),
    GLUTES("Glutes"),
    CALVES("Calves"),

    // Full body / other
    FULL_BODY("Full Body"),
    CARDIO("Cardiovascular System");

    private String displayName;

    MuscleGroup(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return name();
    }
}
