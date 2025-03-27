package com.softuni.project.user.model;

public enum Country {
    BULGARIA("Bulgaria"),
    BELGIUM("Belgium"),
    BRAZIL("Brazil"),
    USA("United States of America"),
    CANADA("Canada"),
    CHINA("Republic of China"),
    FRANCE("France"),
    GERMANY("Germany"),
    INDIA("India"),
    RUSSIA("Russia"),
    ITALY("Italy"),
    JAPAN("Japan");

    private String country;

    Country(String country) {
        this.country = country;
    }

    public String getUrl() {
        return country;
    }

}