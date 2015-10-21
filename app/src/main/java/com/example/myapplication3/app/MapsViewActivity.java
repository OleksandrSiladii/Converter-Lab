package com.example.myapplication3.app;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication3.app.loaders.GeocoderLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsViewActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Address>> {
    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;
    private LatLng latLng;
    Context context;

    private boolean ifTryAgain;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        context = getApplicationContext();

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
                    startLoader(location);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLoader(String location) {
        Bundle bndl1 = new Bundle();
        bndl1.putString(Constants.TAG_LOCATION_NAME, location);
        if (ifTryAgain)
            getLoaderManager().restartLoader(Constants.LOADER_ID_2, bndl1, this).forceLoad();
        else
            getLoaderManager().initLoader(Constants.LOADER_ID_2, bndl1, this).forceLoad();
    }

    @Override
    public Loader<List<Address>> onCreateLoader(int id, Bundle bundle) {
        if (id == Constants.LOADER_ID_2) {
            new GeocoderLoader(getApplicationContext(), bundle);
        }
        return new GeocoderLoader(getApplicationContext(), bundle);
    }

    @Override
    public void onLoadFinished(Loader<List<Address>> loader2, List<Address> addresses) {
        mGoogleMap.clear();
        if (addresses == null || addresses.size() == 0) {
            if (!ifTryAgain) {
                location = location.substring(0, location.indexOf(','));
                ifTryAgain = true;
                Log.d(Constants.TAG_LOG, location);

                startLoader(location);

            } else {
                Toast.makeText(MapsViewActivity.this, R.string.no_location_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            Address address = (Address) addresses.get(0);

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

    @Override
    public void onLoaderReset(Loader<List<Address>> loader) {
    }
}
