package com.customservice.timer;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LockAdminReceiver extends DeviceAdminReceiver {
    private static final String TAG = "AnkitAppAdminReceiver";

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        Log.d(TAG, "Kiosk mode cannot be disabled during active timer");
        return "Kiosk mode cannot be disabled during active timer";
    }
}
