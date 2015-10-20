package com.example.myapplication3.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.TextView;

import com.example.myapplication3.app.Constants;
import com.example.myapplication3.app.R;
import com.example.myapplication3.app.adapters.DetailRecyclerAdapter;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

/**
 * Created by sasha on 22.09.2015.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    private GlobalModel mGlobalModel;
    private Organization mOrganization;
    private TextView mTvBankName;
    private TextView mTvInformation;
    private OnFragmentInteractionListener mListener;
    private int position;
    private DialogFragment mDialogFragmentInfo;
    private FloatingActionsMenu menuMultipleActions;
    private FloatingActionButton btnMenu;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        findViews(rootView);

        mRecyclerView.setHasFixedSize(true);
        btnMenu.setOnClickListener(this);

        Bundle bundle = getArguments();
        mGlobalModel = bundle.getParcelable(Constants.TAG_GLOBAL_MODEL);
        position = bundle.getInt(Constants.TAG_POSITION);
        mOrganization = mGlobalModel.getOrganizations().get(position);
        mTvBankName.setText(mOrganization.getTitle());
        mTvInformation.setText(getInformation());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DetailRecyclerAdapter(mOrganization.getCurrenciesReal(), mGlobalModel.getCurrenciesReal());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        setHasOptionsMenu(true);
        menuMultipleActions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
            }

            @Override
            public void onMenuCollapsed() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnMenu.setVisibility(View.VISIBLE);
                    }
                }, 140);
            }
        });
        return rootView;
    }

    private void findViews(View rootView) {
        mTvBankName = (TextView) rootView.findViewById(R.id.tv_name_of_bank_SF);
        mTvInformation = (TextView) rootView.findViewById(R.id.tv_information_SF);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_FD);
        btnMenu = (FloatingActionButton) rootView.findViewById(R.id.pink_icon);
        menuMultipleActions = (FloatingActionsMenu) rootView.findViewById(R.id.multiple_actions);
        rootView.findViewById(R.id.btn_call_FAB).setOnClickListener(this);
        rootView.findViewById(R.id.btn_map_FAB).setOnClickListener(this);
        rootView.findViewById(R.id.btn_link_FAB).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_call_FAB:
                callToPhone();
                break;
            case R.id.btn_map_FAB:
                mListener.goMapsFragment(mGlobalModel, position);
                break;
            case R.id.btn_link_FAB:
                goToLink();
                break;
            case R.id.pink_icon:
                menuMultipleActions.expand();
                btnMenu.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private String getInformation() {

        String address = mOrganization.getAddress();
        String region = Constants.getRealName(mGlobalModel.getRegionsReal(), mOrganization.getRegionId());
        String city = Constants.getRealName(mGlobalModel.getCitiesReal(), mOrganization.getCityId());
        String phoneNumber = mOrganization.getPhone();
        String allInformation = region + "\n";
        if (!region.equals(city))
            allInformation = allInformation + city + "\n";
        allInformation = allInformation + getActivity().getString(R.string.address) + " " + address + "\n"
                + getActivity().getString(R.string.tel) + " " + phoneNumber;
        return allInformation;
    }

    private void goToLink() {
        List<Organization> organizationList = mGlobalModel.getOrganizations();
        Organization organization = organizationList.get(position);
        String link = organization.getLink();
        Log.d(Constants.TAG_LOG, link);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        startActivity(intent);
    }

    private void callToPhone() {
        List<Organization> organizationList = mGlobalModel.getOrganizations();
        Organization organization = organizationList.get(position);
        String phone = organization.getPhone();
        String uri = "tel:" + phone.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    public static DetailFragment getInstance(GlobalModel globalModel, int position) {
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(Constants.TAG_GLOBAL_MODEL, globalModel);
        mBundle.putInt(Constants.TAG_POSITION, position);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(mBundle);
        return detailFragment;
    }

    public interface OnFragmentInteractionListener {

        void goMapsFragment(GlobalModel globalModel, int position);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share_menu_MDF) {
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mGlobalModel.getOrganizations().get(position).getTitle());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    private void showDialog() {
        mDialogFragmentInfo = new DialogFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(Constants.TAG_GLOBAL_MODEL, mGlobalModel);
        mBundle.putInt(Constants.TAG_POSITION, position);
        mDialogFragmentInfo.setArguments(mBundle);
        mDialogFragmentInfo.show(getActivity().getFragmentManager(), Constants.TAG_FRAGMENT);
    }
}
