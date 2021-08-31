package com.example.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                }
                catch (InterruptedException e) {
                    Log.e("InterruptedException ","--> Problem in SlashScreenActivity.");
                }finally {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        thread.start();

    }
}