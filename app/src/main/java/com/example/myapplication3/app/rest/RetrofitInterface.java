package com.example.myapplication3.app.rest;

import com.example.myapplication3.app.models.GlobalModel;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by omar on 9/22/15.
 */
public interface RetrofitInterface {



@GET(ApiConstants.JSON_PATH)
    void getJson(Callback <GlobalModel>callback);
}
