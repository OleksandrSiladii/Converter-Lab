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
    private List<Currency> mCurrencies;
    private Currency mCurrency;

    public DBWorker(Context context) {
        mContext = context;
    }

    public GlobalModel addNewGlobalModelToDB(GlobalModel _globalModel) {

        mDBHelper = new DBHelper(mContext);
        mGlobalModel = _globalModel;
        mDB = mDBHelper.getWritableDatabase();

//        mDB = mDBHelper.getReadableDatabase();
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

        addNewDataInGlobalTable();
        addNewDataInOrganizationTable();

        addNewDataInRealTables(DBHelper.TABLE_NAME_CITIES_REAL, mGlobalModel.getCitiesReal());
        addNewDataInRealTables(DBHelper.TABLE_NAME_REGIONS_REAL, mGlobalModel.getRegionsReal());
        addNewDataInRealTables(DBHelper.TABLE_NAME_CURRENCIES_REAL, mGlobalModel.getCurrenciesReal());
        addNewDataInRealTables(DBHelper.TABLE_NAME_ORG_TYPES_REAL, mGlobalModel.getCurrenciesReal());
    }

    private void addNewDataInRealTables(String tableName, List<PairedObject> pairedObjectList) {
        mDB.delete(tableName, null, null);

        for (PairedObject citiesReal : pairedObjectList) {
            mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, citiesReal.getName());
            mContentValues.put(DBHelper.ID, citiesReal.getId());

            mDB.insert(tableName, null, mContentValues);
        }
    }

    private void addNewDataInGlobalTable() {
        mDB.delete(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, null);

        mContentValues = new ContentValues();
        mContentValues.put(DBHelper.SOURCE_ID, mGlobalModel.getSourceId());
        mContentValues.put(DBHelper.DATA, mGlobalModel.getDate());

        mDB.insert(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, mContentValues);

        Log.d("qqq", "add data");
    }

    private void addNewDataInOrganizationTable() {
        mDB.delete(DBHelper.TABLE_NAME_ORGANIZATION, null, null);
        mDB.delete(DBHelper.TABLE_NAME_CURRENCY1, null, null);

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
                mContentValues.put(DBHelper.ID, organization.getId());
                mContentValues.put(DBHelper.NAME_CURRENCY, currency.getNameCurrency());
                mContentValues.put(DBHelper.ASK, currency.getAsk());
                mContentValues.put(DBHelper.BID, currency.getBid());
                mContentValues.put(DBHelper.PREVIOUS_ASK, currency.getPreviousAck());
                mContentValues.put(DBHelper.PREVIOUS_BID, currency.getPreviousBid());

                mDB.insert(DBHelper.TABLE_NAME_CURRENCY1, null, mContentValues);
            }
        }
        updateDBCurrency();
    }

    private void updateDBCurrency() {
        cursor2 = mDB.query(DBHelper.TABLE_NAME_CURRENCY2, null, null, null, null, null, null);
        cursor = mDB.query(DBHelper.TABLE_NAME_CURRENCY1, null, null, null, null, null, null);
        if (cursor2.getCount() < 1) {
            addCurrencyInTable2();
        } else {
//            updateCurrencyInTable2();
        }
        updateCurrencyInMainTable();
    }

    private void updateCurrencyInMainTable() {
        if (cursor.moveToFirst()) {
            do {
                if (cursor2.moveToFirst()) {
                    do {
                        if (cursor.getString(cursor.getColumnIndex(DBHelper.ID)).equals(cursor2.getString(cursor2.getColumnIndex(DBHelper.ID))) &&
                                cursor.getString(cursor.getColumnIndex(DBHelper.NAME_CURRENCY)).equals(cursor2.getString(cursor2.getColumnIndex(DBHelper.NAME_CURRENCY)))) {

                            mContentValues.put(DBHelper.ASK, cursor2.getString(cursor2.getColumnIndex(DBHelper.ASK)));
                            mContentValues.put(DBHelper.BID, cursor2.getString(cursor2.getColumnIndex(DBHelper.BID)));
                            mContentValues.put(DBHelper.PREVIOUS_ASK, cursor2.getString(cursor2.getColumnIndex(DBHelper.PREVIOUS_ASK)));
                            mContentValues.put(DBHelper.PREVIOUS_BID, cursor2.getString(cursor2.getColumnIndex(DBHelper.PREVIOUS_BID)));
                            mContentValues.put(DBHelper.ID, cursor2.getString(cursor2.getColumnIndex(DBHelper.ID)));
                            mContentValues.put(DBHelper.NAME_CURRENCY, cursor2.getString(cursor2.getColumnIndex(DBHelper.NAME_CURRENCY)));
                            int rowId = cursor.getInt(cursor.getColumnIndex(DBHelper.UID));
                            mDB.delete(DBHelper.TABLE_NAME_CURRENCY1, DBHelper.UID + "=" + rowId, null);
                            mDB.insert(DBHelper.TABLE_NAME_CURRENCY1, null, mContentValues);
                        }
                    }
                    while (cursor2.moveToNext());
                }
            } while (cursor.moveToNext());
        }
    }

    private void updateCurrencyInTable2() {
        for (Organization organization : mGlobalModel.getOrganizations()) {
            for (Currency currency : organization.getCurrenciesReal()) {
                boolean currencyIsInBase = false;
                if (cursor2.moveToFirst()) {
                    do {
                        if (currency.getId().equals(cursor2.getString(cursor2.getColumnIndex(DBHelper.ID))) &&
                                currency.getNameCurrency().equals(cursor2.getString(cursor2.getColumnIndex(DBHelper.NAME_CURRENCY)))) {
                            currencyIsInBase = true;
                            float oldBid = Float.parseFloat(cursor2.getString(cursor2.getColumnIndex(DBHelper.BID)));
                            float oldAsk = Float.parseFloat(cursor2.getString(cursor2.getColumnIndex(DBHelper.ASK)));
                            float bid = Float.parseFloat(currency.getBid());
                            float ask = Float.parseFloat(currency.getAsk());
                            mContentValues = new ContentValues();
                            if (bid != oldBid) {
                                mContentValues.put(DBHelper.PREVIOUS_BID, cursor2.getString(cursor2.getColumnIndex(DBHelper.BID)));
                                mContentValues.put(DBHelper.BID, currency.getBid());
                            } else {
                                mContentValues.put(DBHelper.PREVIOUS_BID, cursor2.getString(cursor2.getColumnIndex(DBHelper.PREVIOUS_BID)));
                                mContentValues.put(DBHelper.BID, currency.getBid());
                            }
                            if (ask != oldAsk) {
                                mContentValues.put(DBHelper.PREVIOUS_ASK, cursor2.getString(cursor2.getColumnIndex(DBHelper.ASK)));
                                mContentValues.put(DBHelper.ASK, currency.getAsk());
                            } else {
                                mContentValues.put(DBHelper.PREVIOUS_ASK, cursor2.getString(cursor2.getColumnIndex(DBHelper.PREVIOUS_ASK)));
                                mContentValues.put(DBHelper.ASK, currency.getAsk());
                            }
                            mContentValues.put(DBHelper.ID, cursor.getString(cursor.getColumnIndex(DBHelper.ID)));
                            mContentValues.put(DBHelper.NAME_CURRENCY, cursor.getString(cursor.getColumnIndex(DBHelper.NAME_CURRENCY)));
                            int rowId = cursor.getInt(cursor.getColumnIndex(DBHelper.UID));
                            mDB.delete(DBHelper.TABLE_NAME_CURRENCY2, DBHelper.UID + "=" + rowId, null);
                            mDB.insert(DBHelper.TABLE_NAME_CURRENCY2, null, mContentValues);
                        }


                    } while (cursor.moveToNext());
                }
                if (!currencyIsInBase) {
                    mContentValues = new ContentValues();
                    mContentValues.put(DBHelper.ID, organization.getId());
                    mContentValues.put(DBHelper.NAME_CURRENCY, currency.getNameCurrency());
                    mContentValues.put(DBHelper.ASK, currency.getAsk());
                    mContentValues.put(DBHelper.BID, currency.getBid());
                    mContentValues.put(DBHelper.PREVIOUS_ASK, currency.getPreviousAck());
                    mContentValues.put(DBHelper.PREVIOUS_BID, currency.getPreviousBid());

                    mDB.insert(DBHelper.TABLE_NAME_CURRENCY2, null, mContentValues);
                }
            }
        }
    }

    private void addCurrencyInTable2() {
        if (cursor.moveToFirst()) {
            do {
                mContentValues = new ContentValues();
                mContentValues.put(DBHelper.ID, cursor.getString(cursor.getColumnIndex(DBHelper.ID)));
                mContentValues.put(DBHelper.NAME_CURRENCY, cursor.getString(cursor.getColumnIndex(DBHelper.NAME_CURRENCY)));
                mContentValues.put(DBHelper.ASK, cursor.getString(cursor.getColumnIndex(DBHelper.ASK)));
                mContentValues.put(DBHelper.BID, cursor.getString(cursor.getColumnIndex(DBHelper.BID)));
                mContentValues.put(DBHelper.PREVIOUS_ASK, cursor.getString(cursor.getColumnIndex(DBHelper.PREVIOUS_ASK)));
                mContentValues.put(DBHelper.PREVIOUS_BID, cursor.getString(cursor.getColumnIndex(DBHelper.PREVIOUS_BID)));
                mDB.insert(DBHelper.TABLE_NAME_CURRENCY2, null, mContentValues);
            } while (cursor.moveToNext());
        }
    }

    private void updateDBCurrency1() {
        if (cursor.moveToFirst()) {
            do {
                for (Organization organization : mGlobalModel.getOrganizations()) {
                    for (Currency currency : organization.getCurrenciesReal()) {
                        if (cursor.getString(cursor.getColumnIndex(DBHelper.ID)).equals(organization.getId())) {
                            if (cursor.getString(cursor.getColumnIndex(DBHelper.NAME_CURRENCY)).equals(currency.getNameCurrency())) {
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
                                mDB.delete(DBHelper.TABLE_NAME_CURRENCY1, DBHelper.UID + "=" + rowId, null);
                                mDB.insert(DBHelper.TABLE_NAME_CURRENCY1, null, mContentValues);
                            }
                        }

                    }
                }
            } while (cursor.moveToNext());
        }
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
        cursor2 = mDB.query(DBHelper.TABLE_NAME_CURRENCY1, null, null, null, null, null, null);

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
}
