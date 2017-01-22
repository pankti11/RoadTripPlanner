package com.roadsurfers.roadsurfers.utility;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceTypes;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by raula on 1/21/2017.
 */

public class MyLocationManager  {


    private GoogleApiClient googleApiClient;

    public MyLocationManager(Context appContext) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(appContext)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) appContext)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) appContext)
                    .addApi(LocationServices.API)
                    .build();
        }

//        GooglePlaces client = new GooglePlaces("yourApiKey");
    }



}
