package com.example.myapplication3.app.adapters;

/**
 * Created by sasha on 23.09.2015.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication3.app.R;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.models.PairedObject;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private GlobalModel mGlobalModel;
    private List<Organization> mOrganizationList;
    private Organization mOrganization;
    private static MyClickListener myClickListener;


    // класс view holder-а с помощью которого мы получаем ссылку на каждый элемент
    // отдельного пункта списка
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // наш пункт состоит только из одного TextView
        public TextView mBankName;
        public TextView mRegion;
        public TextView mCity;
        public TextView mPhone;
        public TextView mAddress;


        public ViewHolder(View v) {
            super(v);
            mBankName = (TextView) v.findViewById(R.id.tv_bank_name_RI);
            mRegion = (TextView) v.findViewById(R.id.tv_region_RI);
            mCity = (TextView) v.findViewById(R.id.tv_city_RI);
            mPhone = (TextView) v.findViewById(R.id.tv_phone_RI);
            mAddress = (TextView) v.findViewById(R.id.tv_address_RI);

            v.findViewById(R.id.iv_link_RI).setOnClickListener(this);
            v.findViewById(R.id.iv_map_RI).setOnClickListener(this);
            v.findViewById(R.id.iv_phone_RI).setOnClickListener(this);
            v.findViewById(R.id.iv_next_RI).setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            myClickListener.onItemClick(getAdapterPosition(), view);

        }
    }

    // Конструктор
    public RecyclerAdapter(GlobalModel _globalModel) {
        mGlobalModel = _globalModel;
        mOrganizationList = mGlobalModel.getOrganizations();
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        mOrganization = mOrganizationList.get(position);

        String region = getRealName(mGlobalModel.getRegions(), mOrganization.getRegionId());
        String city = getRealName(mGlobalModel.getCities(), mOrganization.getCityId());
        holder.mPhone.setText(mOrganization.getPhone());
        holder.mAddress.setText(mOrganization.getAddress());
        holder.mBankName.setText(mOrganization.getTitle());
        holder.mRegion.setText(region);

        if (!region.equals(city)) {
            holder.mCity.setText(getRealName(mGlobalModel.getCities(), mOrganization.getCityId()));
        }
        else {
            holder.mCity.setText("");
        }

    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {

        return mOrganizationList.size();
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

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }



    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}