package com.example.myapplication3.app.rest;

import com.example.myapplication3.app.Constants;
import com.example.myapplication3.app.models.GlobalModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import java.util.concurrent.TimeUnit;

public abstract class RetrofitAdapter {

    private static RetrofitInterface retrofitInterface;
    private static RestAdapter restAdapter;

    public static RetrofitInterface getInterface() {

        if (retrofitInterface == null) {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            client.setReadTimeout(10, TimeUnit.SECONDS);

            restAdapter = new RestAdapter.Builder().setEndpoint(Constants.SERVER_URL).setClient(new OkClient
                    (client)).setConverter(new GsonConverter(new GsonBuilder().registerTypeAdapter(GlobalModel.class, new CustomJsonDeserializer()).create())).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            retrofitInterface = restAdapter.create(RetrofitInterface.class);
        }
        return retrofitInterface;
    }

}