package com.example.bookcare_qy;
import android.view.View;

import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;

public class ViewExtensions {

    /**
     * A helper method to reliably handle window insets for edge-to-edge display.
     * This applies initial padding and listens for future changes.
     */
    public static void setOnApplyWindowInsets(View view, OnApplyWindowInsetsListener listener) {
        // Set the listener for future changes
        ViewCompat.setOnApplyWindowInsetsListener(view, listener);

        // If the view is already attached, request the insets to be applied immediately.
        if (view.isAttachedToWindow()) {
            view.requestApplyInsets();
        }
        // Otherwise, set a listener to apply the insets when the view is attached.
        else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    v.removeOnAttachStateChangeListener(this);
                    v.requestApplyInsets();
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    // Do nothing
                }
            });
        }
    }
}