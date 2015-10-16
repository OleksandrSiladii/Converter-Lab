package com.example.myapplication3.app.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.myapplication3.app.R;
import com.example.myapplication3.app.workers.Constants;
import java.util.List;

import android.util.Log;
import android.widget.ImageView;
import com.example.myapplication3.app.models.Currency;
import com.example.myapplication3.app.models.PairedObject;


/**
 * Created by sasha on 15.10.2015.
 */
public class DetailRecyclerAdapter extends RecyclerView.Adapter<DetailRecyclerAdapter.ViewHolder> {

    private List<Currency> mCurrencies;
    private List<PairedObject> mRealNameCurrency;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCurrencyName;
        public TextView tvAsk;
        public TextView tvmBid;
        public ImageView ivAsk;
        public ImageView ivBid;

        public ViewHolder(View v) {
            super(v);
            tvCurrencyName = (TextView) v.findViewById(R.id.tv_currency_name_CI);
            tvAsk = (TextView) v.findViewById(R.id.tv_buy_CI);
            tvmBid = (TextView) v.findViewById(R.id.tv_sell_CI);
            ivAsk = (ImageView) v.findViewById(R.id.iv_buy_CI);
            ivBid = (ImageView) v.findViewById(R.id.iv_sell_CI);
        }
    }

    public DetailRecyclerAdapter(List<Currency> currencies, List<PairedObject> realNameCurrency) {
        mCurrencies = currencies;
        mRealNameCurrency = realNameCurrency;
        Log.d(Constants.TAG_LOG, "create DetailRecyclerAdapter");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);

        ViewHolder vh = new ViewHolder(v);
        Log.d(Constants.TAG_LOG, "create ViewHolder");
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Currency currency = mCurrencies.get(position);

        String currencyName = Constants.getRealName(mRealNameCurrency, currency.getNameCurrency());
        String ask = currency.getAsk();
        String bid = currency.getBid();

        holder.tvCurrencyName.setText(currencyName);
        holder.tvAsk.setText(ask);
        holder.tvmBid.setText(bid);

        Log.d(Constants.TAG_LOG, "holder: "+currencyName);

        float fAsk = Float.parseFloat(currency.getAsk());
        float fOlgAsk = Float.parseFloat(currency.getPreviousAck());
        float fBid = Float.parseFloat(currency.getBid());
        float fOlgBid = Float.parseFloat(currency.getPreviousBid());
        if (fBid < fOlgBid) {
            holder.tvmBid.setTextColor(R.color.color_red_down);
            holder.ivBid.setImageResource(R.drawable.ic_red_arrow_down);
        } else {
            holder.tvmBid.setTextColor(R.color.color_green_up);
            holder.ivBid.setImageResource(R.drawable.ic_green_arrow_up);
        }
        if (fAsk < fOlgAsk) {
            holder.tvAsk.setTextColor(R.color.color_red_down);
            holder.ivAsk.setImageResource(R.drawable.ic_red_arrow_down);
        } else {
            holder.tvAsk.setTextColor(R.color.color_green_up);
            holder.ivAsk.setImageResource(R.drawable.ic_green_arrow_up);
        }
    }

    @Override
    public int getItemCount() {
        return mCurrencies.size();
    }
}