package com.example.myapplication3.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;

import com.example.myapplication3.app.fragments.DetailFragment;
import com.example.myapplication3.app.fragments.MapsFragment;
import com.example.myapplication3.app.fragments.RecyclerViewFragment;
import com.example.myapplication3.app.models.GlobalModel;

public class MainActivity extends ActionBarActivity implements RecyclerViewFragment.OnFragmentInteractionListener {


    private RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
    private DetailFragment detailFragment = new DetailFragment();
    private MapsFragment mapsFragment = new MapsFragment();
    private Fragment mFragment = recyclerViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ButterKnife.bind(this);

        addFragment(mFragment);
    }

    @Override
    public void goDetailFragment(GlobalModel globalModel, int position) {

        Bundle args = new Bundle();
        args.putSerializable(GlobalModel.TAG_GLOBAL_MODEL, globalModel);
        args.putInt(GlobalModel.TAG_POSITION, position);
        detailFragment.setArguments(args);

        addFragment(detailFragment);
    }

    @Override
    public void goMapsFragment(GlobalModel globalModel, int position) {
        Bundle args = new Bundle();
        args.putSerializable(GlobalModel.TAG_GLOBAL_MODEL, globalModel);
        args.putInt(GlobalModel.TAG_POSITION, position);
        mapsFragment.setArguments(args);

        addFragment(mapsFragment);
    }

    private void addFragment(Fragment fragment) {
        Log.d("qqq", "replace Fragment");

        if (!(fragment.isVisible())) {
//            fragment.setRetainInstance(true);
            mFragment = fragment;
            FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.animator.add_frag_animator, R.animator.rem_frag_animator);
            mFragmentTransaction.replace(R.id.frgCont, fragment, "my_fragment");
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.commit();
        }
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
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
