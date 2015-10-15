package com.example.myapplication3.app.workers;

import com.example.myapplication3.app.models.PairedObject;

import java.util.List;

/**
 * Created by sasha on 12.10.2015.
 */
public class Constants {
    public static final String TAG_LOG = "myLog";
    public static final String TAG_GLOBAL_MODEL = "TAG_GLOBAL_MODEL";
    public static final String TAG_POSITION = "TAG_POSITION";
    public static final String TAG_CITY = "CITY";
    public static final String TAG_ADDRESS = "ADDRESS";
    public static final String TAG_REGION = "REGION";
    public static final String TAG_LOCATION_NAME = "locationName";
    public final static String TAG_FRAGMENT = "frag";
    public final static String TAG_BROADCAST_ACTION = "com.example.myapplication3.app.service.BROADCAST_ACTION";
    public final static String TAG_ALARM_ACTION = "com.example.myapplication3.app.service.ALARM_ACTION";
    public final static int LOADER_ID_1 = 1;
    public final static int LOADER_ID_2 = 2;

    public static String getRealName(List<PairedObject> pairedObjectList, String id) {
        for (PairedObject item : pairedObjectList) {
            if (item.getId().equals(id)) {
                String rez = item.getName();
                rez = rez.replaceAll("\"", "");
                return rez;
            }
        }
        return id;
    }
}
