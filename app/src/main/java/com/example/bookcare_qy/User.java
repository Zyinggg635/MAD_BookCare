package com.example.bookcare_qy;

/**
 * User model class for Firebase Realtime Database
 */
public class User {
    public String userId;
    public String username;
    public String email;
    public String phone;
    public String address;
    public String genrePreference; // User's preferred genre
    public String age; // User's age
    public String bio; // User's bio
    public int totalPoints;
    public int booksDonated;
    public int booksExchanged;
    public int credits; // Credits for exchanging books (1 credit per uploaded book)
    public int badgeLevel; // Badge level (50 points per level, starts at 0)
    
    // Default constructor required for Firebase
    public User() {
        this.totalPoints = 0;
        this.booksDonated = 0;
        this.booksExchanged = 0;
        this.credits = 0;
        this.badgeLevel = 0;
    }
    
    public User(String userId, String username, String email, String phone, String address, String genrePreference) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.genrePreference = genrePreference;
        this.age = "";
        this.bio = "";
        this.totalPoints = 0;
        this.booksDonated = 0;
        this.booksExchanged = 0;
        this.credits = 0;
        this.badgeLevel = 0;
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
    
    public int getBooksDonated() { return booksDonated; }
    public void setBooksDonated(int booksDonated) { this.booksDonated = booksDonated; }
    
    public int getBooksExchanged() { return booksExchanged; }
    public void setBooksExchanged(int booksExchanged) { this.booksExchanged = booksExchanged; }
    
    public String getGenrePreference() { return genrePreference; }
    public void setGenrePreference(String genrePreference) { this.genrePreference = genrePreference; }
    
    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    
    public int getBadgeLevel() { return badgeLevel; }
    public void setBadgeLevel(int badgeLevel) { this.badgeLevel = badgeLevel; }
    
    /**
     * Calculate badge level based on total points (50 points per level, starting at level 0)
     */
    public void updateBadgeLevel() {
        this.badgeLevel = totalPoints / 50;
    }
}


