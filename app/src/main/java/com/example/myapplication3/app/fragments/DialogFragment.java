package com.example.myapplication3.app.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;

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
    private DrawView drawView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_dialog, container, false);
        findViews(rootView);

        setDataToDrawView();
        return rootView;
    }

    private void findViews(View rootView) {
        rootView.findViewById(R.id.btn_share_DF).setOnClickListener(this);
        drawView = (DrawView) rootView.findViewById(R.id.dv_FD);
    }

    private void setDataToDrawView() {
        Bundle bundle = getArguments();
        GlobalModel mGlobalModel = bundle.getParcelable(Constants.TAG_GLOBAL_MODEL);
        int position = bundle.getInt(Constants.TAG_POSITION);
        Organization organization = mGlobalModel.getOrganizations().get(position);

        drawView.setName(organization.getTitle());
        drawView.setCity(Constants.getRealName(mGlobalModel.getCitiesReal(), organization.getCityId()));
        drawView.setRegion(Constants.getRealName(mGlobalModel.getRegionsReal(), organization.getRegionId()));
        drawView.setCurrencyList(organization.getCurrenciesReal());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private Bitmap getBitmapFromView(View view) {
        mBitmap = Bitmap.createBitmap( view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return mBitmap;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_share_DF) {
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bitmap.png";
            mBitmap = getBitmapFromView(drawView);
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
