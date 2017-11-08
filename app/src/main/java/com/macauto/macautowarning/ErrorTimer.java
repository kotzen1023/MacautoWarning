package com.macauto.macautowarning;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ErrorTimer extends Activity{
    private static final String TAG = ErrorTimer.class.getName();

    static SharedPreferences pref ;
    static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";
    private Long startTime;
    //private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_timer);

        Window window;

        window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color_menu_classic));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color_menu_classic, getTheme()));
        }

        pref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        startTime = pref.getLong("ERROR_TIME_DELAY", System.currentTimeMillis()+301000);

        MyCount timercount = new MyCount(startTime - System.currentTimeMillis(), 1000);

        Log.e(TAG, "start time = "+startTime+", current = "+System.currentTimeMillis());

        timercount.start();

        //handler.removeCallbacks(updateTimer);
        //設定Delay的時間
        //handler.postDelayed(updateTimer, 1000);


    }

    private class MyCount extends CountDownTimer
    {
        private MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            NumberFormat f = new DecimalFormat("00");
            final TextView time = findViewById(R.id.timer);
            Long spentTime = startTime - System.currentTimeMillis() ;

            Long minius = (spentTime/1000)/60;

            Long seconds = (spentTime/1000) % 60;
            String timeString = f.format(minius)+":"+f.format(seconds);
            time.setText(timeString);
        }

        @Override
        public void onFinish() {
            editor = pref.edit();
            editor.putBoolean("LOGIN_ERROR", false);
            editor.apply();

            Intent mainIntent = new Intent(ErrorTimer.this, LoginActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}
