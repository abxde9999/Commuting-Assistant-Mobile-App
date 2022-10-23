package com.example.biyahe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Welcome extends AppCompatActivity {

    private Button btnCallSignupAct;
    private TextView tvCallSigninAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnCallSignupAct = (Button) findViewById(R.id.btnCallSignupAct);
        btnCallSignupAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSignupAct();
            }
        });

        tvCallSigninAct = (TextView) findViewById(R.id.tvCallSigninAct);
        tvCallSigninAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSigninAct();
            }
        });
    }

    //Call Signup Activity
    public void callSignupAct() {
        Intent intent  = new Intent(Welcome.this, Signup.class);
        startActivity(intent);
    }

    //Call Signin Activity
    public void callSigninAct() {
        Intent intent = new Intent(Welcome.this, Signin.class);
        startActivity(intent);
    }
}