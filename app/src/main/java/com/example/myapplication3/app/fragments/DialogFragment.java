package com.example.myapplication3.app.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.myapplication3.app.Constants;
import com.example.myapplication3.app.R;
import com.example.myapplication3.app.adapters.DrawView;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by sasha on 01.10.2015.
 */
public class DialogFragment extends android.app.DialogFragment implements View.OnClickListener {

    private Bitmap mBitmap;
    DrawView v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_dialog, container, false);

        rootView.findViewById(R.id.btn_share_DF).setOnClickListener(this);
        v = (DrawView)rootView.findViewById(R.id.dv_FD);

        Bundle bundle = getArguments();

        GlobalModel mGlobalModel =  bundle.getParcelable(Constants.TAG_GLOBAL_MODEL) ;
        int position = bundle.getInt(Constants.TAG_POSITION);
        Organization organization = mGlobalModel.getOrganizations().get(position);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        v.setName(organization.getTitle());
        v.setCity(organization.getCityId());
        v.setRegion(organization.getRegionId());
        v.setCurrencyList(organization.getCurrenciesReal());
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private Bitmap getBitmapFromView(View view) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);

        view.measure((int) (metricsB.widthPixels * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT);

        mBitmap = Bitmap.createBitmap(((int) (metricsB.widthPixels * 0.7)), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return mBitmap;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_share_DF) {
            mBitmap = getBitmapFromView(v);

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
        }
    }
}
