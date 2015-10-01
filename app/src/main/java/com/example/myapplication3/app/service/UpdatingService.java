package com.example.myapplication3.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication3.app.DB.DBWorker;
import com.example.myapplication3.app.R;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.rest.RetrofitAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sasha on 30.09.2015.
 */
public class UpdatingService extends Service {

    public final static String BROADCAST_ACTION = "com.example.myapplication3.app.service.BROADCAST_ACTION";
    public final static String ALARM_ACTION = "com.example.myapplication3.app.service.ALARM_ACTION";
    DBWorker mDBWorker;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        regBroadcastReceiver();
        startAlarmManager();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("qqq", "MyService onStartCommand");

        mDBWorker = new DBWorker(getApplicationContext());
        getGlobalModel();

        return super.onStartCommand(intent, flags, startId);
    }

    private void getGlobalModel() {

        RetrofitAdapter.getInterface().getJson(new Callback<GlobalModel>() {
            @Override
            public void success(GlobalModel globalModel, Response response) {
                globalModel.deresialize();

                AddModelInDB addModelInDB = new AddModelInDB();
                addModelInDB.execute(globalModel);

                sendBroadcast(globalModel);

                Log.d("qqq", "add new model in DB asyncTask");
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void sendBroadcast(GlobalModel globalModel) {

        Intent intent = new Intent(UpdatingService.BROADCAST_ACTION);
        Log.d("qqq", "send Broadcast from service with model");

        Bundle bundle = new Bundle();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(globalModel);
        bundle.putString(GlobalModel.TAG_GLOBAL_MODEL, json);
        intent.putExtra(GlobalModel.TAG_GLOBAL_MODEL, bundle);

        sendBroadcast(intent);
    }

    private void startAlarmManager() {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1800000, pendingIntent);
    }

    private void regBroadcastReceiver() {
        BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getGlobalModel();
            }
        };
        IntentFilter intentFilter = new IntentFilter(ALARM_ACTION);
        registerReceiver(alarmReceiver, intentFilter);
    }

    class AddModelInDB extends AsyncTask<GlobalModel, Void, Void> {

        @Override
        protected Void doInBackground(GlobalModel... globalModels) {
            DBWorker mDBWorker = new DBWorker(getApplicationContext());
            mDBWorker.addNewGlobalModelToDB(globalModels[0]);
            return null;
        }
    }
}
