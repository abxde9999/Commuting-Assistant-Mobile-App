package com.example.biyahe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

       new Handler().postDelayed(() -> {
           startActivity(new Intent(Main.this, Signin.class));
           finish();
       }, 3000);
    }

}