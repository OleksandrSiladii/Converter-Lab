package com.example.myapplication3.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication3.app.DB.DBWorker;
import com.example.myapplication3.app.fragments.DetailFragment;
import com.example.myapplication3.app.fragments.RecyclerViewFragment;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.models.PairedObject;
import com.example.myapplication3.app.service.UpdatingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener {

    private ActionBar actionBar;
    private DetailFragment detailFragment;
    private Bundle mBundle;
    private DBWorker mDBWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBWorker = new DBWorker(this);
        goRecyclerViewFragment(mDBWorker.getGlobalModelFromDB());
        startUpdatingService();
        supportCustomActionBar();
    }

    private void supportCustomActionBar() {
        actionBar = getSupportActionBar();
        if (!(actionBar == null)) {
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    private void startUpdatingService() {
        Intent intent = new Intent(this, UpdatingService.class);
        startService(intent);
    }

    private void goRecyclerViewFragment(GlobalModel globalModelFromDB) {
        mBundle = new Bundle();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(globalModelFromDB);
        mBundle.putString(Constants.TAG_GLOBAL_MODEL, json);
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        recyclerViewFragment.setArguments(mBundle);
        addFragment(recyclerViewFragment);
    }

    @Override
    public void goDetailFragment(GlobalModel globalModel, int position) {
        mBundle = new Bundle();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(globalModel);
        mBundle.putString(Constants.TAG_GLOBAL_MODEL, json);
        mBundle.putInt(Constants.TAG_POSITION, position);
        detailFragment = new DetailFragment();
        detailFragment.setArguments(mBundle);

        addFragment(detailFragment);
    }

    @Override
    public void goMapsFragment(GlobalModel globalModel, int position) {
        mBundle = new Bundle();
        Organization organization = globalModel.getOrganizations().get(position);
        mBundle.putString(Constants.TAG_CITY, Constants.getRealName(globalModel.getCitiesReal(), organization.getCityId()));
        mBundle.putString(Constants.TAG_REGION, Constants.getRealName(globalModel.getRegionsReal(), organization.getRegionId()));
        mBundle.putString(Constants.TAG_ADDRESS, organization.getAddress());
        Intent intent = new Intent(MainActivity.this, MapsViewActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }



    private void addFragment(Fragment fragment) {
        Log.d(Constants.TAG_LOG, "replace Fragment");

        if (!(fragment.isVisible())) {
            FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.animator.add_frag_animator, R.animator.rem_frag_animator,
                    R.animator.add2_frag_animator, R.animator.rem2_frag_animator);
            mFragmentTransaction.replace(R.id.frgCont, fragment, "my_fragment");
            if (fragment == detailFragment) {
                mFragmentTransaction.addToBackStack(null);
            }
            mFragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
