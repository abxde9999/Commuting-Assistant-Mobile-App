package com.example.biyahe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    //Email Address Pattern
    public static final Pattern EMAIL_ADDRESS =
            Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    private TextView tvCallSigninAct;
    private TextInputLayout inputtxtemail;
    private Button btnSend;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        tvCallSigninAct = (TextView) findViewById(R.id.tvCallSigninAct);
        tvCallSigninAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSigninAct();
            }
        });
        inputtxtemail = (TextInputLayout) findViewById(R.id.inputtxtemail);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSend();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    public void callSigninAct() {
        startActivity(new Intent(ForgotPassword.this, Signin.class));
        finish();
    }

    private void userSend() {
        String email_address = inputtxtemail.getEditText().getText().toString().trim();
        if (email_address.isEmpty()) {
            inputtxtemail.setError("Email address is required!");
            inputtxtemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_address).matches()) {
            inputtxtemail.setError("Please provide a valid email address!");
            inputtxtemail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email_address).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Check your email address to change your password!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(ForgotPassword.this, Signin.class));
                    finish();
                }  else {
                    Toast.makeText(ForgotPassword.this, "Try again! Something wrong happened!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}