package com.example.myapplication3.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication3.app.R;
import com.example.myapplication3.app.models.GlobalModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by sasha on 01.10.2015.
 */
public class DialogFragment extends android.app.DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.scroll_view_detail_fragment, container, false);

        return rootView;
    }
}
