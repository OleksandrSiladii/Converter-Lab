package com.example.myapplication3.app.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import com.example.myapplication3.app.Constants;

import java.io.IOException;
import java.util.List;

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

        try {
            addresses = geocoder.getFromLocationName(locationName, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }
}
