package com.example.myapplication3.app.fragments;

import android.app.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication3.app.Constants;
import com.example.myapplication3.app.R;
import com.example.myapplication3.app.models.Currency;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.models.PairedObject;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by sasha on 22.09.2015.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    private GlobalModel mGlobalModel;
    private Organization mOrganization;
    private TextView mTvBankName;
    private TextView mTvInformation;
    private LinearLayout mLlContainerForCurrency;
    private OnFragmentInteractionListener mListener;
    private int position;
    private DialogFragment mDialogFragmentInfo;
    private FloatingActionsMenu menuMultipleActions;
    private FloatingActionButton btnMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mTvBankName = (TextView) rootView.findViewById(R.id.tv_name_of_bank_SF);
        mTvInformation = (TextView) rootView.findViewById(R.id.tv_information_SF);
        mLlContainerForCurrency = (LinearLayout) rootView.findViewById(R.id.ll_container_for_currency_SR);

        rootView.findViewById(R.id.btn_call_FAB).setOnClickListener(this);
        rootView.findViewById(R.id.btn_map_FAB).setOnClickListener(this);
        rootView.findViewById(R.id.btn_link_FAB).setOnClickListener(this);
        btnMenu = (FloatingActionButton) rootView.findViewById(R.id.pink_icon);
        btnMenu.setOnClickListener(this);

        Bundle bundle = getArguments();
        Gson gson = new GsonBuilder().create();
        mGlobalModel = gson.fromJson(bundle.getString(Constants.TAG_GLOBAL_MODEL), GlobalModel.class);
        position = bundle.getInt(Constants.TAG_POSITION);
        mOrganization = mGlobalModel.getOrganizations().get(position);

        mTvBankName.setText(mOrganization.getTitle());
        mTvInformation.setText(getInformation());
        addLlInCardView();
        setHasOptionsMenu(true);

        menuMultipleActions = (FloatingActionsMenu) rootView.findViewById(R.id.multiple_actions);
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       if ( ((AppCompatActivity) getActivity()).getSupportActionBar() != null){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mGlobalModel.getOrganizations().get(position).getTitle());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);}
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

    public interface OnFragmentInteractionListener {

        public void goMapsFragment(GlobalModel globalModel, int position);
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

    private void addLlInCardView() {

        List<Currency> currencyList = mOrganization.getCurrenciesReal();

        for (Currency item : currencyList) {
            View mLlCurrencyItem = getActivity().getLayoutInflater().inflate(R.layout.item_currency, null);
            TextView tvCurrency = (TextView) mLlCurrencyItem.findViewById(R.id.tv_currency_name_CI);
            TextView tvBuy = (TextView) mLlCurrencyItem.findViewById(R.id.tv_buy_CI);
            TextView tvSell = (TextView) mLlCurrencyItem.findViewById(R.id.tv_sell_CI);
            ImageView ivSell = (ImageView) mLlCurrencyItem.findViewById(R.id.iv_sell_CI);
            ImageView ivBuy = (ImageView) mLlCurrencyItem.findViewById(R.id.iv_buy_CI);
            tvBuy.setText(item.getAsk());
            tvSell.setText(item.getBid());
            tvCurrency.setText(Constants.getRealName(mGlobalModel.getCurrenciesReal(), item.getNameCurrency()));

            float ask = Float.parseFloat(item.getAsk());
            float olgAsk = Float.parseFloat(item.getPreviousAck());
            float bid = Float.parseFloat(item.getBid());
            float olgBid = Float.parseFloat(item.getPreviousBid());
            if (bid < olgBid) {
                tvSell.setTextColor(getResources().getColor(R.color.color_red_down));
                ivSell.setImageResource(R.drawable.ic_red_arrow_down);
            } else {
                tvSell.setTextColor(getResources().getColor(R.color.color_green_up));
                ivSell.setImageResource(R.drawable.ic_green_arrow_up);
            }
            if (ask < olgAsk) {
                tvBuy.setTextColor(getResources().getColor(R.color.color_red_down));
                ivBuy.setImageResource(R.drawable.ic_red_arrow_down);
            } else {
                tvBuy.setTextColor(getResources().getColor(R.color.color_green_up));
                ivBuy.setImageResource(R.drawable.ic_green_arrow_up);
            }
            mLlContainerForCurrency.addView(mLlCurrencyItem);
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
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(mGlobalModel);
        mBundle.putString(Constants.TAG_GLOBAL_MODEL, json);
        mBundle.putInt(Constants.TAG_POSITION, position);
        mDialogFragmentInfo.setArguments(mBundle);
        mDialogFragmentInfo.show(getActivity().getFragmentManager(), Constants.TAG_FRAGMENT);
    }
}
