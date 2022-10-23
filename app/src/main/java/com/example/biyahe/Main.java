package com.example.biyahe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Main extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Signin.PREF_NAME, 0);
                boolean hasSignedIn = sharedPreferences.getBoolean("hasSignedIn", false);

                if (hasSignedIn) {
                    Intent intent = new Intent(Main.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Main.this, Welcome.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIMEOUT);
    }
}