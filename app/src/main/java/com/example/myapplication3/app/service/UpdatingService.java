package com.example.myapplication3.app.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.myapplication3.app.DB.DBWorker;
import com.example.myapplication3.app.MainActivity;
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
    GlobalModel mGlobalModel;
    DBWorker mDBWorker;
    NotificationManager mNotificationManager;

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
        showNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void getGlobalModel() {

        RetrofitAdapter.getInterface().getJson(new Callback<GlobalModel>() {
            @Override
            public void success(GlobalModel globalModel, Response response) {
                globalModel.deresialize();

                if ((mGlobalModel == null) || !(mGlobalModel.getDate().equals(globalModel.getDate()))) {
                    AddModelInDBAsyncTask addModelInDB = new AddModelInDBAsyncTask();
                    addModelInDB.execute(globalModel);
                    Log.d("qqq", "add new model in DB asyncTask");
                    if (!(mGlobalModel == null)) {
                        globalModel.setOrganizations(mDBWorker.getOrganizationList());
                    }
                    sendBroadcast(globalModel);
                } else {
                    Log.d("qqq", "new model and DB is same");
                    sendBroadcast(mGlobalModel);
                }

            }


            @Override

            public void failure(RetrofitError error) {
                sendBroadcast(mGlobalModel);
            }
        });
    }

    void showNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(getResources().getString(R.string.DB_is_update));
        mBuilder.setContentText(getResources().getString(R.string.DB_update_OK));
        mBuilder.setAutoCancel(true);
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(111, mBuilder.build());
    }

    class AddModelInDBAsyncTask extends AsyncTask<GlobalModel, Void, GlobalModel> {

        @Override
        protected GlobalModel doInBackground(GlobalModel... globalModels) {
            DBWorker mDBWorker = new DBWorker(getApplicationContext());
            mDBWorker.addNewGlobalModelToDB(globalModels[0]);
            GlobalModel globalModel = globalModels[0];
            globalModel.setOrganizations(mDBWorker.getOrganizationList());
            return globalModel;
        }

        @Override
        protected void onPostExecute(GlobalModel globalModel) {
            super.onPostExecute(globalModel);

            mGlobalModel = globalModel;
        }

    }

    private void sendBroadcast(GlobalModel globalModel) {
        mNotificationManager.cancelAll();

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
                Log.d("qqq", "alarm");
            }
        };
        IntentFilter intentFilter = new IntentFilter(ALARM_ACTION);
        registerReceiver(alarmReceiver, intentFilter);
    }
}
