package com.example.myapplication3.app.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication3.app.R;
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
    private SQLiteDatabase mDB, mDB2, mDB3;
    private ContentValues mContentValues;
    private Cursor cursor, cursor2;
    private List<Currency> mCurrencies;
    private Currency mCurrency;

    public DBWorker(Context context){
        mContext = context;
    }

    public GlobalModel addNewGlobalModelToDB(GlobalModel _globalModel) {

        mDBHelper = new DBHelper(mContext);
        mGlobalModel = _globalModel;

        mDB = mDBHelper.getReadableDatabase();
        cursor = mDB.query(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, null, null, null, null, null);

        if (cursor.getCount() < 1) {
            addGlobalModelToDB(_globalModel);
            Log.d("qqq", "create new DB : " + cursor.getCount());
        } else {
            Log.d("qqq", "cursor : " + cursor.getCount());
            cursor.moveToFirst();
            if (!cursor.getString(cursor.getColumnIndex(DBHelper.DATA)).equals(_globalModel.getDate())) {
//                Toast.makeText(mContext, R.string.ok_load, Toast.LENGTH_SHORT).show();

                Log.d("qqq", "add new data to DB");
                Log.d("qqq", "data in old DB: " + cursor.getString(cursor.getColumnIndex(DBHelper.DATA)));
                Log.d("qqq", "data in model: " + _globalModel.getDate());

                addGlobalModelToDB(_globalModel);
            } else {
                Log.d("qqq", "DB and Model is same");
            }
        }
        mDB.close();

        return mGlobalModel;
    }

    public void addGlobalModelToDB(GlobalModel _globalModel) {

        mGlobalModel = _globalModel;

//        mDB3 = mDBHelper.getReadableDatabase();
//        Cursor cursor3 = mDB3.query(DBHelper.TABLE_NAME_CURRENCY, null, null, null, null, null, null);
        mDB = mDBHelper.getWritableDatabase();
        mDB.delete(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, null);
        mDB.delete(DBHelper.TABLE_NAME_ORGANIZATION, null, null);
        mDB.delete(DBHelper.TABLE_NAME_ORG_TYPES_REAL, null, null);
        mDB.delete(DBHelper.TABLE_NAME_CURRENCIES_REAL, null, null);
        mDB.delete(DBHelper.TABLE_NAME_REGIONS_REAL, null, null);
        mDB.delete(DBHelper.TABLE_NAME_CITIES_REAL, null, null);
        mDB.delete(DBHelper.TABLE_NAME_CURRENCY, null, null);

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

//            if (cursor3.getCount() < 1) {
                for (Currency currency : organization.getCurrenciesReal()) {

                    mContentValues = new ContentValues();
                    mContentValues.put(DBHelper.ID, organization.getId());
                    mContentValues.put(DBHelper.NAME_CURRENCY, currency.getNameCurrency());
                    mContentValues.put(DBHelper.ASK, currency.getAsk());
                    mContentValues.put(DBHelper.BID, currency.getBid());
                    mContentValues.put(DBHelper.PREVIOUS_ASK, currency.getPreviousAck());
                    mContentValues.put(DBHelper.PREVIOUS_BID, currency.getPreviousBid());

                    mDB.insert(DBHelper.TABLE_NAME_CURRENCY, null, mContentValues);
                }
//            }
        }
//        if(cursor3.getCount()>0) updateDBCurrency(mGlobalModel);
//        mDB3.close();

        mDB = mDBHelper.getWritableDatabase();

        for (PairedObject orgTypesReal : mGlobalModel.getOrgTypes()) {
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, orgTypesReal.getName());
            mContentValues.put(DBHelper.ID, orgTypesReal.getId());

            mDB.insert(DBHelper.TABLE_NAME_ORG_TYPES_REAL, null, mContentValues);
        }

        for (PairedObject currenciesReal : mGlobalModel.getCurrenciesReal()) {
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, currenciesReal.getName());
            mContentValues.put(DBHelper.ID, currenciesReal.getId());

            mDB.insert(DBHelper.TABLE_NAME_CURRENCIES_REAL, null, mContentValues);
        }

        for (PairedObject regionsReal : mGlobalModel.getRegionsReal()) {
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, regionsReal.getName());
            mContentValues.put(DBHelper.ID, regionsReal.getId());

            mDB.insert(DBHelper.TABLE_NAME_REGIONS_REAL, null, mContentValues);
        }

        for (PairedObject citiesReal : mGlobalModel.getCitiesReal()) {
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, citiesReal.getName());
            mContentValues.put(DBHelper.ID, citiesReal.getId());

            mDB.insert(DBHelper.TABLE_NAME_CITIES_REAL, null, mContentValues);
        }
        mDB.close();
//        showLogs();
    }

    public GlobalModel getGlobalModelFromDB() {

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

                    mCurrencies = new ArrayList<Currency>();
                    if (cursor2.moveToFirst()) {
                        try {
                            do {
                                String id = cursor2.getString(cursor2.getColumnIndex(DBHelper.ID));

                                if (id.equals(mOrganization.getId())) {
                                    mCurrency = new Currency();
                                    mCurrency.setNameCurrency(cursor2.getString(cursor2.getColumnIndex(DBHelper.NAME)));
                                    mCurrency.setAsk(cursor2.getString(cursor2.getColumnIndex(DBHelper.ASK)));
                                    mCurrency.setBid(cursor2.getString(cursor2.getColumnIndex(DBHelper.BID)));
                                    mCurrency.setPreviousAck(cursor2.getString(cursor2.getColumnIndex(DBHelper.PREVIOUS_ASK)));
                                    mCurrency.setPreviousBid(cursor2.getString(cursor2.getColumnIndex(DBHelper.PREVIOUS_BID)));
                                    mCurrencies.add(mCurrency);
//                                    Log.d("qqq","ASC: "+ mCurrency.getAsk());
//                                    Log.d("qqq","OLD ASC: "+ mCurrency.getPreviousAck());
                                }

                            } while (cursor2.moveToNext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mOrganization.setCurrenciesReal(mCurrencies);

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

    private void updateDBCurrency(GlobalModel newGlobalModel) {

        SQLiteDatabase mDBRead = mDBHelper.getReadableDatabase();
        SQLiteDatabase mDBWrite = mDBHelper.getWritableDatabase();
        cursor = mDBRead.query(DBHelper.TABLE_NAME_CURRENCY, null, null, null, null, null, null);
        for (Organization organization : newGlobalModel.getOrganizations()) {
            String organizationId = organization.getId();
            List<Currency> currencies = organization.getCurrenciesReal();
            if (cursor.moveToFirst()) {
                do {
                    if (organizationId.equals(cursor.getString(cursor.getColumnIndex(DBHelper.ID)))) {
                        for (Currency currency : currencies) {
                            String nameCurrency = cursor.getString(cursor.getColumnIndex(DBHelper.NAME_CURRENCY));
                            if (currency.getNameCurrency().equals(nameCurrency)) {

                                float oldBid = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DBHelper.BID)));
                                float oldAsk = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DBHelper.ASK)));
                                float bid = Float.parseFloat(currency.getBid());
                                float ask = Float.parseFloat(currency.getAsk());
                                mContentValues = new ContentValues();
                                if (bid != oldBid) {
                                    mContentValues.put(DBHelper.BID, currency.getBid());
                                    mContentValues.put(DBHelper.PREVIOUS_BID, cursor.getString(cursor.getColumnIndex(DBHelper.BID)));
                                } else {
                                    mContentValues.put(DBHelper.BID, currency.getBid());
                                    mContentValues.put(DBHelper.PREVIOUS_BID, cursor.getString(cursor.getColumnIndex(DBHelper.PREVIOUS_BID)));
                                }
                                if (ask != oldAsk) {
                                    mContentValues.put(DBHelper.ASK, currency.getAsk());
                                    mContentValues.put(DBHelper.PREVIOUS_ASK, cursor.getString(cursor.getColumnIndex(DBHelper.ASK)));
                                } else {
                                    mContentValues.put(DBHelper.ASK, currency.getAsk());
                                    mContentValues.put(DBHelper.PREVIOUS_ASK, cursor.getString(cursor.getColumnIndex(DBHelper.PREVIOUS_ASK)));
                                }
                                mContentValues.put(DBHelper.ID, cursor.getString(cursor.getColumnIndex(DBHelper.ID)));
                                mContentValues.put(DBHelper.NAME_CURRENCY, cursor.getString(cursor.getColumnIndex(DBHelper.NAME_CURRENCY)));

                                int rowId = cursor.getInt(cursor.getColumnIndex(DBHelper.UID));
                                mDBWrite.delete(DBHelper.TABLE_NAME_CURRENCY, DBHelper.UID + "=" + rowId, null);
                                mDBWrite.insert(DBHelper.TABLE_NAME_CURRENCY, null, mContentValues);

                            }
                        }
                    }
                } while (cursor.moveToNext());
            }

        }
        mDBWrite.close();
        mDBRead.close();
    }

}
