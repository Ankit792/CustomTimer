package com.customservice.timer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.math.MathServiceManager;
import android.util.Log;
import android.content.Context;
import android.os.RemoteException;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editTextHours, editTextMinutes, editTextSeconds;
    Button button;
    CountDownTimer countDownTimer;
    TextView textView2;
    private MathServiceManager mathService;
    private static final String TAG = "AnkitApp";

    Boolean timerActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        editTextHours = findViewById(R.id.editTextHours);
        editTextMinutes = findViewById(R.id.editTextMinutes);
        editTextSeconds = findViewById(R.id.editTextSeconds);
        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.textView2);

        mathService = (MathServiceManager) getSystemService(Context.MATH_SERVICE);

        if (mathService == null) {
            Log.e("AnkitApp", "Failed to retrieve MathService!");
        } else {
            Log.d("AnkitApp", "MathService successfully connected!");
        }
    }


    public void resetTimer() {
        editTextHours.setEnabled(true);
        editTextMinutes.setEnabled(true);
        editTextSeconds.setEnabled(true);
        updateTimer(0);
        button.setText("Start");
        timerActive = false;
        // Check if countDownTimer is already running
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
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
            timerActive = true;
            button.setText("Stop");

            // Disable the EditText fields so they can't be changed while the timer is running
            editTextHours.setEnabled(false);
            editTextMinutes.setEnabled(false);
            editTextSeconds.setEnabled(false);

            // Get the values from EditText with a check for empty values
            int hours = getValueFromEditText(editTextHours);
            int minutes = getValueFromEditText(editTextMinutes);
            int seconds = getValueFromEditText(editTextSeconds);

            // Convert the total time to seconds
            int totalTimeInSeconds = (hours * 3600) + (minutes * 60) + seconds;

            // Start the countdown timer
            countDownTimer = new CountDownTimer(totalTimeInSeconds * 1000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    textView2.setText("");
                    updateTimer((int) (millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    textView2.setText("Time Up");
                    if (mathService != null) {

                        int result = mathService.add(5, 10);
                        Log.d("AnkitApp", "Result of addition: " + result);

                        int result2 = mathService.sub(10, 5);
                        Log.d("AnkitApp", "Result of Substraction: " + result2);

                        int result3 = mathService.multiply(5, 10);
                        Log.d("AnkitApp", "Result of Multiplication: " + result3);
                    } else {
                        Log.e("AnkitApp", "MathService is not accessible!");
                    }
                    resetTimer();
                }

            };
            countDownTimer.start();
        }
    }

    // Helper method to get value from EditText, defaults to 0 if empty
    private int getValueFromEditText(EditText editText) {
        String text = editText.getText().toString();
        // If the EditText is empty, return 0, otherwise return the parsed value
        if (TextUtils.isEmpty(text)) {
            return 0;
        } else {
            return Integer.parseInt(text);
        }
    }
}
