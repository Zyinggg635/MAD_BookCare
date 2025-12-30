package com.example.bookcare_qy;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
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
     * Call ONCE in Application or MainActivity onCreate
     * Firebase auto-initializes from google-services.json
     */
    public static synchronized void initializeFirebase() {
        if (initialized) return;

        try {
            // Firebase auto-initializes from google-services.json
            // Just get instances - they will auto-initialize if not already done
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();
            
            // Set persistence enabled (must be called before any database reference usage)
            // Only set if not already set
            try {
                firebaseDatabase.setPersistenceEnabled(true);
                Log.d("FirebaseManager", "Database persistence enabled");
            } catch (Exception e) {
                // Persistence might already be enabled, which is fine
                Log.d("FirebaseManager", "Persistence: " + e.getMessage());
            }
            
            databaseReference = firebaseDatabase.getReference();
            initialized = true;
            Log.d("FirebaseManager", "Firebase initialized successfully");

        } catch (Exception e) {
            Log.e("FirebaseManager", "Firebase init error: " + e.getMessage(), e);
            // Continue anyway - Firebase might still work
            initialized = true; // Mark as initialized to avoid retrying
        }
    }

    public static FirebaseDatabase getDatabase() {
        if (!initialized) {
            initializeFirebase();
        }
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
        }
        return firebaseDatabase;
    }

    public static FirebaseAuth getAuth() {
        // FirebaseAuth auto-initializes, so we can always get an instance
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static DatabaseReference getReference() {
        if (!initialized) {
            initializeFirebase();
        }
        if (databaseReference == null) {
            databaseReference = getDatabase().getReference();
        }
        return databaseReference;
    }

    public static DatabaseReference getReference(String path) {
        return getDatabase().getReference(path);
    }
}
