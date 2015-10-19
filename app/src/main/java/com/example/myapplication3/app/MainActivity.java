package com.example.myapplication3.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.myapplication3.app.fragments.DetailFragment;
import com.example.myapplication3.app.fragments.RecyclerViewFragment;
import com.example.myapplication3.app.loaders.GetModelFromDBLoader;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.service.UpdatingService;


public class MainActivity extends AppCompatActivity implements RecyclerViewFragment.OnFragmentInteractionListener,
        DetailFragment.OnFragmentInteractionListener, LoaderManager.LoaderCallbacks<GlobalModel> {
    private ActionBar actionBar;

    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         getLoaderManager().initLoader(Constants.LOADER_ID_1, null, this).forceLoad();

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
        mBundle.putParcelable(Constants.TAG_GLOBAL_MODEL, globalModelFromDB);
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        recyclerViewFragment.setArguments(mBundle);
        addFragment(recyclerViewFragment);
    }

    @Override
    public void goDetailFragment(GlobalModel globalModel, int position) {

        addFragment(DetailFragment.getInstance(globalModel, position));
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
            if (fragment instanceof DetailFragment) {
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

    @Override
    public Loader<GlobalModel> onCreateLoader(int id, Bundle bundle) {

        if (id == Constants.LOADER_ID_1) {
            return  new GetModelFromDBLoader(this);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<GlobalModel> loader, final GlobalModel globalModel) {
        Log.d(Constants.TAG_LOG,"end LOADER");
        final int WHAT = 1;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == WHAT) {
                    goRecyclerViewFragment(globalModel);
                findViewById(R.id.pb_MA).setVisibility(View.INVISIBLE);}
            }
        };
        handler.sendEmptyMessage(WHAT);
    }

    @Override
    public void onLoaderReset(Loader<GlobalModel> loader) {}
}
