package com.amitbharat.keyboard;

import android.app.Application;
import com.google.android.material.color.DynamicColors;

public class KeyboardApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Material You Dynamic Colors across the application
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
