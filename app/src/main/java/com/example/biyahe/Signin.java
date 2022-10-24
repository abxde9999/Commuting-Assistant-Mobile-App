package com.example.biyahe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class Signin extends AppCompatActivity {

    //Email Address Pattern
    public static final Pattern EMAIL_ADDRESS =
            Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    private TextInputLayout inputtxtemail, inputtxtpassword;
    private Button btnSignin;
    private TextView tvCallForgotPassword, tvCallSignupAct;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    public static String PREF_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        inputtxtemail = findViewById(R.id.inputtxtemail);
        inputtxtpassword = findViewById(R.id.inputtxtpassword);
        progressBar = findViewById(R.id.progressBar);

        tvCallForgotPassword = findViewById(R.id.tvCallForgotPassword);
        tvCallForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callForgotPassword();
            }
        });

        btnSignin = findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignin();
            }
        });

        tvCallSignupAct = findViewById(R.id.tvCallSignupAct);
        tvCallSignupAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSignupAct();
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    public void callForgotPassword() {
        startActivity(new Intent(Signin.this, ForgotPassword.class));
    }

    public void callSignupAct() {
        startActivity(new Intent(Signin.this, Signup.class));
    }

    private void userSignin() {
        String email_address = inputtxtemail.getEditText().getText().toString().trim();
        String password = inputtxtpassword.getEditText().getText().toString().trim();

        if (email_address.isEmpty()) {
            Toast.makeText(this, "please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_address).matches()) {
            Toast.makeText(this, "please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Oops! you forgot to enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email_address,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        Toast.makeText(Signin.this, "Sign in Succesfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signin.this, Home.class));
                        finish();
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(Signin.this, "Sign in Failed! Please verify your email address!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(Signin.this, "Sign in Failed!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    //Skip SignIn and SignUp if already SignedIn
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Signin.this, Home.class));
        }
    }
}