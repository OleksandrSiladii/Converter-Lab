package com.example.myapplication3.app.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication3.app.DB.DBWorker;
import com.example.myapplication3.app.R;
import com.example.myapplication3.app.adapters.RecyclerAdapter;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.models.PairedObject;
import com.example.myapplication3.app.rest.RetrofitAdapter;
import com.example.myapplication3.app.service.UpdatingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sasha on 22.09.2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GlobalModel mGlobalModel;
    private OnFragmentInteractionListener mListener;
    private ProgressBar mProgressBarLoad;
    private DBWorker mDBWorker;
    BroadcastReceiver mBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recycler_view_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_RVF);
        mProgressBarLoad = (ProgressBar) rootView.findViewById(R.id.pb_load_RF);
        mDBWorker = new DBWorker(getActivity());

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        Bundle bundle = getArguments();
        Gson gson = new GsonBuilder().create();
        mGlobalModel = gson.fromJson(bundle.getString(GlobalModel.TAG_GLOBAL_MODEL), GlobalModel.class);

        setModelInRecyclerView(mGlobalModel);

        setHasOptionsMenu(true);

        return rootView;
    }

    public void setModelInRecyclerView(GlobalModel globalModel) {
        mGlobalModel = globalModel;

        mAdapter = new RecyclerAdapter(mGlobalModel);
        mProgressBarLoad.setVisibility(View.INVISIBLE);

        mRecyclerView.setAdapter(mAdapter);
        ((RecyclerAdapter) mAdapter).setOnItemClickListener(new RecyclerAdapter.MyClickListener() {

            @Override
            public void onItemClick(int position, View view) {


                switch (view.getId()) {
                    case R.id.iv_link_RI:
                        goToLink(position);
                        break;
                    case R.id.iv_map_RI:
                        mListener.goMapsFragment(mGlobalModel, position);
                        break;
                    case R.id.iv_phone_RI:
                        callToPhone(position);
                        break;
                    case R.id.iv_next_RI:
                        Log.d("qqq", "next");
                        mListener.goDetailFragment(mGlobalModel, position);
                        break;
                }
            }
        });
    }

    private void callToPhone(int position) {

        List<Organization> organizationList = mGlobalModel.getOrganizations();
        Organization organization = organizationList.get(position);
        String phone = organization.getPhone();
        String uri = "tel:" + phone.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void goToLink(int position) {

        List<Organization> organizationList = mGlobalModel.getOrganizations();
        Organization organization = organizationList.get(position);
        String link = organization.getLink();
        Log.d("qqq", link);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        startActivity(intent);
    }

    public interface OnFragmentInteractionListener {

        public void goDetailFragment(GlobalModel globalModel, int position);

        public void goMapsFragment(GlobalModel globalModel, int position);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recycler_view_fragment, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_menu).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                              public boolean onQueryTextChange(String text) {
                                                  getRezOfSearch(text);
                                                  Log.d("qqq","SearchView action");
                                                  return false;
                                              }

                                              public boolean onQueryTextSubmit(String text) {
                                                  return false;
                                              }
                                          }
        );
    }

    private void getRezOfSearch(String searchText) {
        GlobalModel rezOfSearch = mGlobalModel;
        List<Organization> organizations = new ArrayList<Organization>();
        for (Organization organization : mGlobalModel.getOrganizations()) {
            String city = getRealName(mGlobalModel.getCitiesReal(), organization.getCityId());
            String region = getRealName(mGlobalModel.getRegionsReal(), organization.getRegionId());

            if (organization.getTitle().contains(searchText) || organization.getAddress().contains(searchText)
                    || city.contains(searchText) || region.contains(searchText)) {
                organizations.add(organization);
            }
        }
        rezOfSearch.setOrganizations(organizations);
        setModelInRecyclerView(rezOfSearch);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        regBroadcastReceiver();
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(mBroadcastReceiver);
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


    private void regBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getBundleExtra(GlobalModel.TAG_GLOBAL_MODEL);
                Gson gson = new GsonBuilder().create();
                GlobalModel globalModel = gson.fromJson(bundle.getString(GlobalModel.TAG_GLOBAL_MODEL), GlobalModel.class);
                setModelInRecyclerView(globalModel);
            }
        };
        IntentFilter intentFilter = new IntentFilter(UpdatingService.BROADCAST_ACTION);
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }

}

