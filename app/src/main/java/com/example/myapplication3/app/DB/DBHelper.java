package com.example.myapplication3.app.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sasha on 28.09.2015.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "global_model";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_ORGANIZATION = "organization";
    public static final String TABLE_NAME_CURRENCY1 = "currency1";
    public static final String TABLE_NAME_CURRENCY2 = "currency2";
    public static final String TABLE_NAME_ORG_TYPES_REAL = "orgTypesReal";
    public static final String TABLE_NAME_CURRENCIES_REAL = "currenciesReal";
    public static final String TABLE_NAME_REGIONS_REAL = "regionsReal";
    public static final String TABLE_NAME_CITIES_REAL = "citiesReal";
    public static final String TABLE_NAME_GLOBAL_MADEL = "globalModel";

    public static final String UID = "_id";
    public static final String ID = "id";
    public static final String OLD_ID = "old_id";
    public static final String ORG_TYPE = "org_TYPE";
    public static final String TITLE = "title";
    public static final String REGION_ID = "region_id";
    public static final String CITY_ID = "city_id";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String LINK = "link";
    public static final String NAME_CURRENCY = "name";
    public static final String ASK = "ask";
    public static final String PREVIOUS_ASK = "previous_ask";
    public static final String BID = "bid";
    public static final String PREVIOUS_BID = "previous_bid";
    public static final String NAME = "name";
    public static final String SOURCE_ID = "sourceId";
    public static final String DATA = "data";


    private static final String SQL_CREATE_ENTRIES_GLOBAL_MADEL = "CREATE TABLE "
            + TABLE_NAME_GLOBAL_MADEL + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SOURCE_ID + " TEXT,"
            + DATA + " TEXT);";

    private static final String SQL_CREATE_ENTRIES_ORGANIZATION = "CREATE TABLE "
            + TABLE_NAME_ORGANIZATION + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ID + " TEXT,"
            + OLD_ID + " INTEGER,"
            + ORG_TYPE + " INTEGER,"
            + TITLE + " TEXT,"
            + REGION_ID + " TEXT,"
            + CITY_ID + " TEXT,"
            + PHONE + " TEXT,"
            + ADDRESS + " TEXT,"
            + LINK + " TEXT);";

    private static final String SQL_CREATE_ENTRIES_CURRENCY1 = "CREATE TABLE "
            + TABLE_NAME_CURRENCY1 + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ID + " TEXT,"
            + NAME_CURRENCY + " TEXT,"
            + ASK + " TEXT,"
            + PREVIOUS_ASK + " TEXT,"
            + PREVIOUS_BID + " TEXT,"
            + BID + " TEXT);";

    private static final String SQL_CREATE_ENTRIES_CURRENCY2 = "CREATE TABLE "
            + TABLE_NAME_CURRENCY2 + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ID + " TEXT,"
            + NAME_CURRENCY + " TEXT,"
            + ASK + " TEXT,"
            + PREVIOUS_ASK + " TEXT,"
            + PREVIOUS_BID + " TEXT,"
            + BID + " TEXT);";

    private static final String SQL_CREATE_ENTRIES_ORG_TYPES_REAL = "CREATE TABLE "
            + TABLE_NAME_ORG_TYPES_REAL + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME + " TEXT,"
            + ID + " TEXT);";

    private static final String SQL_CREATE_ENTRIES_CURRENCIES_REAL = "CREATE TABLE "
            + TABLE_NAME_CURRENCIES_REAL + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME + " TEXT,"
            + ID + " TEXT);";

    private static final String SQL_CREATE_ENTRIES_REGION_REAL = "CREATE TABLE "
            + TABLE_NAME_REGIONS_REAL + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME + " TEXT,"
            + ID + " TEXT);";

    private static final String SQL_CREATE_ENTRIES_CITIES_REAL = "CREATE TABLE "
            + TABLE_NAME_CITIES_REAL + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME + " TEXT,"
            + ID + " TEXT);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_GLOBAL_MADEL);
        db.execSQL(SQL_CREATE_ENTRIES_ORGANIZATION);
        db.execSQL(SQL_CREATE_ENTRIES_CURRENCY1);
        db.execSQL(SQL_CREATE_ENTRIES_CURRENCY2);
        db.execSQL(SQL_CREATE_ENTRIES_ORG_TYPES_REAL);
        db.execSQL(SQL_CREATE_ENTRIES_CURRENCIES_REAL);
        db.execSQL(SQL_CREATE_ENTRIES_REGION_REAL);
        db.execSQL(SQL_CREATE_ENTRIES_CITIES_REAL);
        Log.d("qqq", "create all tables");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}