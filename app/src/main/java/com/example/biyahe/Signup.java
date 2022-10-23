package com.example.biyahe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.UUID;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    //Email Address Pattern
    public static final Pattern EMAIL_ADDRESS =
            Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    //Password Pattern
    public static final Pattern PASSWORD =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    //Phone Number pattern
    public static final Pattern PHONE_NUMBER =
            Pattern.compile("^(?=.*[0-9])(?=\\S+$).{11,}$");

    TextInputLayout inputtxtfname, inputtxtemail, inputtxtphonenum, inputtxtpassword;
    Button btnSignup;
    private TextView tvCallWelcomeAct;
    private ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        tvCallWelcomeAct = (TextView) findViewById(R.id.tvCallWelcomeAct);
        tvCallWelcomeAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callWelcomeAct();
            }
        });

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignup();
            }
        });

        inputtxtfname = (TextInputLayout) findViewById(R.id.inputtxtfname);
        inputtxtemail = (TextInputLayout) findViewById(R.id.inputtxtemail);
        inputtxtphonenum= (TextInputLayout) findViewById(R.id.inputtxtphonenum);
        inputtxtpassword = (TextInputLayout) findViewById(R.id.inputtxtpassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void callWelcomeAct() {
        startActivity(new Intent(Signup.this, Welcome.class));
        finish();
    }

    private void userSignup() {
        String full_name = inputtxtfname.getEditText().getText().toString().trim();
        String email_address = inputtxtemail.getEditText().getText().toString().trim();
        String phone_number = inputtxtphonenum.getEditText().getText().toString().trim();
        String password = inputtxtpassword.getEditText().getText().toString().trim();

        if (full_name.isEmpty()) {
            inputtxtfname.setError("Name is required!");
            inputtxtfname.requestFocus();
            return;
        }

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

        if (phone_number.isEmpty()) {
            inputtxtphonenum.setError("Phone number is required!");
            inputtxtphonenum.requestFocus();
            return;
        }

        if (!PHONE_NUMBER.matcher(phone_number).matches()) {
            inputtxtphonenum.setError("Please provide a valid phone number!");
            inputtxtphonenum.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            inputtxtpassword.setError("Password is required!");
            inputtxtpassword.requestFocus();
            return;
        }

        if (!PASSWORD.matcher(password).matches()) {
            inputtxtpassword.setError("Password Strength: WEAK!");
            inputtxtpassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email_address, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(full_name, email_address, phone_number);

                            FirebaseDatabase.getInstance().getReference("user_information")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Signup.this, "Sign up Successfully!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(new Intent(Signup.this, Signin.class));
                                                finish();
                                            } else {
                                                Toast.makeText(Signup.this, "Sign up Failed!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Signup.this, "Email address has already been taken!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}