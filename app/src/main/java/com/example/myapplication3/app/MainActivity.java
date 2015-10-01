package com.example.myapplication3.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.example.myapplication3.app.DB.DBWorker;
import com.example.myapplication3.app.fragments.DetailFragment;
import com.example.myapplication3.app.fragments.MapsFragment;
import com.example.myapplication3.app.fragments.RecyclerViewFragment;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.service.UpdatingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity implements RecyclerViewFragment.OnFragmentInteractionListener {


    private RecyclerViewFragment recyclerViewFragment;
    private DetailFragment detailFragment = new DetailFragment();
    private MapsFragment mapsFragment = new MapsFragment();
    private BroadcastReceiver mBroadcastReceiver;
    private DBWorker mDBWorker;
    private Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBWorker = new DBWorker(this);

//        ButterKnife.bind(this);

        goRecyclerViewFragment(mDBWorker.getGlobalModelFromDB());


        startUpdatingService();
//        regBroadcastReceiver();
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
        recyclerViewFragment = new RecyclerViewFragment();
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

    @Override
    public void goMapsFragment(GlobalModel globalModel, int position) {
        mBundle = new Bundle();
        mBundle.putSerializable(GlobalModel.TAG_GLOBAL_MODEL, globalModel);
        mBundle.putInt(GlobalModel.TAG_POSITION, position);
        mapsFragment.setArguments(mBundle);

        addFragment(mapsFragment);
    }

    private void addFragment(Fragment fragment) {
        Log.d("qqq", "replace Fragment");

        if (!(fragment.isVisible())) {
//            fragment.setRetainInstance(true);

            FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.animator.add_frag_animator, R.animator.rem_frag_animator);
            mFragmentTransaction.replace(R.id.frgCont, fragment, "my_fragment");
            mFragmentTransaction.addToBackStack(null);
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
}
