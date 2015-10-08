package com.example.myapplication3.app;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.myapplication3.app.DB.DBWorker;
import com.example.myapplication3.app.fragments.DetailFragment;
import com.example.myapplication3.app.fragments.RecyclerViewFragment;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.models.PairedObject;
import com.example.myapplication3.app.service.UpdatingService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewFragment.OnFragmentInteractionListener,DetailFragment.OnFragmentInteractionListener {


    private DetailFragment detailFragment = new DetailFragment();

    private Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBWorker mDBWorker = new DBWorker(this);

        goRecyclerViewFragment(mDBWorker.getGlobalModelFromDB());

        startUpdatingService();
    }

    private void startUpdatingService() {

        Intent intent = new Intent(this, UpdatingService.class);
        startService(intent);
    }

    private void goRecyclerViewFragment(GlobalModel globalModelFromDB) {
        mBundle = new Bundle();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(globalModelFromDB);
        mBundle.putString(GlobalModel.TAG_GLOBAL_MODEL, json);
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        recyclerViewFragment.setArguments(mBundle);
        addFragment(recyclerViewFragment);
    }

    @Override
    public void goDetailFragment(GlobalModel globalModel, int position) {

        mBundle = new Bundle();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(globalModel);
        mBundle.putString(GlobalModel.TAG_GLOBAL_MODEL, json);
        mBundle.putInt(GlobalModel.TAG_POSITION, position);
        detailFragment.setArguments(mBundle);

        addFragment(detailFragment);
    }
    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    @Override
    public void goMapsFragment(GlobalModel globalModel, int position) {
        mBundle = new Bundle();
        Organization organization = globalModel.getOrganizations().get(position);
        mBundle.putString(MapsViewActivity.CITY, getRealName(globalModel.getCitiesReal(), organization.getCityId()));
        mBundle.putString(MapsViewActivity.REGION, getRealName(globalModel.getRegionsReal(), organization.getRegionId()));
        mBundle.putString(MapsViewActivity.ADDRESS,organization.getAddress());
        Intent intent = new Intent(MainActivity.this, MapsViewActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    private String getRealName(List<PairedObject> pairedObjectList, String id) {
        for (PairedObject item : pairedObjectList) {
            if (item.getId().equals(id)) {
                String rez = item.getName();
                rez = rez.replaceAll("\"", "");
                return rez;
            }
        }
        return id;
    }

    private void addFragment(Fragment fragment) {
        Log.d("qqq", "replace Fragment");

        if (!(fragment.isVisible())) {
//            fragment.setRetainInstance(true);

            FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.animator.add_frag_animator, R.animator.rem_frag_animator);
            mFragmentTransaction.replace(R.id.frgCont, fragment, "my_fragment");
            if (fragment == detailFragment){
            mFragmentTransaction.addToBackStack(null);}
            mFragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(MainActivity.this);
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(MainActivity.this, "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            googleMap.clear();

            // Adding Markers on Google Map for each matching address
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

                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

}
