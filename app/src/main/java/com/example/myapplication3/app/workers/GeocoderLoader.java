package com.example.myapplication3.app.workers;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by sasha on 14.10.2015.
 */
public class GeocoderLoader extends AsyncTaskLoader<List<Address>> {
    private String locationName;

    public GeocoderLoader(Context context, Bundle args) {
        super(context);
        if (args != null)
            locationName = args.getString(Constants.TAG_LOCATION_NAME);
    }

    @Override
    public List<Address> loadInBackground() {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses = null;
        Log.d(Constants.TAG_LOG, "do LOADER G ;" + locationName);

        try {
            addresses = geocoder.getFromLocationName(locationName, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }
}
