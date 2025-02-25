package com.customservice.timer.utils;

import android.app.Activity;
import android.view.View;

public class NavigationUtils {
    public static void hideNavigationBar(Activity activity) {
        activity.runOnUiThread(() -> {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        });
    }

    public static void showNavigationBar(Activity activity) {
        activity.runOnUiThread(() -> {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_VISIBLE
            );
        });
    }
}