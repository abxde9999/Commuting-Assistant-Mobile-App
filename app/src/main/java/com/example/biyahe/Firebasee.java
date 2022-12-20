package com.example.biyahe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Firebasee extends AppCompatActivity {

    DatabaseReference reference; //Declare Variable
    TextView pickUp, pickupRoute, dropOff, nextRoute, fare; //Decalre variable for Textviews
    String pickUpSt, pickupRouteSt, dropOffSt, nextRouteSt, fareSt; //Declare the String Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebasee);

        //Set the variable to the id of textview in Layout
        pickUp = findViewById(R.id.pickup_fill);
        //pickupRoute = findViewById(R.id.pic);
        dropOff = findViewById(R.id.dropoff_fill);
        nextRoute = findViewById(R.id.next_fill);
        fare = findViewById(R.id.fare_fill);

        showJourney(); //Method to show the  Info in the Layout
    }

    //Function to Locate the Database in the Firebase, Get the Value and Put in on String, Set the String to the Textview
    private void showJourney() {
        reference = FirebaseDatabase.getInstance().getReference().child("location_information"); //Locate the Database in the Firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Get the Value and set it to String
                pickUpSt = snapshot.child("pup_luneta").child("way").child("biyahe").child("pickup").getValue().toString();
                pickupRouteSt = snapshot.child("pup_luneta").child("way").child("biyahe").child("pu_route").getValue().toString();
                dropOffSt = snapshot.child("pup_luneta").child("way").child("biyahe").child("dropoff").getValue().toString();
                fareSt = snapshot.child("pup_luneta").child("way").child("biyahe").child("fare").getValue().toString();
                nextRouteSt = snapshot.child("pup_luneta").child("way").child("biyahe2").child("pu_route").getValue().toString();

                //Set the String Text to the Text View Variable
                pickUp.setText(pickUpSt);
                pickupRoute.setText(pickupRouteSt);
                dropOff.setText(dropOffSt);
                fare.setText(fareSt);
                nextRoute.setText(nextRouteSt);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}