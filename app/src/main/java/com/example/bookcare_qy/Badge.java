package com.example.bookcare_qy;

public class Badge {
    private final String name;
    private final String description;
    private final int pointsRequired;

    public Badge(String name, String description, int pointsRequired) {
        this.name = name;
        this.description = description;
        this.pointsRequired = pointsRequired;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }
}
