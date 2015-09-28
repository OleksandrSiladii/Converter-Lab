package com.example.myapplication3.app.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
    private SQLiteDatabase mSQLiteDatabase;
    private ContentValues mContentValues;
    private Cursor cursor, cursor2;


    public void setNewGlobalModelToDB(Context _context, GlobalModel _globalModel) {
        mContext = _context;
        mGlobalModel = _globalModel;
        mDBHelper = new DBHelper(mContext);

        mSQLiteDatabase = mDBHelper.getWritableDatabase();

        int positionOrganization = 0, positionCurrency = 0, positionOrgTypesReal = 0,
                positionCurrenciesReal = 0, positionRegionsReal = 0, positionCitiesReal = 0;

        mContentValues = new ContentValues();
        mContentValues.put(DBHelper.SOURCE_ID, mGlobalModel.getSourceId());
        mContentValues.put(DBHelper.OLD_ID, mGlobalModel.getDate());

        mSQLiteDatabase.update(DBHelper.TABLE_NAME_GLOBAL_MADEL, mContentValues,
                DBHelper.UID + " = '" + Integer.toString(1) + "'", null);

        for (Organization organization : mGlobalModel.getOrganizations()) {
            positionOrganization += 1;
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

            mSQLiteDatabase.update(DBHelper.TABLE_NAME_ORGANIZATION, mContentValues,
                    DBHelper.UID + " = '" + Integer.toString(positionOrganization) + "'", null);

            for (Currency currency : organization.getCurrenciesReal()) {
                positionCurrency += 1;
                mContentValues = new ContentValues();
                mContentValues.put(DBHelper.TITLE, organization.getTitle());
                mContentValues.put(DBHelper.NAME_CURRENCY, currency.getName());
                mContentValues.put(DBHelper.ASK, currency.getAsk());
                mContentValues.put(DBHelper.BID, currency.getBid());

                mSQLiteDatabase.update(DBHelper.TABLE_NAME_CURRENCY, mContentValues,
                        DBHelper.UID + " = '" + Integer.toString(positionCurrency) + "'", null);
            }
        }

        for (PairedObject orgTypesReal : mGlobalModel.getOrgTypes()) {
            positionOrgTypesReal += 1;
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, orgTypesReal.getName());
            mContentValues.put(DBHelper.ID, orgTypesReal.getId());

            mSQLiteDatabase.update(DBHelper.TABLE_NAME_ORG_TYPES_REAL, mContentValues,
                    DBHelper.UID + " = '" + Integer.toString(positionOrgTypesReal) + "'", null);
        }

        for (PairedObject currenciesReal : mGlobalModel.getOrgTypes()) {
            positionCurrenciesReal += 1;
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, currenciesReal.getName());
            mContentValues.put(DBHelper.ID, currenciesReal.getId());

            mSQLiteDatabase.update(DBHelper.TABLE_NAME_CURRENCIES_REAL, mContentValues,
                    DBHelper.UID + " = '" + Integer.toString(positionCurrenciesReal) + "'", null);
        }

        for (PairedObject regionsReal : mGlobalModel.getOrgTypes()) {
            positionRegionsReal += 1;
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, regionsReal.getName());
            mContentValues.put(DBHelper.ID, regionsReal.getId());

            mSQLiteDatabase.update(DBHelper.TABLE_NAME_REGIONS_REAL, mContentValues,
                    DBHelper.UID + " = '" + Integer.toString(positionRegionsReal) + "'", null);
        }

        for (PairedObject citiesReal : mGlobalModel.getOrgTypes()) {
            positionCitiesReal += 1;
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, citiesReal.getName());
            mContentValues.put(DBHelper.ID, citiesReal.getId());

            mSQLiteDatabase.update(DBHelper.TABLE_NAME_CITIES_REAL, mContentValues,
                    DBHelper.UID + " = '" + Integer.toString(positionCitiesReal) + "'", null);
        }
        mSQLiteDatabase.close();
    }

    public GlobalModel getNewGlobalModelToDB(Context _context) {
        mContext = _context;
        mGlobalModel = new GlobalModel();
        mDBHelper = new DBHelper(mContext);

        mSQLiteDatabase = mDBHelper.getReadableDatabase();


        cursor = mSQLiteDatabase.query(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, null, null, null, null, null);

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

        cursor = mSQLiteDatabase.query(DBHelper.TABLE_NAME_ORGANIZATION, null, null, null, null, null, null);
        cursor2 = mSQLiteDatabase.query(DBHelper.TABLE_NAME_CURRENCY, null, null, null, null, null, null);

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


        cursor = mSQLiteDatabase.query(DBHelper.TABLE_NAME_ORG_TYPES_REAL, null, null, null, null, null, null);
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


        cursor = mSQLiteDatabase.query(DBHelper.TABLE_NAME_CURRENCIES_REAL, null, null, null, null, null, null);
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


        cursor = mSQLiteDatabase.query(DBHelper.TABLE_NAME_REGIONS_REAL, null, null, null, null, null, null);
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


        cursor = mSQLiteDatabase.query(DBHelper.TABLE_NAME_CITIES_REAL, null, null, null, null, null, null);
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

        return mGlobalModel;
    }

}
