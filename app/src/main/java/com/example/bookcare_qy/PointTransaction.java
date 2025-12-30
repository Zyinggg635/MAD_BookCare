package com.example.bookcare_qy;

import java.util.Date;

public class PointTransaction {
    private String userId;
    private int points;
    private String type; // e.g., "Donation", "Exchange"
    private Date timestamp;

    // Required default constructor for Firebase
    public PointTransaction() {}

    public PointTransaction(String userId, int points, String type, Date timestamp) {
        this.userId = userId;
        this.points = points;
        this.type = type;
        this.timestamp = timestamp;
    }

    // Getters
    public String getUserId() { return userId; }
    public int getPoints() { return points; }
    public String getType() { return type; }
    public Date getTimestamp() { return timestamp; }
}
