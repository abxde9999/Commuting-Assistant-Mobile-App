package com.example.biyahe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.telephony.SmsManager;
import android.util.Log;

import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.biyahe.Utility.NetworkChangeListener;
import com.example.biyahe.model.AutocompleteEditText;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest.Builder;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
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
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Home extends FragmentActivity implements OnMapReadyCallback {

    String[] DList;

    String biyahe, NextRT, NullCheck, NRTcheck;
    Marker Pickup, DropOff;
    DatabaseReference reference; //Declare Variable
    TextView pickUp, pickupRoute, dropOff, nextRoute, fare, distancePU, slide_origin, slide_dest, slide_fare; //Declare variable for Textviews
    String pickUpSt, pickupRouteSt, dropOffSt, nextRouteSt, fareSt, pu_lat, pu_lng, do_lat, do_lng, o_lat, o_lng, d_lat, d_lng, trip_dest, trip_org, total_fare; //Declare the String Variable

    String PlaceKey, TripID;


    float distance, speed;
    int meters, kmh, fare_total;


    String status;

    double pu_latD;
    double pu_lngD;
    LatLng puLatLng;
    double do_latD;
    double do_lngD;
    LatLng doLatLng;

    LatLng PuDoLatLng;

    int PuDo, swap, nav;
    int ctr, mapLoad = 0;
    int centerCtr = 1;
    double DisPU;

    //Geofencing
    private GeofencingClient geofencingClient;
    private GeofencingHelper geofencingHelper;
    private float GEOFENCE_RADIUS = 200;
    private String GEOFENCE_ID = "GEOFENCE";
    Circle geofenceBounds;

    Geofence HomeGeofence;

    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    //Real-time Location

    LocationRequest locationRequest;
    Location locationUser;

    int priority;


    //
    private static final String TAG2 = "ADDRESS_AUTOCOMPLETE";
    private static final String TAG = "MapsActivity";
    private static final String MAP_FRAGMENT_TAG = "MAP";
    private AutocompleteEditText address3Field;
    private LatLng coordinates;
    private boolean checkProximity = false;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private PlacesClient placesClient, placesClient2;
    private View mapPanel;
    private LatLng deviceLocation;
    private static final double acceptedProximity = 150;
    private String oAddress;

    double NortheastLat = 14.640519;
    double NortheastLong = 120.941203;
    double SouthWestLat = 14.553814;
    double SouthWestLong = 121.026003;

    TextView currLoc;
    ConstraintLayout constraintLayout2, constraintLayout3;
    LinearLayout constraintLayout;
    SlidingUpPanelLayout slidingUpPanelLayout;

    //Nearby
    double nearbyLatitude, nearbyLongitude;
    String nearbyPlace, durationDes, distanceDes;


    LatLng Southwest = new LatLng(SouthWestLat, SouthWestLong);
    LatLng Northeast = new LatLng(NortheastLat, NortheastLong);
    LatLng cameraLatLng;
    LatLng newCameraLatLng = null;
    LatLng userLoc;

    int nearby = 0;
    int PROXIMITY_RADIUS = 5000;

    double originLat;
    double originLong;

    double destinationLat;
    double destinationLong;


    LocationRestriction locationRestriction;
    LatLng originLoc;
    LatLng destinationLoc;
    LocationCallback locationCallback;
    // [START maps_solutions_android_autocomplete_define]
    private final ActivityResultLauncher<Intent> startAutocomplete3 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResultCallback<ActivityResult>) result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);

                        // Write a method to read the address components from the Place
                        // and populate the form with the address components
                        Log.d(TAG2, "Place: " + place.getAddressComponents());
                        search(place);
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG2, "User cancelled autocomplete");
                }
            });
//


    private static final String SOS_RECEIVER = "sos_receiver.txt";
    private static final String SOS_MESSAGE = "sos_message.txt";

    //Handler
    Handler mapHandler = new Handler();
    Handler animationHandler = new Handler();
    Handler navHandler = new Handler();
    Handler DistHandler = new Handler();
    int delayMillis = 5000;

    //Initialization of Strings
    String msgRc;
    String msgI;


    ExtendedFloatingActionButton startTrip, hospitals, schools, restaurants, endTrip, confirm;
    FloatingActionButton explore;
    BottomNavigationView bottomNav;
    Marker currentLocationMarker;
    Marker markerOrigin;
    Marker markerDestination;
    Marker userLocationMarker;
    Marker searchMarker;
    Circle userLocationAccuracyCircle;
    FloatingActionButton useCurrLoc;

    private Geocoder geocoder;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener(); //initialization of no wifi popup

    //New search view
    DatabaseReference ref;
    private AutoCompleteTextView searchTrip;
    private RecyclerView listTrips;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize new search view
        ref = FirebaseDatabase.getInstance().getReference("location_information");
        searchTrip = (AutoCompleteTextView) findViewById(R.id.searchTrip);
        listTrips = (RecyclerView) findViewById(R.id.listTrips);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        listTrips.setLayoutManager(layoutManager);

        //Method for new search view
        populateSearch();

        //Set the variable to the id of textview in Layout
        pickUp = findViewById(R.id.pickup_fill);
        //pickupRoute = findViewById(R.id.pic);
        dropOff = findViewById(R.id.dropoff_fill);
        nextRoute = findViewById(R.id.next_fill);
        fare = findViewById(R.id.fare_fill);
        distancePU = findViewById(R.id.distance_fill);
        slide_origin = findViewById(R.id.slide_origin);
        slide_dest = findViewById(R.id.slide_dest);
        slide_fare = findViewById(R.id.slide_fare);
        confirm = findViewById(R.id.confirm_button);
        confirm.setVisibility(View.GONE);


        startTrip = findViewById(R.id.startTrip);
        startTrip.setVisibility(View.GONE);

        endTrip = findViewById(R.id.endTrip);
        endTrip.setVisibility(View.GONE);

        currLoc = findViewById(R.id.currLoc);
        constraintLayout = findViewById(R.id.cLayout);
        constraintLayout.setVisibility(View.GONE);

        constraintLayout2 = findViewById(R.id.cLayout2);
        constraintLayout2.setVisibility(View.GONE);

        constraintLayout3 = findViewById(R.id.cLayout3);
        constraintLayout3.setVisibility(View.GONE);

        slidingUpPanelLayout = findViewById(R.id.sliding_layout);

        restaurants = findViewById(R.id.restaurants);
        restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRestaurants();
                setUserLocationMarker(locationUser);
            }
        });
        hospitals = findViewById(R.id.hospitals);
        hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHospitals();
                setUserLocationMarker(locationUser);
            }
        });
        schools = findViewById(R.id.schools);
        schools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSchools();
                setUserLocationMarker(locationUser);
            }
        });

        loadSOS();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofencingHelper = new GeofencingHelper(this);

        geocoder = new Geocoder(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        priority = Priority.PRIORITY_HIGH_ACCURACY;
        locationRequest.setPriority(priority);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);


        startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startStartTrip();

            }
        });
        endTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                endTrip();

            }
        });

        explore = findViewById(R.id.explore);
        onStartMap();


        // Get Location


        // Bottom Navigation Bar
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sos:
                        if (msgRc != "" && msgI != "") {
                            //Check permission
                            if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.SEND_SMS)
                                    == PackageManager.PERMISSION_GRANTED) {
                                //When permission is granted
                                //Create a Method

                                sendSOS();
                            } else {
                                //When Permission is not Granted
                                //Request for Permission
                                ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.SEND_SMS},
                                        100);
                            }
                            Toast.makeText(Home.this, "SOS Message Sent!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Home.this, "Please Set SOS Contact and Message first!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Home.this, SOS.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.account:
                        startActivity(new Intent(Home.this, Profile.class));
                        break;
                    case R.id.my_place:
                        myPlace();
                        animateMyPlaceIn();
                        animateMyPlaceOut();
                        break;
                    case R.id.search:
                        startAutocompleteIntent3();
                        break;
                    case R.id.nearby:
                        if (nearby == 0) {
                            animateNearbyIn();
                            hospitals.setClickable(true);
                            schools.setClickable(true);
                            restaurants.setClickable(true);
                            nearby = 1;
                        } else {
                            animateNearbyOut();
                            hospitals.setClickable(false);
                            schools.setClickable(false);
                            restaurants.setClickable(false);
                            nearby = 0;
                        }
                        break;

                }
                return true;
            }
        });

        Places.initialize(getApplicationContext(), "AIzaSyA9y3TlxDiPcFrRXML4EbjGeJqr7h6f308");
        // Retrieve a PlacesClient (previously initialized - see MainActivity)
        placesClient = Places.createClient(this);

    }

    //Method for new search view
    private void populateSearch() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> places = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String p = ds.child("place").getValue(String.class);
                        places.add(p);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, places);
                    searchTrip.setAdapter(adapter);
                    searchTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            String selection = parent.getItemAtPosition(position).toString();
                            getLocation_info(selection);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    private void getLocation_info(String selection) {

        Query query = ref.orderByChild("place").equalTo(selection);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<LocationDetails> locDetails = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        LocationDetails locationDetails = new LocationDetails(ds.child("place").getValue(String.class)
                                , ds.getKey());
                        locDetails.add(locationDetails);

                        PlaceKey = ds.getKey();
                        PlaceKeyShow();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(eventListener);
    }

    public void PlaceKeyShow(){
        if(startTrip.getVisibility() == View.VISIBLE){
            startTrip.setVisibility(View.GONE);
        }
        biyahe = "biyahe";
        NextRT = "biyahe2";
        nav = 0;
        TripDataLoader();
        confirm.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirm();

            }
        });


        //Toast.makeText(this,PlaceKey, Toast.LENGTH_LONG).show();
    }

    class LocationDetails {
        public String getPlace() {
            return place;
        }
        public String place;
        public String key;

        public LocationDetails(String place, String key) {
            this.place = place;
            this.key = key;
        }
    }

    //Function for no wifi popup
    @Override
    protected void onStart(){
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    //Function for no wifi popup
    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @SuppressLint("MissingPermission")
    private void addGeofence(LatLng latLng, float radius) {


        Geofence geofence = geofencingHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofencingHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofencingHelper.getPendingIntent();

        geofencingClient.removeGeofences(geofencingHelper.getPendingIntent()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: Existing Geofence Removed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Error" );
            }
        });
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: Geofence Added...");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String errorMessage = geofencingHelper.getErrorString(e);
                            Log.d(TAG, "onFailure: " + errorMessage);
                        }
                    });

            HomeGeofence =geofence;

    }
    //
    //AutoComplete Destination


    // [START maps_solutions_android_autocomplete_intent]

    private void startAutocompleteIntent3() {

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS_COMPONENTS,
                Place.Field.LAT_LNG, Place.Field.VIEWPORT);

        // Build the autocomplete intent with field, country, and type filters applied
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("PH")
                .build(this);
        startAutocomplete3.launch(intent);
    }
    // [END maps_solutions_android_autocomplete_intent]

    private void search(Place place){
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

        String address = address1.toString();
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.search);
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon, 150, 150, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        if (searchMarker == null) {
            searchMarker = map.addMarker(new MarkerOptions().position(latLng).title(address).icon(smallMarkerIcon));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        } else {
            searchMarker.remove();
            searchMarker = map.addMarker(new MarkerOptions().position(latLng).title(address).icon(smallMarkerIcon));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        }

    }

    // [START maps_solutions_android_autocomplete_map_add]
    public void showOrigin() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon, 125, 125, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        if (markerOrigin == null) {
            markerOrigin = map.addMarker(new MarkerOptions().position(originLoc).title("Origin").icon(smallMarkerIcon));
            fitAllMarkers();
        } else {
            markerOrigin.setPosition(originLoc);
            fitAllMarkers();
        }
    }

    public void showDestination() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.checkered_flag);
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon, 125, 125, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        if (markerDestination == null) {
            markerDestination = map.addMarker(new MarkerOptions().position(destinationLoc).title(trip_dest).icon(smallMarkerIcon).snippet("Destination"));
            fitAllMarkers();
        } else {
            markerDestination.setPosition(destinationLoc);
            fitAllMarkers();

        }
    }


    //
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        if(map == googleMap && mapLoad == 0){
            map.setIndoorEnabled(true);
            map.setTrafficEnabled(true);
            map.setBuildingsEnabled(true);
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
            mapLoad = 1;
        }
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
                if (map != null) {

                    startLocationUpdates();
                    locationUser = locationResult.getLastLocation();
                    setUserLocationMarker(locationResult.getLastLocation());
                }
            }
        };

        startLocationUpdates();

        explore.setVisibility(View.GONE);
        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                explore.setVisibility(View.VISIBLE);
                explore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        explore();
                    }
                });
            }
        });

    }

    private void setUserLocationMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        userLoc = latLng;
        if (userLocationMarker == null) {
            //Create a new marker
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.navigation);
            Bitmap smallMarker = Bitmap.createScaledBitmap(icon, 125, 125, false);
            BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            markerOptions.snippet("Current Location");
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            userLocationMarker = map.addMarker(markerOptions.title("You are here").icon(smallMarkerIcon));
        } else {

            //use the previously created marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());

        }

        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 200, 0, 200));
            circleOptions.fillColor(Color.argb(32, 200, 0, 200));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = map.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check Condition
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //When permission is Granted
            //Call Method
            sendSOS();
        } else {
            //When permission is Denied
            //Display Toast
            Toast.makeText(this, "Permission Denied, Please Grant!", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            //When permission is Denied
            //Display Toast
            Toast.makeText(this, "Permission Denied, Please Grant!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSOS() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
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
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        address = addresses.get(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (!msgRc.isEmpty() && !msgI.isEmpty()) {

                        //Initialize SMS Manager
                        SmsManager smsManager = SmsManager.getDefault();
                        //Send Msg
                        smsManager.sendTextMessage(msgRc, null, msgI + "\n" + "Latitude: " + address.getLatitude() + "\nLongitude: " + address.getLongitude() + "\nAddress: " + address.getAddressLine(0), null, null);
                    } else {

                        Toast.makeText(Home.this, "Please enter a message.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }


    // Method to get Current Location
    private void explore() {

        if (centerCtr == 1){
            map.clear();
            userLocationMarker = null;
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
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
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        address = addresses.get(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    mapFragment.getMapAsync(Home.this);


                    //assert mapFragment != null;
                    setUserLocationMarker(location);
                }
            }
        });
    }

    public void loadSOS() {
        FileInputStream fisReceiver = null;

        try {
            fisReceiver = openFileInput(SOS_RECEIVER);
            InputStreamReader isrReceiver = new InputStreamReader(fisReceiver);
            BufferedReader brReceiver = new BufferedReader(isrReceiver);
            StringBuilder sbReceiver = new StringBuilder();
            String msgR;

            while ((msgR = brReceiver.readLine()) != null) {
                sbReceiver.append(msgR).append("\n");
            }
            msgRc = sbReceiver.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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

            while ((msgM = brReceiver.readLine()) != null) {
                sbMessage.append(msgM).append("\n");
            }
            msgI = sbMessage.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fisMessage != null) {
                try {
                    fisMessage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void fitAllMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

//the include method will calculate the min and max bound.

        if (markerOrigin != null && markerDestination == null) {
            builder.include(markerOrigin.getPosition());
            builder.include(userLocationMarker.getPosition());
        } else if (markerOrigin == null && markerDestination != null) {
            builder.include(markerDestination.getPosition());
            builder.include(userLocationMarker.getPosition());
        } else if (markerOrigin == null && markerDestination == null) {
            builder.include(userLocationMarker.getPosition());
        } else if (userLocationMarker == null && markerOrigin != null) {
            builder.include(markerOrigin.getPosition());
        } else if (userLocationMarker == null && markerDestination != null) {
            builder.include(markerDestination.getPosition());
        } else {
            builder.include(markerOrigin.getPosition());
            builder.include(markerDestination.getPosition());
            builder.include(userLocationMarker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        map.animateCamera(cu);
    }


    public void calcDistance() {

        float[] results = new float[1];

        if(swap == 0){
            Location.distanceBetween(locationUser.getLatitude(), locationUser.getLongitude(), pu_latD, pu_lngD, results);
        }else if(swap == 1){
            Location.distanceBetween(locationUser.getLatitude(), locationUser.getLongitude(), do_latD, do_lngD, results);
        }

        speed = locationUser.getSpeed();

        kmh = (int) (speed * 3.6);

        distance = results[0];

        meters = (int) (distance / 1000);
        Toast.makeText(this, Integer.toString(kmh) ,Toast.LENGTH_SHORT).show();
    }

    // Power Save Map Tracking.
    private void onStartMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
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
                    setUserLocationMarker(location);
                    myPlace();
                    animateMyPlaceIn();
                    animateMyPlaceOut();
                }
            }
        });

    }

    public void startLoop() {
        mapRunnable.run();

    }

    public void stopLoop() {

    }

    private Runnable mapRunnable = new Runnable() {
        @Override
        public void run() {
            onStartMap();
            mapHandler.postDelayed(this, 1000);
        }
    };

    public void userLoc() {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.person);
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon, 125, 125, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        if (currentLocationMarker == null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            currentLocationMarker = map.addMarker(new MarkerOptions().position(latLng).title("").icon(smallMarkerIcon));
        } else {
            currentLocationMarker.setPosition(latLng);
        }
    }

    public void desCalc() throws JSONException {

        JSONObject locationJsonObject = new JSONObject();
        locationJsonObject.put("origin", originLoc);
        locationJsonObject.put("destination", destinationLoc);
        LatlngCalc(locationJsonObject);
    }

    private void LatlngCalc(JSONObject locationJsonObject) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(Home.this);
        String url = "https://maps.googleapis.com/maps/api/distancematrix/" +
                "json?origins=" + locationJsonObject.getString("origin") + "&destinations=" + locationJsonObject.getString("destination") + "&mode=driving&" +
                "language=en-EN&sensor=false" + "&key=" + "AIzaSyA9y3TlxDiPcFrRXML4EbjGeJqr7h6f308";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        currLoc.setText("Response is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                currLoc.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }

    private void addCircle(LatLng latLng, float radius) {

        if (geofenceBounds == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(radius);
            circleOptions.strokeColor(Color.argb(255, 255, 0,0));
            circleOptions.fillColor(Color.argb(64, 255, 0,0));
            circleOptions.strokeWidth(4);
            geofenceBounds = map.addCircle(circleOptions);
        } else {
            geofenceBounds.setCenter(latLng);
        }

    }
    private void animateMyPlaceIn(){
        final View v = constraintLayout2;
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TranslateAnimation animate = new TranslateAnimation(0,0,-500,0);
                animate.setDuration(500);
                animate.setFillAfter(true);
                v.startAnimation(animate);
                v.setVisibility(View.VISIBLE);
            }
        },250);
    }
    private void animateMyPlaceOut(){

        final View v = constraintLayout2;
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TranslateAnimation animate = new TranslateAnimation(0,0,0,-500);
                animate.setDuration(500);
                animate.setFillAfter(true);
                v.startAnimation(animate);
                v.setVisibility(View.GONE);
            }
        },5000);

    }

    private void animateTripIn(){
        final View v = constraintLayout;
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TranslateAnimation animate = new TranslateAnimation(0,0,-500,0);
                animate.setDuration(500);
                animate.setFillAfter(true);
                v.startAnimation(animate);
                v.setVisibility(View.VISIBLE);
            }
        },250);
    }
    private void animateTripOut(){

        final View v = constraintLayout;
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TranslateAnimation animate = new TranslateAnimation(0,0,0,-500);
                animate.setDuration(500);
                animate.setFillAfter(true);
                v.startAnimation(animate);
                v.setVisibility(View.GONE);
            }
        },5000);

    }
    private void animateNearbyIn(){

        final View v = constraintLayout3;
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TranslateAnimation animate = new TranslateAnimation(-500,-20,0,0);
                animate.setDuration(500);
                animate.setFillAfter(true);
                v.startAnimation(animate);
                v.setVisibility(View.VISIBLE);
            }
        },0);
        nearby = 1;
    }
    private void animateNearbyOut(){

        final View v = constraintLayout3;
        animationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TranslateAnimation animate = new TranslateAnimation(-20,-500,0,0);
                animate.setDuration(500);
                animate.setFillAfter(true);
                v.startAnimation(animate);
                v.setVisibility(View.GONE);
            }
        },0);

    }
    @Override
    public void onBackPressed() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        if( nearby == 1){
            animateNearbyOut();
            nearby = 0;
        }else{
            doExit();
        }
    }
    private void doExit() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Home.this);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.setNegativeButton("No", null);

        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setTitle("Biyahe");
        alertDialog.show();
    }
    public void myPlace(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            Address address;
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    address = null;
                    Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
                    mapFragment.getMapAsync(Home.this);
                    //assert mapFragment != null;

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                        address = addresses.get(0);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                if (address != null){
                    currLoc.setText(address.getAddressLine(0));
                    //assert mapFragment != null;
                    setUserLocationMarker(location);
                }else{
                    navHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currLoc.setText(address.getAddressLine(0));
                            //assert mapFragment != null;
                            setUserLocationMarker(location);
                        }
                    },2000);
                }
            }
        });

    }

    private String getUrl(double latitude,double longitude, String nearbyPlace){

        latitude = currentLocation.getLatitude();
        longitude = currentLocation.getLongitude();

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyA9y3TlxDiPcFrRXML4EbjGeJqr7h6f308");

        nearbyLatitude = latitude;
        nearbyLongitude = longitude;

        return googlePlaceUrl.toString();

    }

    public void setHospitals(){

        map.clear();

        nearbyPlace = "hospital";
        String url = getUrl(nearbyLatitude, nearbyLongitude, nearbyPlace);
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = map;
        dataTransfer[1] = url;
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(Home.this, "Showing nearby Hospitals", Toast.LENGTH_LONG).show();
        userLocationMarker = null;
    }
    public void setSchools(){

        map.clear();

        nearbyPlace = "school";
        String url = getUrl(nearbyLatitude, nearbyLongitude, nearbyPlace);
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = map;
        dataTransfer[1] = url;
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(Home.this, "Showing nearby Schools", Toast.LENGTH_LONG).show();
        userLocationMarker = null;
    } public void setRestaurants(){

        map.clear();

        nearbyPlace = "restaurant";
        String url = getUrl(nearbyLatitude, nearbyLongitude, nearbyPlace);
        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = map;
        dataTransfer[1] = url;
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(Home.this, "Showing nearby Restaurants", Toast.LENGTH_LONG).show();
        userLocationMarker = null;

    }public void Duration(){


        Object dataTransfer[] = new Object[3];
        String url = getDirectionsUrl();
        //GetDirectionsData getDirectionsData = new GetDirectionsData();
        dataTransfer[0] = map;
        dataTransfer[1] = url;
        dataTransfer[2] = new LatLng(destinationLat, destinationLong);
        //getDirectionsData.execute(dataTransfer);

    }

    private String getDirectionsUrl(){

        originLat = originLoc.latitude;
        originLong = originLoc.longitude;

        destinationLat = destinationLoc.latitude;
        destinationLong = destinationLoc.longitude;

        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("&origin="+originLat+","+originLong);
        googleDirectionsUrl.append("&destination="+destinationLat+","+destinationLong);
        googleDirectionsUrl.append("&key="+"AIzaSyBzKLXS2uFOSVE1Lhr3AOkDn1OkbKfo01M");

        return googleDirectionsUrl.toString();
    }



    public void startTrip() {
        if (originLoc != null && destinationLoc != null) {
            startTrip.setVisibility(View.VISIBLE);
            Duration();
        } else if (originLoc == null && destinationLoc != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    Home.this);

            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mapHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Duration();

                        }
                    }, 500);

                    startTrip.setVisibility(View.VISIBLE);
                }
            });

            alertDialog.setNegativeButton("No", null);

            alertDialog.setMessage("You have not set your origin yet. Do you want to set your current location as the origin of your trip?");
            alertDialog.setTitle("Biyahe");
            alertDialog.setIcon(R.drawable.jeepney);
            alertDialog.show();
        }
        }

        public class GetDirectionsData extends AsyncTask<Object, String, String> {


            GoogleMap map;
            String url;
            String googleDirectionsData;
            String duration, distance;
            LatLng latLng;

            @Override
            protected String doInBackground(Object... objects) {
                map = (GoogleMap) objects[0];
                url = (String) objects[1];
                latLng = (LatLng) objects[2];

                DowloadURL dowloadURL = new DowloadURL();
                try {
                    googleDirectionsData = dowloadURL.readURL(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return googleDirectionsData;
            }

            @Override
            protected void onPostExecute(String s) {

                HashMap<String, String> directionsList = null;
                DataParser parser = new DataParser();
                directionsList = parser.parseDirections(s);
                duration = directionsList.get("duration");
                distance = directionsList.get("distance");

                distanceDes = distance;
                durationDes = duration;


                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.racing_flag);
                Bitmap smallMarker = Bitmap.createScaledBitmap(icon, 125, 125, false);
                BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                if (markerDestination == null) {

                    markerDestination = map.addMarker(new MarkerOptions().position(destinationLoc).title("Duration = " + duration).icon(smallMarkerIcon).snippet("Distance = " + distance));
                    fitAllMarkers();
                } else {
                    markerDestination.setPosition(destinationLoc);
                    fitAllMarkers();

                }
            }
        }
public void tripFinished(){

    DistHandler.removeCallbacks(distRunnable);

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
            Home.this);

    alertDialog.setMessage("Have you already reached your destination?");
    alertDialog.setTitle("Biyahe");
    alertDialog.setIcon(R.drawable.jeepney);

    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            bottomNav.getMenu().findItem(R.id.nearby).setEnabled(true);
            bottomNav.getMenu().findItem(R.id.nearby).setCheckable(true);


            animateTripOut();
            slidingUpPanelLayout.setTouchEnabled(true);
            startTrip.setVisibility(View.GONE);
            endTrip.setVisibility(View.GONE);


            slide_origin.setText(null);
            slide_dest.setText(null);
            slide_fare.setText(null);
            confirm.setVisibility(View.GONE);

            Pickup.remove();

            Pickup = null;

            if (dropOff != null){

                DropOff.remove();

                DropOff = null;
            }

            markerOrigin.remove();

            markerOrigin = null;

            markerDestination.remove();

            markerDestination = null;

            geofenceBounds.remove();

            geofenceBounds = null;

            PuDo = 0;

            DistHandler.removeCallbacks(distRunnable);
            DistHandler.removeCallbacks(DOdistRunnable);
            navHandler.removeCallbacks(navRunnable);


            geofencingClient.removeGeofences(geofencingHelper.getPendingIntent()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "onSuccess: Existing Geofence Removed");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: Error" );
                }
            });

        }
    });
    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            NavDistRepeater();
        }
    });

    alertDialog.setMessage("Have you already reached your destination?");
    alertDialog.setTitle("Biyahe");
    alertDialog.setIcon(R.drawable.jeepney);
    alertDialog.show();

}
public void endTrip(){
        centerCtr = 1;
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
            Home.this);

    alertDialog.setMessage("Do you want to end your trip now?");
    alertDialog.setTitle("Biyahe");
    alertDialog.setIcon(R.drawable.jeepney);

    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            bottomNav.getMenu().findItem(R.id.nearby).setEnabled(true);
            bottomNav.getMenu().findItem(R.id.nearby).setCheckable(true);

            slide_origin.setText(null);
            slide_dest.setText(null);
            slide_fare.setText(null);
            confirm.setVisibility(View.GONE);

            animateTripOut();
            slidingUpPanelLayout.setTouchEnabled(true);
            startTrip.setVisibility(View.GONE);
            endTrip.setVisibility(View.GONE);


            searchTrip.clearListSelection();
            searchTrip.getText().clear();

            Pickup.remove();

            Pickup = null;

            markerOrigin.remove();

            markerOrigin = null;

            markerDestination.remove();

            markerDestination = null;

            geofenceBounds.remove();

            geofenceBounds = null;

            PuDo = 0;

            DistHandler.removeCallbacks(distRunnable);
            DistHandler.removeCallbacks(DOdistRunnable);
            navHandler.removeCallbacks(navRunnable);


            geofencingClient.removeGeofences(geofencingHelper.getPendingIntent()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "onSuccess: Existing Geofence Removed");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: Error" );
                }
            });


            if (DropOff != null){

                DropOff.remove();

                DropOff = null;
            }


        }
    });
    alertDialog.setNegativeButton("No", null);

    alertDialog.setMessage("Do you want to end your trip now?");
    alertDialog.setTitle("Biyahe");
    alertDialog.setIcon(R.drawable.jeepney);
    alertDialog.show();

}

public void startStartTrip(){

    animateTripIn();
    startTrip.setVisibility(View.GONE);
    endTrip.setVisibility(View.VISIBLE);
    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    bottomNav.getMenu().findItem(R.id.nearby).setEnabled(false);
    bottomNav.getMenu().findItem(R.id.nearby).setCheckable(false);

    biyahe = "biyahe";

    if (Build.VERSION.SDK_INT >= 29){

        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED){
            centerCtr = 0;
            showJourney();
            slidingUpPanelLayout.setTouchEnabled(false);

            //addCircle(latLng, GEOFENCE_RADIUS);
            //addGeofence(latLng, GEOFENCE_RADIUS);
        }else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {

                ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);

            }else {
                ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
            }
        }

    }else {
        centerCtr = 0;
        slidingUpPanelLayout.setTouchEnabled(false);
        showJourney();
        //addCircle(latLng, GEOFENCE_RADIUS);
        //addGeofence(latLng, GEOFENCE_RADIUS);
    }
}
    public void TripDataLoader(){

        reference = FirebaseDatabase.getInstance().getReference().child("location_information"); //Locate the Database in the Firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                NullCheck = snapshot.child(PlaceKey).child("way").child(biyahe).getValue().toString();

                if(NullCheck != "false") {
                    //Get the Value and set it to String
                    pickUpSt = snapshot.child(PlaceKey).child("way").child(biyahe).child("pickup").getValue().toString();
                    pickupRouteSt = snapshot.child(PlaceKey).child("way").child(biyahe).child("pu_route").getValue().toString();
                    dropOffSt = snapshot.child(PlaceKey).child("way").child(biyahe).child("dropoff").getValue().toString();
                    fareSt = snapshot.child(PlaceKey).child("way").child(biyahe).child("fare").getValue().toString();
                    pu_lat = snapshot.child(PlaceKey).child("way").child(biyahe).child("pickupLat").getValue().toString();
                    pu_lng = snapshot.child(PlaceKey).child("way").child(biyahe).child("pickupLng").getValue().toString();
                    do_lat = snapshot.child(PlaceKey).child("way").child(biyahe).child("dropoffLat").getValue().toString();
                    do_lng = snapshot.child(PlaceKey).child("way").child(biyahe).child("dropoffLng").getValue().toString();

                    o_lat = snapshot.child(PlaceKey).child("originLat").getValue().toString();
                    o_lng = snapshot.child(PlaceKey).child("originLng").getValue().toString();
                    trip_org = snapshot.child(PlaceKey).child("origin").getValue().toString();
                    d_lat = snapshot.child(PlaceKey).child("destinationLat").getValue().toString();
                    d_lng = snapshot.child(PlaceKey).child("destinationLng").getValue().toString();
                    trip_dest = snapshot.child(PlaceKey).child("destination").getValue().toString();



                    if(snapshot.child(PlaceKey).child("way").child("biyahe").getValue().toString() != "false"){
                        fare_total = Integer.parseInt(snapshot.child(PlaceKey).child("way").child("biyahe").child("fare").getValue().toString());
                        if(snapshot.child(PlaceKey).child("way").child("biyahe2").getValue().toString() != "false"){
                            fare_total = Integer.parseInt(snapshot.child(PlaceKey).child("way").child("biyahe").child("fare").getValue().toString()) + Integer.parseInt(snapshot.child(PlaceKey).child("way").child("biyahe2").child("fare").getValue().toString());
                            if(snapshot.child(PlaceKey).child("way").child("biyahe3").getValue().toString() != "false"){
                                fare_total = Integer.parseInt(snapshot.child(PlaceKey).child("way").child("biyahe").child("fare").getValue().toString()) + Integer.parseInt(snapshot.child(PlaceKey).child("way").child("biyahe2").child("fare").getValue().toString()) + Integer.parseInt(snapshot.child(PlaceKey).child("way").child("biyahe3").child("fare").getValue().toString());
                            }
                        }
                    }

                    total_fare = Integer.toString(fare_total);

                    NRTcheck = snapshot.child(PlaceKey).child("way").child(NextRT).getValue().toString();
                    if(NRTcheck != "false"){
                        nextRouteSt = snapshot.child(PlaceKey).child("way").child(NextRT).child("pu_route").getValue().toString();
                    }else if(NRTcheck == "false"){
                        nextRouteSt = trip_dest;
                    }

                }


                originLoc = new LatLng(Double.parseDouble(o_lat),Double.parseDouble(o_lng));
                destinationLoc = new LatLng(Double.parseDouble(d_lat),Double.parseDouble(d_lng));

                pu_latD = Double.parseDouble(pu_lat);
                pu_lngD = Double.parseDouble(pu_lng);
                puLatLng = new LatLng (pu_latD, pu_lngD);
                do_latD = Double.parseDouble(do_lat);
                do_lngD = Double.parseDouble(do_lng);
                doLatLng = new LatLng(do_latD,do_lngD);

                if (nav == 0){
                    Nav();
                    nav = 1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void Nav() {
        slide_origin.setText("Origin: " + trip_org);
        slide_dest.setText("Destination: " + trip_dest);
        slide_fare.setText("Fare Estimate: ₱" + total_fare);
    }
    public void Confirm(){
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        showOrigin();
        showDestination();
        startTrip();
    }

    public void showJourney() {
        setPuDoMarker();
        PuDo = 0;
        swap = 0;
        ctr = 0;
        //Set the String Text to the Text View Variable
        pickUp.setText(pickUpSt);
        //pickupRoute.setText(pickupRouteSt);
        dropOff.setText(dropOffSt);
        fare.setText(fareSt);
        nextRoute.setText(pickupRouteSt);
        NavDurationRepeater();
        puMarkerGeofence();
    }

    public void setPuDoMarker(){
        NavDistRepeater();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(puLatLng);
        markerOptions.title(pickupRouteSt);
        markerOptions.snippet("Pickup Point");
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.bus_stop);
        Bitmap marker = Bitmap.createScaledBitmap(icon,125, 245, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(marker);

        if (Pickup == null) {

            Pickup = map.addMarker(markerOptions.icon(smallMarkerIcon));

        } else {
            Pickup.setPosition(puLatLng);
            Pickup.setTitle(pickupRouteSt);
            Pickup.setSnippet("Pickup Point");
        }

    }
    public void setDoMarker(){

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(doLatLng);
        markerOptions.title(dropOffSt);
        markerOptions.snippet("Dropoff Point");
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.flagged);
        Bitmap marker = Bitmap.createScaledBitmap(icon,150, 150, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(marker);

        if (DropOff == null) {

            DropOff = map.addMarker(markerOptions.icon(smallMarkerIcon));

        } else {
            DropOff.setPosition(doLatLng);
            DropOff.setTitle(dropOffSt);
            DropOff.setSnippet("Dropoff Point");
        }
        fitAllMarkers();
    }

    public void puMarkerGeofence(){


        if (Pickup != null){
            addCircle(puLatLng, GEOFENCE_RADIUS);
            addGeofence(puLatLng, GEOFENCE_RADIUS);

        }else {
            mapHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    addCircle(puLatLng, GEOFENCE_RADIUS);
                    addGeofence(puLatLng, GEOFENCE_RADIUS);

                }
            },2000);
        }
    }

    public void doMarkerGeofence(){

            mapHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    addCircle(doLatLng, GEOFENCE_RADIUS);
                    addGeofence(doLatLng, GEOFENCE_RADIUS);
                }
            },20000);

    }


    public void NavDurationRepeater (){
       navRunnable.run();

    }


    private Runnable navRunnable = new Runnable() {

        @Override
        public void run() {

            calcDistance();

            //NavDuration();

            navHandler.postDelayed(this, 500);
        }
    };



    public void NavDistRepeater (){
        distRunnable.run();

    }


    private Runnable distRunnable = new Runnable() {

        @Override
        public void run() {

            DistHandler.removeCallbacks(DOdistRunnable);

            distancePU.setText(" " + Math.round(distance) + " m" + "\n Pickup");

            if(distance <= 200 && distance != 0 && PuDo == 0){
                setDoMarker();
                doMarkerGeofence();
                nextRoute.setText(nextRouteSt);
                PuDo = 1;
            }else if (distance>= 400 && distance<= 500 && PuDo == 1){
                swap = 1;
                DONavDistRepeater();
                DistHandler.removeCallbacks(distRunnable);
            }else if (kmh >= 25 && distance>= 200 && distance<= 700){
                setDoMarker();
                doMarkerGeofence();
                PuDo = 1;
                swap = 1;
                nextRoute.setText(nextRouteSt);
                DONavDistRepeater();
                DistHandler.removeCallbacks(distRunnable);
            }else if(NullCheck == "false" && ctr == 0){
                ctr = 1;
                DistHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tripFinished();
                        DistHandler.removeCallbacks(distRunnable);
                    }
                },1000);
            }
            DistHandler.postDelayed(this, 500);
        }
    };

    public void DONavDistRepeater (){
        DOdistRunnable.run();
    }
    private Runnable DOdistRunnable = new Runnable() {
        @Override
        public void run() {

            DistHandler.removeCallbacks(distRunnable);

            //DONavDuration();
            distancePU.setText(" " + Math.round(distance) + " m" + "\n Dropoff");

            if(distance <= 200 && distance != 0){
                if(biyahe == "biyahe"){
                    biyahe = "biyahe2";
                    NextRT = "biyahe3";
                    TripDataLoader();
                    setPuDoMarker();
                    swap = 0;
                    PuDo = 0;
                    DistHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showJourney();
                        }
                    },1000);
                }else if(biyahe == "biyahe2"){
                    biyahe = "biyahe3";
                    NextRT = "biyahe4";
                    TripDataLoader();
                    setPuDoMarker();
                    swap = 0;
                    PuDo = 0;
                    DistHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showJourney();
                        }
                    },1000);
                }
            }
            DistHandler.postDelayed(this, 500);
        }
    };
}







