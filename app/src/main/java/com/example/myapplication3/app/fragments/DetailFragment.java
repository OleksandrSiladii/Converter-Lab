package com.example.myapplication3.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.myapplication3.app.R;
import com.example.myapplication3.app.models.Currency;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.models.PairedObject;
import com.getbase.floatingactionbutton.FloatingActionButton;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.scroll_view_detail_fragment, container, false);
        mTvBankName = (TextView) rootView.findViewById(R.id.tv_name_of_bank_SF);
        mTvInformation = (TextView) rootView.findViewById(R.id.tv_information_SF);
        mLlContainerForCurrency = (LinearLayout) rootView.findViewById(R.id.ll_container_for_currency_SR);

        rootView.findViewById(R.id.btn_call_FAB).setOnClickListener(this);
        rootView.findViewById(R.id.btn_map_FAB).setOnClickListener(this);
        rootView.findViewById(R.id.btn_link_FAB).setOnClickListener(this);

        Bundle bundle = getArguments();
        Gson gson = new GsonBuilder().create();
        mGlobalModel = gson.fromJson(bundle.getString(GlobalModel.TAG_GLOBAL_MODEL), GlobalModel.class);
        position = bundle.getInt(GlobalModel.TAG_POSITION);

        mOrganization = mGlobalModel.getOrganizations().get(position);

        mTvBankName.setText(mOrganization.getTitle());
        mTvInformation.setText(getInformation());
        addLlInCardView();
        setHasOptionsMenu(true);

        return rootView;
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
        }
    }

    private String getInformation() {

        String address = mOrganization.getAddress();
        String region = getRealName(mGlobalModel.getRegionsReal(), mOrganization.getRegionId());
        String city = getRealName(mGlobalModel.getCitiesReal(), mOrganization.getCityId());
        String phoneNumber = mOrganization.getPhone();

        String allInformation = region + "\n";

        if (!region.equals(city))
            allInformation = allInformation + city + "\n";

        allInformation = allInformation + getActivity().getString(R.string.address) + " " + address + "\n"
                + getActivity().getString(R.string.tel) + " " + phoneNumber;


        return allInformation;
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

    private void goToLink() {
        List<Organization> organizationList = mGlobalModel.getOrganizations();
        Organization organization = organizationList.get(position);
        String link = organization.getLink();
        Log.d("qqq", link);
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
            View mLlCurrencyItem = getActivity().getLayoutInflater().inflate(R.layout.currency_item, null);
            TextView tvCurrency = (TextView) mLlCurrencyItem.findViewById(R.id.tv_currency_name_CI);
            TextView tvBuy = (TextView) mLlCurrencyItem.findViewById(R.id.tv_buy_CI);
            TextView tvSell = (TextView) mLlCurrencyItem.findViewById(R.id.tv_sell_CI);
            ImageView ivSell = (ImageView) mLlCurrencyItem.findViewById(R.id.iv_sell_CI);
            ImageView ivBuy = (ImageView) mLlCurrencyItem.findViewById(R.id.iv_buy_CI);
            tvBuy.setText(item.getAsk());
            tvSell.setText(item.getBid());
            tvCurrency.setText(getRealName(mGlobalModel.getCurrenciesReal(), item.getNameCurrency()));

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
            Log.d("qqq", "ask: " + ask + " old ask: " + olgAsk);
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
        mBundle.putString(GlobalModel.TAG_GLOBAL_MODEL, json);
        mBundle.putInt(GlobalModel.TAG_POSITION, position);
        mDialogFragmentInfo.setArguments(mBundle);

        mDialogFragmentInfo.show(getFragmentManager(), "dlg2");

    }
}
