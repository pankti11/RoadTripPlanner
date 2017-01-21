package com.roadsurfers.roadsurfers.utility;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class MyLocationManager extends AsyncTask<URL, Integer, Long> {
    static final int EARTH_RADIUS= 3959;
    static final int SEARCH_RAD=5;
    ObjectMapper mp ;

    private GoogleApiClient googleApiClient;
    private ArrayList<Place> places;

    public MyLocationManager(Context appContext) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(appContext)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) appContext)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) appContext)
                    .addApi(LocationServices.API)
                    .build();
        }
        mp = new ObjectMapper();

//        GooglePlaces client = new GooglePlaces("yourApiKey");
    }

    public Places getUrlContents(URL url) throws IOException {
        StringBuilder content = new StringBuilder();
        try {
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return mp.readValue(content.toString(), Places.class);
    }

    public LatLng[] getBoundary(int distance,LatLng startPoint ){
        int numPoints = (int) (4+ 2*Math.PI*distance/SEARCH_RAD);
        LatLng[] latLngs = new LatLng[numPoints];
        for (int i=0; i <numPoints; i++){
            //verify if the result is float
            latLngs[i]= getCoordinate(startPoint, (float) ((double)i*360.0/numPoints),distance);
        }
        return  latLngs;
    }

    public LatLng getCoordinate(LatLng startPoint,float angle, int distance){
        double lat = Math.asin( Math.sin(startPoint.latitude)*Math.cos(distance/EARTH_RADIUS) +
                Math.cos(startPoint.latitude)*Math.sin(distance/EARTH_RADIUS)*Math.cos(angle) );
        double lon = startPoint.longitude + Math.atan2(Math.sin(angle)*Math.sin(distance/EARTH_RADIUS)*Math.cos(startPoint.latitude),
                Math.cos(distance/EARTH_RADIUS)-Math.sin(startPoint.latitude)*Math.sin(lat));
        return new LatLng(lat,lon);
    }

    @Override
    protected Long doInBackground(URL... params) {
        places= new ArrayList<>();
        for (URL url: params ) {
            try {
                places.addAll(getUrlContents(url).getPlaces());
                publishProgress(getUrlContents(url).getPlaces());
            } catch (IOException e) {
                e.printStackTrace();
                return 0L;
            }
        }
        return 1L;
    }

    private void publishProgress(List<Place> places) {
    }

    public Place[] getSuggestions(LatLng[] latLngs){
        URL[] urls = new URL[latLngs.length];
        for (int i=0; i<latLngs.length; i++) {
            try {
                urls[i] = new URL("https://maps.googleapis.com/maps/api/place/search/json?types=cafe&location=" + latLngs[i].latitude + "," + latLngs[i].longitude + "&radius=5000&sensor=false&key=AIzaSyDnV9lO5NqMAKU3K68lgtgXK-UyvAR2gKI");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        this.execute(urls);

    }

}
