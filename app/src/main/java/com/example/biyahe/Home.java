package com.example.biyahe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewTreeViewModelKt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
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

import com.example.biyahe.model.AutocompleteEditText;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationSettingsRequest.Builder;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.gms.location.LocationListener;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Home extends FragmentActivity implements OnMapReadyCallback{
//
    private static final String TAG = "ADDRESS_AUTOCOMPLETE";
    private static final String MAP_FRAGMENT_TAG = "MAP";
    private AutocompleteEditText address1Field;
    private AutocompleteEditText address2Field;
    private LatLng coordinates;
    private boolean checkProximity = false;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private PlacesClient placesClient, placesClient2;
    private View mapPanel;
    private LatLng deviceLocation;
    private static final double acceptedProximity = 150;
    private String oAddress;

    LatLng originLoc;
    LatLng destinationLoc;

    View.OnClickListener startAutocompleteIntentListener = view -> {
        view.setOnClickListener(null);
        startAutocompleteIntent();
    };
    View.OnClickListener startAutocompleteIntentListener2 = view -> {
        view.setOnClickListener(null);
        startAutocompleteIntent2();
    };

    // [START maps_solutions_android_autocomplete_define]
    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResultCallback<ActivityResult>) result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);

                        // Write a method to read the address components from the Place
                        // and populate the form with the address components
                        Log.d(TAG, "Place: " + place.getAddressComponents());
                        fillInAddress(place);
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG, "User canceled autocomplete");
                }
            });
    private final ActivityResultLauncher<Intent> startAutocomplete2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResultCallback<ActivityResult>) result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);

                        // Write a method to read the address components from the Place
                        // and populate the form with the address components
                        Log.d(TAG, "Place: " + place.getAddressComponents());
                        fillInAddress2(place);
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG, "User canceled autocomplete");
                }
            });
//


    private static final String SOS_RECEIVER = "sos_receiver.txt";
    private static final String SOS_MESSAGE = "sos_message.txt";

    //Handler
    Handler mapHandler = new Handler();

    //Initialization of Strings
    String msgRc ;
    String msgI ;

    FloatingActionButton explore;
    BottomNavigationView bottomNav;
    Marker currentLocationMarker;
    Marker markerOrigin;
    Marker markerDestination;
    FloatingActionButton useCurrLoc;

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadSOS();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        onStartMap();
        startLoop();

        //Use Current Location
        useCurrLoc = findViewById(R.id.useCurrentLoc);
        useCurrLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useCurrLoc();
            }
        });

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
                        if (msgRc != "" && msgI != "") {
                            sendSOS();
                            Toast.makeText(Home.this, "SOS Message Sent!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(Home.this, "Please Set SOS Contact and Message first!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Home.this, SOS.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.account:
                        startActivity(new Intent(Home.this, Profile.class));
                        break;
                }
                return true;
            }
        });

//
        Places.initialize(getApplicationContext(), "AIzaSyBzKLXS2uFOSVE1Lhr3AOkDn1OkbKfo01M");
        // Retrieve a PlacesClient (previously initialized - see MainActivity)
        placesClient = Places.createClient(this);

        address1Field = findViewById(R.id.autocomplete_address1);
        address2Field = findViewById(R.id.autocomplete_address2);


        // Attach an Autocomplete intent to the Address 1 EditText field
        address1Field.setOnClickListener(startAutocompleteIntentListener);
        address2Field.setOnClickListener(startAutocompleteIntentListener2);
//

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        address1Field.setOnClickListener(startAutocompleteIntentListener);
        address2Field.setOnClickListener(startAutocompleteIntentListener2);
    }

    //
    //AutoComplete Destination


    // [START maps_solutions_android_autocomplete_intent]
    private void startAutocompleteIntent() {

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS_COMPONENTS,
                Place.Field.LAT_LNG, Place.Field.VIEWPORT);


        // Build the autocomplete intent with field, country, and type filters applied
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("PH")
                .setTypeFilter(TypeFilter.ADDRESS)
                .build(this);
        startAutocomplete.launch(intent);
    }
    private void startAutocompleteIntent2() {

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS_COMPONENTS,
                Place.Field.LAT_LNG, Place.Field.VIEWPORT);

        // Build the autocomplete intent with field, country, and type filters applied
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("PH")
                .setTypeFilter(TypeFilter.ADDRESS)
                .build(this);
        startAutocomplete2.launch(intent);
    }
    // [END maps_solutions_android_autocomplete_intent]



    private void fillInAddress(Place place) {
        AddressComponents components = place.getAddressComponents();
        LatLng latLng = place.getLatLng();
        StringBuilder address1 = new StringBuilder();
        StringBuilder postcode = new StringBuilder();

        // Get each component of the address from the place details,
        // and then fill-in the corresponding field on the form.
        // Possible AddressComponent types are documented at https://goo.gle/32SJPM1
        if (components != null) {
            for (AddressComponent component : components.asList()) {
                String type = component.getTypes().get(0);
                switch (type) {
                    case "street_number": {
                        address1.insert(0, component.getName());
                        break;
                    }

                    case "route": {
                        address1.append(" ");
                        address1.append(component.getShortName());
                        break;
                    }

                    case "postal_code": {
                        postcode.insert(0, component.getName());
                        break;
                    }

                    case "postal_code_suffix": {
                        postcode.append("-").append(component.getName());
                        break;
                    }

                }
            }
        }

        address1Field.setText(address1.toString());
        originLoc = latLng;
        showOrigin();
        // Add a map for visual confirmation of the address

    }
    private void fillInAddress2(Place place) {
        AddressComponents components = place.getAddressComponents();
        LatLng latLng = place.getLatLng();
        StringBuilder address1 = new StringBuilder();
        StringBuilder postcode = new StringBuilder();

        // Get each component of the address from the place details,
        // and then fill-in the corresponding field on the form.
        // Possible AddressComponent types are documented at https://goo.gle/32SJPM1
        if (components != null) {
            for (AddressComponent component : components.asList()) {
                String type = component.getTypes().get(0);
                switch (type) {
                    case "street_number": {
                        address1.insert(0, component.getName());
                        break;
                    }

                    case "route": {
                        address1.append(" ");
                        address1.append(component.getShortName());
                        break;
                    }

                    case "postal_code": {
                        postcode.insert(0, component.getName());
                        break;
                    }

                    case "postal_code_suffix": {
                        postcode.append("-").append(component.getName());
                        break;
                    }

                }
            }
        }

        address2Field.setText(address1.toString());
        destinationLoc = latLng;
        // Add a map for visual confirmation of the address
        showDestination();
    }

    // [START maps_solutions_android_autocomplete_map_add]
    public void showOrigin() {
        if (markerOrigin == null){
            markerOrigin = map.addMarker(new MarkerOptions().position(originLoc).title("Origin"));
            fitAllMarkers();
        } else{
            markerOrigin.setPosition(originLoc);
            fitAllMarkers();
        }
    }
    public void showDestination() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.checkered_flag);
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon,125,125, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        if (markerDestination == null ){
            markerDestination = map.addMarker(new MarkerOptions().position(destinationLoc).title("Destination").icon(smallMarkerIcon));
            fitAllMarkers();
        }else{
            markerDestination.setPosition(destinationLoc);
            fitAllMarkers();

        }
    }
    //
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
       map = googleMap;
       userLoc();
    }
    public void userLoc(){
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.person);
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon,125,125, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        if (currentLocationMarker == null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            currentLocationMarker =map.addMarker(new MarkerOptions().position(latLng).title("").icon(smallMarkerIcon));
        }else{
            currentLocationMarker.setPosition(latLng);
        }
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

                    if (!msgRc.isEmpty() && !msgI.isEmpty()){

                        //Initialize SMS Manager
                        SmsManager smsManager = SmsManager.getDefault();
                        //Send Msg
                        smsManager.sendTextMessage(msgRc,null,msgI+"\n"+ "Latitude: " + address.getLatitude() + "\nLongitude: " + address.getLongitude() + "\nAddress: " + address.getAddressLine(0) , null, null);}
                    else {

                        Toast.makeText(Home.this, "Please enter a message.", Toast.LENGTH_SHORT).show();
                    }

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
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    mapFragment.getMapAsync(Home.this);



                    //assert mapFragment != null;

                }
            }
        });
    }
    private void onStartMap() {
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
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
                    mapFragment.getMapAsync(Home.this);
                    //assert mapFragment != null;
                }
            }
        });
    }

    public void loadSOS(){
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
            if (fisMessage != null) {
                try {
                    fisMessage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void fitAllMarkers(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

//the include method will calculate the min and max bound.

        if (markerOrigin != null && markerDestination ==null){
            builder.include(markerOrigin.getPosition());
            builder.include(currentLocationMarker.getPosition());
        }else if (markerOrigin == null && markerDestination !=null){
            builder.include(markerDestination.getPosition());
            builder.include(currentLocationMarker.getPosition());
        }else if (markerOrigin == null && markerDestination == null){
            builder.include(currentLocationMarker.getPosition());
        }else{
            builder.include(markerOrigin.getPosition());
            builder.include(markerDestination.getPosition());
            builder.include(currentLocationMarker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        map.animateCamera(cu);
    }
    public void useCurrLoc(){

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
                        address1Field.setText(address.getAddressLine(0));
                        originLoc = new LatLng(address.getLatitude(),address.getLongitude());
                        showOrigin();
                }
            }
        });
    }
    public void calcDistance(){
        float[] results = new float[1];
        Location.distanceBetween(originLoc.latitude,originLoc.longitude,destinationLoc.latitude,destinationLoc.longitude,results);
        float distance = results[0];

        int kilometer = (int) (distance/1000);

        Toast.makeText(this, String.valueOf(kilometer)+ " km", Toast.LENGTH_SHORT).show();
    }
    public void startLoop(){
        mapRunnable.run();

    }
    public void stopLoop(){

    }
    private Runnable mapRunnable = new Runnable() {
        @Override
        public void run() {
            onStartMap();
            mapHandler.postDelayed(this, 1000);
        }
    };

}