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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by sasha on 07.10.2015.
 */
public class MapsViewActivity extends FragmentActivity {
    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    public static final String CITY = "CITY";
    public static final String ADDRESS= "ADDRESS";
    public static final String REGION= "REGION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        googleMap = supportMapFragment.getMap();
        googleMap.getUiSettings().setZoomControlsEnabled(true);


       Bundle bundle = getIntent().getExtras();
        if (bundle!=null && bundle.containsKey(CITY)){

            String city = bundle.getString(CITY);
            String region = bundle.getString(REGION);
            String address = bundle.getString(ADDRESS);

            String location = " "+address;
            if (!city.equals(region)) {
                location += ", " + city ;
            }
            location += ", " + region;

            Log.d("qqq" ,  location);

            if(location!=null && !location.equals("")){
                new GeocoderTask().execute(location);
            }
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

            if(addresses==null || addresses.size()==0)
                Toast.makeText(MapsViewActivity.this, "No Location found", Toast.LENGTH_SHORT).show();

            googleMap.clear();
            for(int i=0;i<addresses.size();i++){

                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                googleMap.addMarker(markerOptions);
                Log.d("qqq", address.toString());
                // Locate the first location
//                if(i==0)
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 15.0f));
            }
        }
    }

}
