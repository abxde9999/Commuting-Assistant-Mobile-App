package com.example.biyahe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Account extends AppCompatActivity {

    private TextView tvCallHomeAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        tvCallHomeAct = (TextView) findViewById(R.id.tvCallHomeAct);
        tvCallHomeAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callHomeAct();
            }
        });
    }

    //Call Home Activity
    public void callHomeAct() {
        Intent intent = new Intent(Account.this, Home.class);
        startActivity(intent);
    }
}