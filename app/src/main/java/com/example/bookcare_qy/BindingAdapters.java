package com.example.bookcare_qy;

import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.bookcare_qy.R;

public class BindingAdapters {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, Uri uri) {
        if (uri != null) {
            // If a URI is provided (the user picked an image), load it.
            view.setImageURI(uri);
        } else {
            // If the URI is null, show the default placeholder icon.
            view.setImageResource(R.drawable.ic_account_circle);
        }
    }
}
