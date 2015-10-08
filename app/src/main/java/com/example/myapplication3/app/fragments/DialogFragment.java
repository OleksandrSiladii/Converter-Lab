package com.example.myapplication3.app.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication3.app.R;
import com.example.myapplication3.app.models.Currency;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.models.PairedObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by sasha on 01.10.2015.
 */
public class DialogFragment extends android.app.DialogFragment implements View.OnClickListener {
    private ImageView mImageView;
    private GlobalModel mGlobalModel;
    private int position;
    private Bitmap mBitmap;
    private Organization mOrganization;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.dialog_fragment, container, false);
        mImageView = (ImageView) rootView.findViewById(R.id.iv_bank_DF);
        rootView.findViewById(R.id.btn_share_DF).setOnClickListener(this);
        Button b = new Button(getActivity());
        b.setText("gh");
        createBitmap();

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void createBitmap() {
        mImageView.setImageBitmap(getBitmapFromView(getConstructionView()));
    }

    private LinearLayout getConstructionView() {
        Bundle bundle = getArguments();
        Gson gson = new GsonBuilder().create();
        mGlobalModel = gson.fromJson(bundle.getString(GlobalModel.TAG_GLOBAL_MODEL), GlobalModel.class);
        position = bundle.getInt(GlobalModel.TAG_POSITION);

        mOrganization = mGlobalModel.getOrganizations().get(position);

        LinearLayout mLlBankItem = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_item, null);

        TextView tvBankName = (TextView) mLlBankItem.findViewById(R.id.tv_name_of_bank_DI);
        TextView tvBankInformation = (TextView) mLlBankItem.findViewById(R.id.tv_information_DI);
        LinearLayout llCurrencies = (LinearLayout) mLlBankItem.findViewById(R.id.ll_container_for_currency_DI);
        String region = getRealName(mGlobalModel.getRegionsReal(), mOrganization.getRegionId());
        String city = getRealName(mGlobalModel.getCitiesReal(), mOrganization.getCityId());
        String information;
        if (region.equals(city)) information = region;
        else information = region + "\n" + city;
        tvBankName.setText(mOrganization.getTitle());
        tvBankInformation.setText(information);

        for (Currency currency : mOrganization.getCurrenciesReal()) {
            View mLlCurrencyItem = getActivity().getLayoutInflater().inflate(R.layout.dialig_curreny_item, null);
            TextView tvCurrency = (TextView) mLlCurrencyItem.findViewById(R.id.tv_currency_name_DCI);
            TextView tvBuySell = (TextView) mLlCurrencyItem.findViewById(R.id.tv_currency_buy_sell_DCI);
            String ask = currency.getAsk().substring(0, 5);
            String bid = currency.getBid().substring(0, 5);
            tvCurrency.setText(currency.getNameCurrency());
            tvBuySell.setText(ask + "/" + bid);
            llCurrencies.addView(mLlCurrencyItem);
        }

        return mLlBankItem;
    }

    private Bitmap getBitmapFromView(View view) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);

        view.measure((int) (metricsB.widthPixels * 0.6), LinearLayout.LayoutParams.WRAP_CONTENT);

        mBitmap = Bitmap.createBitmap(((int) (metricsB.widthPixels * 0.5)), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return mBitmap;

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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_share_DF) {
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/bitmap.png";

            new AsyncTask<String, Void, File>() {
                @Override
                protected File doInBackground(String... strings) {
                    File file = null;
                    try {
                        file = new File(strings[0]);
                        FileOutputStream fOut = new FileOutputStream(file);
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return file;
                }

                @Override
                protected void onPostExecute(File file) {
                    if (file.exists()) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("image/jpg");
                        file.setReadable(true, false);
//            Uri uri = Uri.fromFile(new File(getFilesDir(), "foo.jpg"));
                        Uri uri = Uri.fromFile(file);
                        dismiss();
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        getActivity().startActivity(Intent.createChooser(shareIntent, "Share image using"));
                    }
                }
            }.execute(filePath);


//            file.setReadable(true, false);

        }
    }
}
