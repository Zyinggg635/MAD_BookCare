package com.example.bookcare_qy;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDatabaseHelper {

    private DatabaseReference databaseReference;

    public FirebaseDatabaseHelper() {
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void updateWithTimestamp(String path) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("updatedAt", ServerValue.TIMESTAMP);

        databaseReference.child(path).updateChildren(updates);
    }
}
