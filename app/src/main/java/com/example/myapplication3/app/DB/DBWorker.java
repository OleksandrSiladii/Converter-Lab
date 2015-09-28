package com.example.myapplication3.app.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication3.app.models.Currency;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.models.PairedObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sasha on 28.09.2015.
 */
public class DBWorker {

    private Context mContext;
    private DBHelper mDBHelper;
    private GlobalModel mGlobalModel;
    private Organization mOrganization;
    private SQLiteDatabase mDB;
    private ContentValues mContentValues;
    private Cursor cursor, cursor2;

    public GlobalModel addNewGlobalModelToDB(Context _context, GlobalModel _globalModel) {

        mDBHelper = new DBHelper(_context);

        mDB = mDBHelper.getReadableDatabase();
        cursor = mDB.query(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, null, null, null, null, null);
//    String data = cursor.getString(cursor.getColumnIndex(DBHelper.DATA));

        if (cursor.getCount() < 1) {
            addGlobalModelToDB(_context, _globalModel);
            Log.d("qqq", "create new DB : " + cursor.getCount());
        } else {
            if (!cursor.getString(cursor.getColumnIndex(DBHelper.DATA)).equals(_globalModel.getDate())) {
                Log.d("qqq", "add new data to DB");

                mDB = mDBHelper.getWritableDatabase();
                mDB.delete(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, null);
                mDB.delete(DBHelper.TABLE_NAME_ORGANIZATION, null, null);
                mDB.delete(DBHelper.TABLE_NAME_CURRENCY, null, null);
                mDB.delete(DBHelper.TABLE_NAME_ORG_TYPES_REAL, null, null);
                mDB.delete(DBHelper.TABLE_NAME_CURRENCIES_REAL, null, null);
                mDB.delete(DBHelper.TABLE_NAME_REGIONS_REAL, null, null);
                mDB.delete(DBHelper.TABLE_NAME_CITIES_REAL, null, null);

                addGlobalModelToDB(_context, _globalModel);
            }
        }
        mDB.close();
        return _globalModel;
    }

    public void addGlobalModelToDB(Context _context, GlobalModel _globalModel) {
        mContext = _context;
        mGlobalModel = _globalModel;

        mDB = mDBHelper.getWritableDatabase();

        mContentValues = new ContentValues();
        mContentValues.put(DBHelper.SOURCE_ID, mGlobalModel.getSourceId());
        mContentValues.put(DBHelper.DATA, mGlobalModel.getDate());

        mDB.insert(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, mContentValues);

        Log.d("qqq", "add data");

        for (Organization organization : mGlobalModel.getOrganizations()) {
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.ID, organization.getId());
            mContentValues.put(DBHelper.OLD_ID, organization.getOldId());
            mContentValues.put(DBHelper.ORG_TYPE, organization.getOrgType());
            mContentValues.put(DBHelper.TITLE, organization.getTitle());
            mContentValues.put(DBHelper.REGION_ID, organization.getRegionId());
            mContentValues.put(DBHelper.CITY_ID, organization.getCityId());
            mContentValues.put(DBHelper.PHONE, organization.getPhone());
            mContentValues.put(DBHelper.ADDRESS, organization.getAddress());
            mContentValues.put(DBHelper.LINK, organization.getLink());

            mDB.insert(DBHelper.TABLE_NAME_ORGANIZATION, null, mContentValues);

            for (Currency currency : organization.getCurrenciesReal()) {

                mContentValues = new ContentValues();
                mContentValues.put(DBHelper.TITLE, organization.getTitle());
                mContentValues.put(DBHelper.NAME_CURRENCY, currency.getName());
                mContentValues.put(DBHelper.ASK, currency.getAsk());
                mContentValues.put(DBHelper.BID, currency.getBid());

                mDB.insert(DBHelper.TABLE_NAME_CURRENCY, null, mContentValues);
            }
        }

        for (PairedObject orgTypesReal : mGlobalModel.getOrgTypes()) {
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, orgTypesReal.getName());
            mContentValues.put(DBHelper.ID, orgTypesReal.getId());

            mDB.insert(DBHelper.TABLE_NAME_ORG_TYPES_REAL, null, mContentValues);
        }

        for (PairedObject currenciesReal : mGlobalModel.getOrgTypes()) {
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, currenciesReal.getName());
            mContentValues.put(DBHelper.ID, currenciesReal.getId());

            mDB.insert(DBHelper.TABLE_NAME_CURRENCIES_REAL, null, mContentValues);
        }

        for (PairedObject regionsReal : mGlobalModel.getOrgTypes()) {
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, regionsReal.getName());
            mContentValues.put(DBHelper.ID, regionsReal.getId());

            mDB.insert(DBHelper.TABLE_NAME_REGIONS_REAL, null, mContentValues);
        }

        for (PairedObject citiesReal : mGlobalModel.getOrgTypes()) {
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, citiesReal.getName());
            mContentValues.put(DBHelper.ID, citiesReal.getId());

            mDB.insert(DBHelper.TABLE_NAME_CITIES_REAL, null, mContentValues);
        }
        mDB.close();
    }

    public GlobalModel getGlobalModelFromDB(Context _context) {
        mContext = _context;
        mGlobalModel = new GlobalModel();
        mDBHelper = new DBHelper(mContext);

        mDB = mDBHelper.getReadableDatabase();


        cursor = mDB.query(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            try {
                do {
                    mGlobalModel.setSourceId(cursor.getString(cursor.getColumnIndex(DBHelper.SOURCE_ID)));
                    mGlobalModel.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.DATA)));
                } while (cursor.moveToNext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cursor = mDB.query(DBHelper.TABLE_NAME_ORGANIZATION, null, null, null, null, null, null);
        cursor2 = mDB.query(DBHelper.TABLE_NAME_CURRENCY, null, null, null, null, null, null);

        List<Organization> organizations = new ArrayList<Organization>();

        if (cursor.moveToFirst()) {
            try {
                do {
                    mOrganization = new Organization();
                    mOrganization.setId(cursor.getString(cursor.getColumnIndex(DBHelper.ID)));
                    mOrganization.setOldId(cursor.getInt(cursor.getColumnIndex(DBHelper.OLD_ID)));
                    mOrganization.setOrgType(cursor.getInt(cursor.getColumnIndex(DBHelper.ORG_TYPE)));
                    mOrganization.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.TITLE)));
                    mOrganization.setRegionId(cursor.getString(cursor.getColumnIndex(DBHelper.REGION_ID)));
                    mOrganization.setCityId(cursor.getString(cursor.getColumnIndex(DBHelper.CITY_ID)));
                    mOrganization.setPhone(cursor.getString(cursor.getColumnIndex(DBHelper.PHONE)));
                    mOrganization.setLink(cursor.getString(cursor.getColumnIndex(DBHelper.LINK)));
                    mOrganization.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.ADDRESS)));

                    List<Currency> currencies = new ArrayList<Currency>();
                    if (cursor2.moveToFirst()) {
                        try {
                            do {
                                String title = cursor2.getString(cursor2.getColumnIndex(DBHelper.TITLE));

                                if (title.equals(mOrganization.getTitle())) {
                                    Currency currency = new Currency();
                                    currency.setAsk(cursor2.getString(cursor2.getColumnIndex(DBHelper.ASK)));
                                    currency.setBid(cursor2.getString(cursor2.getColumnIndex(DBHelper.BID)));
                                    currencies.add(currency);
                                }

                            } while (cursor.moveToNext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mOrganization.setCurrenciesReal(currencies);

                    organizations.add(mOrganization);

                } while (cursor.moveToNext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mGlobalModel.setOrganizations(organizations);


        cursor = mDB.query(DBHelper.TABLE_NAME_ORG_TYPES_REAL, null, null, null, null, null, null);
        List<PairedObject> orgTypeReal = new ArrayList<PairedObject>();

        if (cursor.moveToFirst()) {
            try {
                do {
                    PairedObject pairedObject = new PairedObject();
                    pairedObject.setId(cursor.getString(cursor.getColumnIndex(DBHelper.ID)));
                    pairedObject.setName(cursor.getString(cursor.getColumnIndex(DBHelper.NAME)));

                    orgTypeReal.add(pairedObject);

                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mGlobalModel.setOrgTypesReal(orgTypeReal);


        cursor = mDB.query(DBHelper.TABLE_NAME_CURRENCIES_REAL, null, null, null, null, null, null);
        List<PairedObject> currenciesReal = new ArrayList<PairedObject>();

        if (cursor.moveToFirst()) {
            try {
                do {
                    PairedObject pairedObject = new PairedObject();
                    pairedObject.setId(cursor.getString(cursor.getColumnIndex(DBHelper.ID)));
                    pairedObject.setName(cursor.getString(cursor.getColumnIndex(DBHelper.NAME)));

                    currenciesReal.add(pairedObject);

                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mGlobalModel.setCurrenciesReal(currenciesReal);


        cursor = mDB.query(DBHelper.TABLE_NAME_REGIONS_REAL, null, null, null, null, null, null);
        List<PairedObject> regionsReal = new ArrayList<PairedObject>();

        if (cursor.moveToFirst()) {
            try {
                do {
                    PairedObject pairedObject = new PairedObject();
                    pairedObject.setId(cursor.getString(cursor.getColumnIndex(DBHelper.ID)));
                    pairedObject.setName(cursor.getString(cursor.getColumnIndex(DBHelper.NAME)));

                    regionsReal.add(pairedObject);

                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mGlobalModel.setRegionsReal(regionsReal);


        cursor = mDB.query(DBHelper.TABLE_NAME_CITIES_REAL, null, null, null, null, null, null);
        List<PairedObject> citiesReal = new ArrayList<PairedObject>();

        if (cursor.moveToFirst()) {
            try {
                do {
                    PairedObject pairedObject = new PairedObject();
                    pairedObject.setId(cursor.getString(cursor.getColumnIndex(DBHelper.ID)));
                    pairedObject.setName(cursor.getString(cursor.getColumnIndex(DBHelper.NAME)));

                    citiesReal.add(pairedObject);

                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mGlobalModel.setCitiesReal(citiesReal);
        mDB.close();
        return mGlobalModel;
    }


}
