package com.example.bookcare_qy;

/**
 * Constants class for shared Firebase paths and application values
 */
public class Constants {
    
    // Firebase Database URL
    public static final String FIREBASE_DATABASE_URL = "https://bookcare-82eb6-default-rtdb.asia-southeast1.firebasedatabase.app";
    
    // Database Paths
    public static final String PATH_USERS = "users";
    public static final String PATH_BOOKS = "books";
    public static final String PATH_POSTS = "posts";
    public static final String PATH_ACTIVITY_FEED = "activity_feed";
    public static final String PATH_POINT_TRANSACTIONS = "point_transactions";
    
    // Points Configuration
    public static final int POINTS_PER_BOOK_EXCHANGE = 5;
    public static final int POINTS_PER_BOOK_DONATION = 10;
    
    // Activity Feed Action Types
    public static final String ACTION_DONATED = "donated";
    public static final String ACTION_EXCHANGED = "exchanged";
    
    // Intent Extras
    public static final String EXTRA_BOOK_COUNT = "BOOK_COUNT";
    public static final String EXTRA_ACTION_TYPE = "ACTION_TYPE"; // "donation" or "exchange"
}


