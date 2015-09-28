package com.example.myapplication3.app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;

import com.example.myapplication3.app.fragments.DetailFragment;
import com.example.myapplication3.app.fragments.MapsFragment;
import com.example.myapplication3.app.fragments.RecyclerViewFragment;
import com.example.myapplication3.app.models.GlobalModel;

public class MainActivity extends ActionBarActivity implements RecyclerViewFragment.OnFragmentInteractionListener {

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    private RecyclerViewFragment recyclerViewFragment;
    private DetailFragment detailFragment;
    private MapsFragment mapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        recyclerViewFragment = new RecyclerViewFragment();
        mFragmentTransaction.add(R.id.frgCont, recyclerViewFragment);
        mFragmentTransaction.addToBackStack("stack");
        mFragmentTransaction.commit();
    }

    @Override
    public void goDetailFragment(GlobalModel globalModel, int position) {

        Bundle args = new Bundle();
        args.putSerializable(GlobalModel.TAG_GLOBAL_MODEL, globalModel);
        args.putInt(GlobalModel.TAG_POSITION, position);

        mFragmentTransaction = mFragmentManager.beginTransaction();
        detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        mFragmentTransaction.replace(R.id.frgCont, detailFragment);
        mFragmentTransaction.addToBackStack("stack");
        mFragmentTransaction.commit();
    }

    @Override
    public void goMapsFragment(GlobalModel globalModel, int position) {
        Bundle args = new Bundle();
        args.putSerializable(GlobalModel.TAG_GLOBAL_MODEL, globalModel);
        args.putInt(GlobalModel.TAG_POSITION, position);

        mFragmentTransaction = mFragmentManager.beginTransaction();
        mapsFragment = new MapsFragment();
        mapsFragment.setArguments(args);
        mFragmentTransaction.replace(R.id.frgCont, mapsFragment);
        mFragmentTransaction.addToBackStack("stack");
        mFragmentTransaction.commit();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
