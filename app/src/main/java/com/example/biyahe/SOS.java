package com.example.biyahe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class SOS extends AppCompatActivity {
    BottomNavigationView navigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        navigationView = findViewById(R.id.bottomNav);
        navigationView.setSelectedItemId(R.id.sos);

        navigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.home:
                    startActivity(new Intent(getApplicationContext()
                            ,Home.class));
                    return true;

                case R.id.sos:
                    startActivity(new Intent(getApplicationContext()
                            ,SOS.class));
                    return true;

                case R.id.account:
                    startActivity(new Intent(getApplicationContext()
                            ,Account.class));
                    return true;
            }
            return true;
    });
}
}