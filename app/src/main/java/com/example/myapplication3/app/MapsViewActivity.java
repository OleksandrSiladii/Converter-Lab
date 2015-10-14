package com.example.myapplication3.app;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by sasha on 07.10.2015.
 */
public class MapsViewActivity extends FragmentActivity {
    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;
    private LatLng latLng;

    private boolean ifTryAgain;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        try {
            SupportMapFragment supportMapFragment = (SupportMapFragment)
                    getSupportFragmentManager().findFragmentById(R.id.map);

            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    mGoogleMap = googleMap;
                }
            });
            ifTryAgain = false;

            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.containsKey(Constants.TAG_CITY)) {

                String city = bundle.getString(Constants.TAG_CITY);
                String region = bundle.getString(Constants.TAG_REGION);
                String address = bundle.getString(Constants.TAG_ADDRESS);

                location = " " + region;
                if (!city.equals(region)) {
                    location += " " + city;
                }
                location += " " + address;

                Log.d(Constants.TAG_LOG, location);

                if (!location.isEmpty()) {
                    new GeocoderTask().execute(location);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {

            Geocoder geocoder = new Geocoder(MapsViewActivity.this);
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocationName(locationName[0], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            mGoogleMap.clear();
            if (addresses == null || addresses.size() == 0) {
                if (!ifTryAgain) {
                    location = location.substring(0, location.indexOf(','));
                    new GeocoderTask().execute(location);
                    ifTryAgain = true;
                    Log.d(Constants.TAG_LOG, location);

                } else {
                    Toast.makeText(MapsViewActivity.this, R.string.no_location_found, Toast.LENGTH_SHORT).show();
                }
            } else {

                for (int i = 0; i < addresses.size(); i++) {

                    Address address = (Address) addresses.get(i);

                    latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    String addressText = String.format("%s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getCountryName());

                    markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(addressText);

                    mGoogleMap.addMarker(markerOptions);
                    Log.d(Constants.TAG_LOG, address.toString());

                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 15.0f));
                }
            }
        }
    }
}
