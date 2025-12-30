package com.example.bookcare_qy;

public class Badge {
    private final String name;
    private final String description;
    private final int pointsRequired;
    private final int level;

    public Badge(String name, String description, int pointsRequired, int level) {
        this.name = name;
        this.description = description;
        this.pointsRequired = pointsRequired;
        this.level = level;
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

    public int getLevel() {
        return level;
    }
}
