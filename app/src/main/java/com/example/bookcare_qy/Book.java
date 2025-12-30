package com.example.bookcare_qy;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public String id;
    public String title;
    public String author;
    public String condition;
    public String listingType; 
    public String genre;       
    public String ownerId; // New field to track the owner

    public Book() {
        // Default constructor required for Firebase
    }

    public Book(String id, String title, String author, String condition, String listingType, String genre, String ownerId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.condition = condition;
        this.listingType = listingType;
        this.genre = genre;
        this.ownerId = ownerId;
    }

    // --- Parcelable Implementation ---
    protected Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        author = in.readString();
        condition = in.readString();
        listingType = in.readString();
        genre = in.readString();
        ownerId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(condition);
        dest.writeString(listingType);
        dest.writeString(genre);
        dest.writeString(ownerId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public String getListingType() { return listingType; }
    public void setListingType(String listingType) { this.listingType = listingType; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
}
