package com.example.biyahe;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap map;
    String url;
    @Override
    protected String doInBackground(Object... objects) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
