package com.example.bookcare_qy;

import java.util.Date;

/**
 * Model class for activity feed items (donations/exchanges)
 */
public class ActivityFeedItem {
    public String userId;
    public String username;
    public String action; // "donated" or "exchanged"
    public int bookCount;
    public int pointsEarned;
    public long timestamp;
    
    // Default constructor required for Firebase
    public ActivityFeedItem() {
    }
    
    public ActivityFeedItem(String userId, String username, String action, int bookCount, int pointsEarned) {
        this.userId = userId;
        this.username = username;
        this.action = action;
        this.bookCount = bookCount;
        this.pointsEarned = pointsEarned;
        this.timestamp = new Date().getTime();
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public int getBookCount() { return bookCount; }
    public void setBookCount(int bookCount) { this.bookCount = bookCount; }
    
    public int getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}


