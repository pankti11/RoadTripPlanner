package com.roadsurfers.roadsurfers.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roadsurfers.roadsurfers.R;
import com.roadsurfers.roadsurfers.utility.Place;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by raula on 1/21/2017.
 */

public class PlaceAdapter extends ArrayAdapter<Place> {
    private final Context context;
    private final Place[] places;

    public PlaceAdapter(Context context, Place[] places) {
        super(context, R.layout.suggest_row, places);
        this.context = context;
        this.places = places;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.suggest_row, parent, false);
        TextView fline= (TextView) rowView.findViewById(R.id.firstLine);
        TextView sLine = (TextView) rowView.findViewById(R.id.secondLine);
//        final ImageView img = (ImageView) rowView.findViewById(R.id.icon);
//        places[position].setIcon();
        fline.setText(places[position].getName());
        sLine.setText("Near "+ places[position].getVicinity());
/*        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ImageView img = (ImageView) rowView.findViewById(R.id.icon);
                if (places[position].getPhotos()!=null)
                img.setImageBitmap(getBitmapFromURL("https://maps.googleapis.com/maps/api/place/photo?photoreference="+ places[position].getPhotos().get(0).getPhotoReference()+"&sensor=false&maxheight=100&maxwidth=200&key=AIzaSyAGOVuNGKOJuBhJmf_wnsjjXYK68CrFqkY"));
            }
        });*/
        return rowView;
    }



    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
