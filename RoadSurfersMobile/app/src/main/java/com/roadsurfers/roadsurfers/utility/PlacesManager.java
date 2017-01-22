package com.roadsurfers.roadsurfers.utility;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.roadsurfers.roadsurfers.R;
import com.roadsurfers.roadsurfers.SuggestionActivity;
import com.roadsurfers.roadsurfers.adapters.PlaceAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by raula on 1/21/2017.
 */

public class PlacesManager extends AsyncTask<URL, Integer, List<Place>> {

    static final int EARTH_RADIUS= 3959;
    static final int SEARCH_RAD=5;
    private static  boolean ACTIVE = false;
    static int NUM_PLACES = 5;
    private SuggestionActivity parentActivity;
    private ListView listview;
    private ArrayList<Place> places= new ArrayList<>();

    ObjectMapper mp = new ObjectMapper();

    public PlacesManager() {
        NUM_PLACES=5;
    }

    public PlacesManager(ListView listview, SuggestionActivity suggestionActivity) {
        NUM_PLACES=5;
        this.listview=listview;
        this.parentActivity=suggestionActivity;
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

    public LatLng[] getBoundary(int distance, LatLng startPoint ){
//        int numPoints = (int) (4+ 2*Math.PI*distance/SEARCH_RAD);
        int numPoints =10;
        LatLng[] latLngs = new LatLng[numPoints];
        for (int i=0; i <numPoints; i++){
            //verify if the result is float
            latLngs[i]= getCoordinate(startPoint, (float) ((double)i*360.0/numPoints),distance);
        }
        return  latLngs;
    }

    public LatLng getCoordinate(LatLng startPoint,float angle, int distance){
//        double lat = Math.asin( Math.sin(Math.toRadians(startPoint.latitude))*Math.cos(distance/EARTH_RADIUS) +
//                Math.cos(startPoint.latitude)*Math.sin(Math.toRadians(distance/EARTH_RADIUS))*Math.cos(Math.toRadians(angle)) );
//
//        double lon = startPoint.longitude + Math.atan2(Math.sin(angle)*Math.sin(Math.toRadians(distance/EARTH_RADIUS))*Math.cos(Math.toRadians(startPoint.latitude)),
//                Math.cos(Math.toRadians(distance/EARTH_RADIUS))-Math.sin(Math.toRadians(startPoint.latitude))*Math.sin(lat));
//        lat+=startPoint.latitude;

        double lat1 = Math.toRadians(startPoint.latitude);
        double lon1 = Math.toRadians(startPoint.longitude);
        double brng = Math.toRadians(angle);
        double dist= (double)distance/EARTH_RADIUS;

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist) +
                Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));

        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(dist) * Math.cos(lat1),
                                         Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));
        return new LatLng(Math.toDegrees(lat2),Math.toDegrees(lon2));
    }

    @Override
    protected List<Place> doInBackground(URL... params) {
        if (NUM_PLACES>0) NUM_PLACES--;
        for (URL url: params ) {
            try {
                if (this.places.size()>100) return this.places;
                this.places.addAll(getUrlContents(url).getPlaces());

            } catch (IOException e) {
                e.printStackTrace();
                return places;
            }
        }
        return places;
    }



    public void getSuggestions(LatLng[] latLngs){
        URL[] urls = new URL[latLngs.length];
        for (int i=0; i<latLngs.length; i++) {
            try {
                urls[i] = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?type=natural_feature&rankby=prominence&location=" + latLngs[i].latitude + "," + latLngs[i].longitude + "&radius=5000&key=AIzaSyAGOVuNGKOJuBhJmf_wnsjjXYK68CrFqkY");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
            this.execute(urls);
    }

    @Override
    public void onPostExecute (List<Place> places){
        // render the results one after the other

        Place[] placesArr =places.toArray(new Place[places.size()]);
        Arrays.sort(placesArr, new Comparator<Place>() {
            @Override
            public int compare(Place o1, Place o2) {
                if (o1.getRating()>o2.getRating()) return 1;
                else if (o1.getRating()==o2.getRating()) return 0;
                else return -1;
            }
        });
        if (NUM_PLACES>0){

//            Place[] ps= new Place[10];
//            for (int i=0; i<10; i++){
//                ps[i] = new Place();
//                ps[i].setName("place"+i);
//            }


//            String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//                    "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//                    "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
//                    "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
//                    "Android", "iPhone", "WindowsMobile" };
//            final ArrayList<String> list = new ArrayList<String>();
//            for (int i = 0; i < values.length; ++i) {
//                list.add(values[i]);
//            }

            final PlaceAdapter adapter = new PlaceAdapter(parentActivity, places.toArray(new Place[places.size()]));
            listview.setAdapter(adapter);
        }
        ACTIVE=false;
    }

    @Override
    public void onPreExecute (){
        // render the results one after the other
        ACTIVE=true;

    }
}
