package com.example.biyahe;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap map;
    String url;
    @Override
    protected String doInBackground(Object... objects) {

        map = (GoogleMap) objects[0];
        url = (String) objects[1];

        DowloadURL dowloadURL= new DowloadURL();
        try {
            googlePlacesData = dowloadURL.readURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;

    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser parser = new DataParser();
        nearbyPlacesList = parser.parse(s);
        showNearbyPlaces(nearbyPlacesList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList){

        for (int i =0; i<nearbyPlaceList.size(); i++){

            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.snippet(vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));

        }
    }

}
