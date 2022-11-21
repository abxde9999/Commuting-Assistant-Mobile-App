package com.example.biyahe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Account extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        navigationView = findViewById(R.id.bottomNav);

        navigationView.setSelectedItemId(R.id.account);

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