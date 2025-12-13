package com.example.bookcare_qy;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    private static FirebaseDatabase firebaseDatabase;
    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference databaseReference;
    private static boolean initialized = false;

    /**
     * Initialize Firebase safely
     * Call ONCE in Application or MainActivity
     */
    public static synchronized void initializeFirebase() {
        if (initialized) return;

        try {
            firebaseDatabase = FirebaseDatabase.getInstance();

            // MUST be before any database reference usage
            firebaseDatabase.setPersistenceEnabled(true);

            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = firebaseDatabase.getReference();

            initialized = true;
            Log.d("FirebaseManager", "Firebase initialized successfully");

        } catch (Exception e) {
            Log.e("FirebaseManager", "Firebase init error", e);
        }
    }

    public static FirebaseDatabase getDatabase() {
        if (!initialized) initializeFirebase();
        return firebaseDatabase;
    }

    public static FirebaseAuth getAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static DatabaseReference getReference() {
        if (!initialized) initializeFirebase();
        return databaseReference;
    }

    public static DatabaseReference getReference(String path) {
        if (!initialized) initializeFirebase();
        return firebaseDatabase.getReference(path);
    }
}
