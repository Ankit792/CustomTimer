package com.customservice.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    SeekBar seekBar;
    Button button;
    CountDownTimer countDownTimer;
    TextView textView2;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView=(TextView) findViewById(R.id.textView);
        seekBar = (SeekBar)findViewById(R.id.seekBar);


        seekBar.setMax(200000);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void resetTimer(){
        seekBar.setEnabled(true);
        seekBar.setProgress(12000);
        updateTimer(12000);
        button=(Button)findViewById(R.id.button);
        button.setText("Start");
        timerActive=false;
        countDownTimer.cancel();

        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.alert);
        mediaPlayer.start();
    }

    public void updateTimer(int s){

        int minutes =s/60000;
        int seconds = (s/1000) - (minutes*60);
        String secString="";
        if(seconds<10){
            secString="0"+seconds;
        }else {
            secString=Integer.toString(seconds);
        }



        String timeleft = minutes +":"+secString;

        textView.setText(timeleft);

    }
    Boolean timerActive=false;
    public void startTimer(View view) {
//        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.Beeps18);


        textView=(TextView) findViewById(R.id.textView);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        button=(Button)findViewById(R.id.button);
        textView2=(TextView)findViewById(R.id.textView2);

        if(timerActive){

            resetTimer();

        }else{
            timerActive=true;
            button.setText("Stop");
            seekBar.setEnabled(false);
            int timer_set = seekBar.getProgress();
            countDownTimer=new CountDownTimer(timer_set,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    textView2.setText("");
                    updateTimer((int) millisUntilFinished);

                }

                @Override
                public void onFinish() {

                    textView2.setText("Time Up");
                    resetTimer();

                }
            };
            countDownTimer.start();
        }
    }
}
