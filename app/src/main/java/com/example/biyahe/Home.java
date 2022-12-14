package com.example.biyahe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewTreeViewModelKt;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class Home extends FragmentActivity implements OnMapReadyCallback {
    private static final String SOS_RECEIVER = "sos_receiver.txt";
    private static final String SOS_MESSAGE = "sos_message.txt";

    //Initialization of Strings
    String msgRc ;
    String msgI ;

    FloatingActionButton explore;
    BottomNavigationView bottomNav;


    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FileInputStream fisReceiver = null;

        try {
            fisReceiver = openFileInput(SOS_RECEIVER);
            InputStreamReader isrReceiver = new InputStreamReader(fisReceiver);
            BufferedReader brReceiver = new BufferedReader(isrReceiver);
            StringBuilder sbReceiver = new StringBuilder();
            String msgR;

            while ((msgR = brReceiver.readLine()) != null){
                sbReceiver.append(msgR).append("\n");
            }
            msgRc = sbReceiver.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fisReceiver != null) {
                try {
                    fisReceiver.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        FileInputStream fisMessage = null;

        try {
            fisMessage = openFileInput(SOS_MESSAGE);
            InputStreamReader isrMessage = new InputStreamReader(fisMessage);
            BufferedReader brReceiver = new BufferedReader(isrMessage);
            StringBuilder sbMessage = new StringBuilder();
            String msgM;

            while ((msgM = brReceiver.readLine()) != null){
                sbMessage.append(msgM).append("\n");
            }
            msgI = sbMessage.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fisReceiver != null) {
                try {
                    fisReceiver.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Get Location
        explore = findViewById(R.id.explore);
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explore();
            }
        });

        // Bottom Navigation Bar
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sos:
                        sendSOS();
                        Toast.makeText(Home.this, "SOS Message Sent!", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.account:
                        startActivity(new Intent(Home.this, Profile.class));
                        break;
                }
                return true;
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check Condition
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //When permission is Granted
            //Call Method
            sendSOS();
        }else {
            //When permission is Denied
            //Display Toast
            Toast.makeText(this, "Permission Denied, Please Grant!", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendSOS() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Address address = null;
                    Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                        address = addresses.get(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //if (!msgR.isEmpty() && !msgI.isEmpty()){
                        //String message = "SOS";
                        //String receiver = "09430940014";
                        //Initialize SMS Manager
                        SmsManager smsManager = SmsManager.getDefault();
                        //Send Msg
                        smsManager.sendTextMessage(msgRc,null,msgI+"\n"+ "Latitude: " + address.getLatitude() + "\nLongitude: " + address.getLongitude() + "\nAddress: " + address.getAddressLine(0) , null, null);

                }
            }
        });


    }


    // Method to get Current Location
    private void explore() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Address address = null;
                    Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                        address = addresses.get(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "Latitude: " + address.getLatitude() + "\nLongitude: " + address.getLongitude() + "\nCountry: " + address.getCountryName() + "\nAddress: " + address.getAddressLine(0) + "\nLocality: " + address.getLocality(), Toast.LENGTH_LONG).show();

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
                    mapFragment.getMapAsync(Home.this);
                    //assert mapFragment != null;

                }
            }
        });
    }


    //Get Current Location
    /*private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Address address = null;
                    Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                        address = addresses.get(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "Latitude: " + address.getLatitude() + "\nLongitude: " + address.getLongitude() + "\nCountry: " + address.getCountryName() + "\nAddress: " + address.getAddressLine(0) + "\nLocality: " + address.getLocality(), Toast.LENGTH_LONG).show();


                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
                    //assert mapFragment != null;
                    mapFragment.getMapAsync(Home.this);
                }
            }
        });
    }*/


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                    break;
        }
    }*/

}