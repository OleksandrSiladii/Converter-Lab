package com.example.myapplication3.app.workers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sasha on 14.10.2015.
 */
public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, UpdatingService.class);
        context.startService(myIntent);
    }
}
