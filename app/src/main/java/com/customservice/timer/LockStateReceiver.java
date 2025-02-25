package com.customservice.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LockStateReceiver extends BroadcastReceiver {
    private static final String TAG = "AnkitAppLock";
    private static final String ACTION_LOCK_STATE_CHANGED = "com.customservice.timer.ACTION_LOCK_STATE_CHANGED";
    private static final String EXTRA_LOCKED = "locked";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_LOCK_STATE_CHANGED.equals(intent.getAction())) {
            boolean isLocked = intent.getBooleanExtra(EXTRA_LOCKED, false);
            Log.d(TAG, "Lock state changed: " + (isLocked ? "LOCKED" : "UNLOCKED"));
            if (isLocked) {
                Log.d(TAG, "Status bar locked!");
            } else {
                Log.d(TAG, "Status bar unlocked!");
            }
        }
    }
}
