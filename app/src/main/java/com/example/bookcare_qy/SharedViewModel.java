package com.example.bookcare_qy;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    // Fields are public, final, and MutableLiveData to support two-way data binding.
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> age = new MutableLiveData<>();
    public final MutableLiveData<String> bio = new MutableLiveData<>();
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> phone = new MutableLiveData<>();
    public final MutableLiveData<String> location = new MutableLiveData<>();

    // --- START: ADDED FOR PROFILE PICTURE ---
    public final MutableLiveData<Uri> profileImageUri = new MutableLiveData<>();
    // --- END: ADDED FOR PROFILE PICTURE ---

    // The constructor where we can set initial/default values for demonstration.
    public SharedViewModel() {
        name.setValue("Alex Green");
        age.setValue("25");
        bio.setValue("Book lover and eco-warrior");
        email.setValue("alex@example.com");
        phone.setValue("+1 (555) 123-4567");
        location.setValue("San Francisco, CA");
        profileImageUri.setValue(null); // Initially, there is no image
    }

    // Getters and Setters are no longer needed for data binding.
}


