package com.example.myapplication3.app.rest;

import com.example.myapplication3.app.Constants;
import com.example.myapplication3.app.models.GlobalModel;

import retrofit.Callback;
import retrofit.http.GET;

public interface RetrofitInterface {

    @GET(Constants.JSON_PATH)
    void getJson(Callback<GlobalModel> callback);
}
