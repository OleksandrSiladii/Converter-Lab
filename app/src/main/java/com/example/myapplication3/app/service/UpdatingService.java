package com.example.myapplication3.app.service;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.myapplication3.app.Constants;
import com.example.myapplication3.app.DB.DBWorker;
import com.example.myapplication3.app.MainActivity;
import com.example.myapplication3.app.R;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.rest.RetrofitAdapter;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sasha on 30.09.2015.
 */
public class UpdatingService extends Service {

    private GlobalModel mGlobalModel;
    private NotificationManager mNotificationManager;

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
        DBWorker dbWorker = DBWorker.getInstance(getApplicationContext());

        getGlobalModel();
        showNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void getGlobalModel() {

        RetrofitAdapter.getInterface().getJson(new Callback<GlobalModel>() {
            @Override
            public void success(GlobalModel globalModel, Response response) {

                if ((mGlobalModel == null) || !(mGlobalModel.getDate().equals(globalModel.getDate()))) {
                    AddModelInDBAsyncTask addModelInDB = new AddModelInDBAsyncTask();
                    addModelInDB.execute(globalModel);
                } else {
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
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(this).setTicker(getResources().getString(R.string.DB_is_update)).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getResources().getString(R.string.DB_is_update)).setContentText(getResources().getString(R.string.DB_update_OK)).setContentIntent(pendingIntent).setAutoCancel(false).build();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, notification);
    }

    class AddModelInDBAsyncTask extends AsyncTask<GlobalModel, Void, GlobalModel> {

        @Override
        protected GlobalModel doInBackground(GlobalModel... globalModels) {
            DBWorker dbWorker = DBWorker.getInstance(getApplicationContext());
            globalModels[0].deserialize();
            dbWorker.addNewGlobalModelToDB(globalModels[0]);
            GlobalModel globalModel = globalModels[0];
            globalModel.setOrganizations(dbWorker.getOrganizationList());
            return globalModel;
        }

        @Override
        protected void onPostExecute(GlobalModel globalModel) {
            super.onPostExecute(globalModel);
            mGlobalModel = globalModel;
            sendBroadcast(globalModel);
        }
    }

    private void sendBroadcast(GlobalModel globalModel) {
        mNotificationManager.cancelAll();
        Intent intent = new Intent(Constants.TAG_BROADCAST_ACTION);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.TAG_GLOBAL_MODEL, globalModel);
        intent.putExtra(Constants.TAG_GLOBAL_MODEL, bundle);
        sendBroadcast(intent);
    }

    private void startAlarmManager() {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Constants.TAG_ALARM_ACTION);
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
        IntentFilter intentFilter = new IntentFilter(Constants.TAG_ALARM_ACTION);
        registerReceiver(alarmReceiver, intentFilter);
    }
}
