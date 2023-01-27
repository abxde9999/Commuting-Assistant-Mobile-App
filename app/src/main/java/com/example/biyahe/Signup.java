package com.example.biyahe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    //Email Address Pattern
    public static final Pattern EMAIL_ADDRESS =
            Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    //Password Pattern
    public static final Pattern PASSWORD =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+=])(?=\\S+$).{8,}$");

    //Phone Number Pattern
    public static final Pattern PHONE_NUMBER =
            Pattern.compile("^(09|\\+639)\\d{9}$");

    TextInputLayout inputtxtfname, inputtxtemail, inputtxtphonenum, inputtxtpassword;
    private Button btnSignup;
    private ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        inputtxtfname = (TextInputLayout) findViewById(R.id.inputtxtfname);
        inputtxtemail = (TextInputLayout) findViewById(R.id.inputtxtemail);
        inputtxtphonenum= (TextInputLayout) findViewById(R.id.inputtxtphonenum);
        inputtxtpassword = (TextInputLayout) findViewById(R.id.inputtxtpassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignup();
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private void userSignup() {
        String full_name = inputtxtfname.getEditText().getText().toString().trim();
        String email_address = inputtxtemail.getEditText().getText().toString().trim();
        String phone_number = inputtxtphonenum.getEditText().getText().toString().trim();
        String password = inputtxtpassword.getEditText().getText().toString().trim();

        if (full_name.isEmpty()) {
            Toast.makeText(this, "name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email_address.isEmpty()) {
            Toast.makeText(this, "email address is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!EMAIL_ADDRESS.matcher(email_address).matches()) {
            Toast.makeText(this, "please provide a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone_number.isEmpty()) {
            Toast.makeText(this, "phone number is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!PHONE_NUMBER.matcher(phone_number).matches()) {
            Toast.makeText(this, "please provide a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "password is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!PASSWORD.matcher(password).matches()) {
            Toast.makeText(this, "must be at least 8 characters and must have a uppercase letter, lowercase letter, special character, and number", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email_address, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(full_name, email_address, phone_number);

                            FirebaseDatabase.getInstance().getReference("user_information").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Signup.this, "Sign up Successful!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Signup.this, Signin.class);
                                                intent.putExtra("finish", true); // To close all the Activities(history)
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                                progressBar.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(Signup.this, "Sign up Failed!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Signup.this, "Email address had already been taken!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}