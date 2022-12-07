package com.example.biyahe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    TextView profileName, profileEmail, profilePhone;
    Button logoutButton;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhone = findViewById(R.id.profilePhone);

        profileName.setText(FetchData.currentUser.getFull_name());
        profileEmail.setText(FetchData.currentUser.getEmail_address());
        profilePhone.setText(FetchData.currentUser.getPhone_number());

        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, Signin.class));
                Toast.makeText(Profile.this, "Log out Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });





    }

}