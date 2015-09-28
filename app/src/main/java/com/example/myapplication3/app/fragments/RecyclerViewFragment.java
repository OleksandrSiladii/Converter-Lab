package com.example.myapplication3.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication3.app.DB.DBWorker;
import com.example.myapplication3.app.R;
import com.example.myapplication3.app.adapters.RecyclerAdapter;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.rest.RetrofitAdapter;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sasha on 22.09.2015.
 */
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GlobalModel mGlobalModel;
    private OnFragmentInteractionListener mListener;
    private ProgressBar mProgressBarLoad;
    private DBWorker mDBWorker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recycler_view_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_RVF);
        mProgressBarLoad = (ProgressBar) rootView.findViewById(R.id.pb_load_RF);
        mDBWorker = new DBWorker();

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getGlobalModel();
        return rootView;
    }


    private void getGlobalModel() {

        RetrofitAdapter.getInterface().getJson(new Callback<GlobalModel>() {
            @Override
            public void success(GlobalModel globalModel, Response response) {
                globalModel.deresialize();

                setModelInRecyclerView(mDBWorker.addNewGlobalModelToDB(getActivity(),globalModel));
            }

            @Override
            public void failure(RetrofitError error) {
                setModelInRecyclerView(mDBWorker.getGlobalModelFromDB(getActivity()));
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

    public interface OnFragmentInteractionListener {

        public void goDetailFragment(GlobalModel globalModel, int position);

        public void goMapsFragment(GlobalModel globalModel, int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }
}

