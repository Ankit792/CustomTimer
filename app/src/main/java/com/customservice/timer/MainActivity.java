package com.customservice.timer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.math.MathServiceManager;
import android.util.Log;
import android.content.Context;
import android.app.lock.LockServiceManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import com.customservice.timer.utils.NavigationUtils;

import android.os.IBinder;  // For IBinder class
import android.os.ServiceManager;  // For ServiceManager class
import android.app.lock.ILockService;  // For your AIDL interface

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editTextHours, editTextMinutes, editTextSeconds;
    Button button;
    CountDownTimer countDownTimer;
    TextView textView2;
    private MathServiceManager mathService;
    private LockServiceManager mLockService;
    private static final String TAG = "AnkitApp";

    Boolean timerActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupServices();
        checkAdminPermissions();
    }

    private void initializeViews() {
        textView = findViewById(R.id.textView);
        editTextHours = findViewById(R.id.editTextHours);
        editTextMinutes = findViewById(R.id.editTextMinutes);
        editTextSeconds = findViewById(R.id.editTextSeconds);
        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.textView2);
    }

    private void setupServices() {
        mathService = (MathServiceManager) getSystemService(Context.MATH_SERVICE);
        mLockService = (LockServiceManager) getSystemService(Context.LOCK_SERVICE);
        // or With direct Binder access:
        // IBinder binder = ServiceManager.getService("lock_service");
        // if (binder != null) {
        //     mLockService = new LockServiceManager(this, ILockService.Stub.asInterface(binder));
        // } else {
        //     Log.e("AnkitApp", "Failed to get lock_service binder");
        // }
        if (mLockService == null) {
            Log.e("AnkitApp", "Failed to get LockServiceManager");
        } else {
            Log.d("AnkitApp", "LockService successfully connected!");
        }

        if (mathService == null) {
            Log.e("AnkitApp", "Failed to retrieve MathService!");
        } else {
            Log.d("AnkitApp", "MathService successfully connected!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAdminPermissions();
    }

    private void checkAdminPermissions() {
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName admin = new ComponentName(this, LockAdminReceiver.class);

        if (!dpm.isAdminActive(admin)) {
            showAdminActivationDialog();
        }

        if (dpm.isDeviceOwnerApp(getPackageName())) {
            Log.d(TAG, "Already device owner!");
            dpm.setActiveAdmin(admin, true); // Force activate admin
        } else {
            Log.e(TAG, "Not Device Owner! Check device_owner.xml");
        }
    }

    private void showAdminActivationDialog() {
        Toast.makeText(this, "Please enable device admin rights for this app", Toast.LENGTH_LONG).show();
        Intent activate = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        activate.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                new ComponentName(this, LockAdminReceiver.class));
        startActivity(activate);
    }

    public void resetTimer() {
        editTextHours.setEnabled(true);
        editTextMinutes.setEnabled(true);
        editTextSeconds.setEnabled(true);
        updateTimer(0);
        button.setText("Start");
        timerActive = false;

        if (mLockService != null) {
            try {
                mLockService.setLockState(false);
                NavigationUtils.showNavigationBar(this);
            } catch (Exception e) {
                Log.e(TAG, "Unlock failed", e);
            }
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void onLockClick(View view) {
        if (mLockService != null) {
            try {
                mLockService.setLockState(true);
                NavigationUtils.hideNavigationBar(this);
            } catch (Exception e) {
                Log.e(TAG, "Lock failed", e);
            }
        }
    }

    public void onUnlockClick(View view) {
        resetTimer();
    }

    public void updateTimer(int s) {
        int hours = s / 3600;
        int minutes = (s % 3600) / 60;
        int seconds = s % 60;
        String timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        textView.setText(timeLeft);
    }

    public void startTimer(View view) {
        if (timerActive) {
            resetTimer();
        } else {
            startNewTimer();
        }
    }

    private void startNewTimer() {
        timerActive = true;
        button.setText("Stop");
        disableInputFields();

        int totalSeconds = calculateTotalSeconds();

        countDownTimer = new CountDownTimer(totalSeconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView2.setText("");
                updateTimer((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                handleTimerCompletion();
            }
        }.start();
    }

    private int calculateTotalSeconds() {
        int hours = getValueFromEditText(editTextHours);
        int minutes = getValueFromEditText(editTextMinutes);
        int seconds = getValueFromEditText(editTextSeconds);
        return (hours * 3600) + (minutes * 60) + seconds;
    }

    private void disableInputFields() {
        editTextHours.setEnabled(false);
        editTextMinutes.setEnabled(false);
        editTextSeconds.setEnabled(false);
    }

    private void handleTimerCompletion() {
        textView2.setText("Time Up");

        if (mathService != null) {
            int result = mathService.add(5, 10);
            Log.d("AnkitApp", "Math result: " + result);
        }

        if (mLockService != null) {
            NavigationUtils.hideNavigationBar(MainActivity.this);
            try {
                mLockService.setLockState(true);
                mLockService.showLockMessage("Device locked - restart timer to unlock");
                // NavigationUtils.hideNavigationBar(MainActivity.this);
            } catch (Exception e) {
                Log.e(TAG, "Lock failed", e);
            }
        }
        startLockTask();
    }

    private int getValueFromEditText(EditText editText) {
        String text = editText.getText().toString();
        return TextUtils.isEmpty(text) ? 0 : Integer.parseInt(text);
    }
}