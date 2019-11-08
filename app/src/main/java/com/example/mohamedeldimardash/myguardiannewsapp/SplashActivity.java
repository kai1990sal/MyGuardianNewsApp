package com.example.mohamedeldimardash.myguardiannewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_TIME_OUT = 3500;
        new Handler().postDelayed(new Runnable() {

            /** Display splash screen with a timer */
            @Override
            public void run() {
                // This method executes once the timer is over
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                // Close splash activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}


