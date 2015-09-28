package com.example.myapplication3.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication3.app.R;
import com.example.myapplication3.app.adapters.RecyclerAdapter;
import com.example.myapplication3.app.models.Currency;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.models.PairedObject;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by sasha on 22.09.2015.
 */
public class DetailFragment extends Fragment {

    private GlobalModel mGlobalModel;
    private Organization mOrganization;
    private int position;

    TextView mTvBankName;
    TextView mTvInformation;
    LinearLayout mLlContainerForCurrency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.scroll_view_detail_fragment, container, false);
        mTvBankName = (TextView) rootView.findViewById(R.id.tv_name_of_bank_SF);
        mTvInformation = (TextView) rootView.findViewById(R.id.tv_information_SF);
        mLlContainerForCurrency = (LinearLayout) rootView.findViewById(R.id.ll_container_for_currency_SR);

        Bundle args = getArguments();
        mGlobalModel = (GlobalModel) args.getSerializable(GlobalModel.TAG_GLOBAL_MODEL);
        position = args.getInt(GlobalModel.TAG_POSITION);

        mOrganization = mGlobalModel.getOrganizations().get(position);

        mTvBankName.setText(mOrganization.getTitle());
        mTvInformation.setText(getInformation());
        addLlInCardView();

        return rootView;
    }

    public String getInformation() {

        String address = mOrganization.getAddress();
        String region = getRealName(mGlobalModel.getRegions(), mOrganization.getRegionId());
        String city = getRealName(mGlobalModel.getCities(), mOrganization.getCityId());
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

    private void addLlInCardView() {

        List<Currency> currencyList = mOrganization.getCurrencies();


        for (Currency item : currencyList) {
            View mLlCurrencyItem = getActivity().getLayoutInflater().inflate(R.layout.currency_item, null);
            TextView tvCurrency = (TextView) mLlCurrencyItem.findViewById(R.id.tv_currency_name_CI);
            TextView tvBuy = (TextView) mLlCurrencyItem.findViewById(R.id.tv_buy_CI);
            TextView tvSell = (TextView) mLlCurrencyItem.findViewById(R.id.tv_sell_CI);
            tvBuy.setText(item.getAsk());
            tvSell.setText(item.getBid());
            tvCurrency.setText(getRealName(mGlobalModel.getCurrencies(), item.getName()));
            mLlContainerForCurrency.addView(mLlCurrencyItem);

        }
    }
}
