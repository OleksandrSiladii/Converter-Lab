package com.example.myapplication3.app.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication3.app.Constants;
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

    private List<Currency> mCurrencies;
    private Currency mCurrency;

    public DBWorker(Context context) {
        mContext = context;
    }

    public GlobalModel addNewGlobalModelToDB(GlobalModel _globalModel) {

        mDBHelper = new DBHelper(mContext);
        mGlobalModel = _globalModel;
        mDB = mDBHelper.getWritableDatabase();

        Cursor cursor = mDB.query(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, null, null, null, null, null);

        if (cursor.getCount() < 1) {
            addGlobalModelToDB(_globalModel);
            Log.d(Constants.TAG_LOG, "create new DB  ");
        } else {
            Log.d(Constants.TAG_LOG, "cursor : " + cursor.getCount());
            cursor.moveToFirst();
            if (!cursor.getString(cursor.getColumnIndex(DBHelper.DATA)).equals(_globalModel.getDate())) {
                Log.d(Constants.TAG_LOG, "add new data to DB");
                Log.d(Constants.TAG_LOG, "data in old DB: " + cursor.getString(cursor.getColumnIndex(DBHelper.DATA)));
                Log.d(Constants.TAG_LOG, "data in model: " + _globalModel.getDate());
                addGlobalModelToDB(_globalModel);
            } else {
                Log.d(Constants.TAG_LOG, "DB and Model is same");
            }
        }
        cursor.close();
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
            ContentValues mContentValues = new ContentValues();
            mContentValues.put(DBHelper.NAME, citiesReal.getName());
            mContentValues.put(DBHelper.ID, citiesReal.getId());
            mDB.insert(tableName, null, mContentValues);
        }
    }

    private void addNewDataInGlobalTable() {
        mDB.delete(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, null);
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(DBHelper.SOURCE_ID, mGlobalModel.getSourceId());
        mContentValues.put(DBHelper.DATA, mGlobalModel.getDate());

        mDB.insert(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, mContentValues);
        Log.d(Constants.TAG_LOG, "add data");
    }

    private void addNewDataInOrganizationTable() {
        mDB.delete(DBHelper.TABLE_NAME_ORGANIZATION, null, null);
        mDB.delete(DBHelper.TABLE_NAME_CURRENCY1, null, null);

        for (Organization organization : mGlobalModel.getOrganizations()) {
            ContentValues mContentValues = new ContentValues();
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

                ContentValues mContentValues2 = new ContentValues();
                mContentValues2.put(DBHelper.ID, organization.getId());
                mContentValues2.put(DBHelper.NAME_CURRENCY, currency.getNameCurrency());
                mContentValues2.put(DBHelper.ASK, currency.getAsk());
                mContentValues2.put(DBHelper.BID, currency.getBid());
                mContentValues2.put(DBHelper.PREVIOUS_ASK, currency.getPreviousAck());
                mContentValues2.put(DBHelper.PREVIOUS_BID, currency.getPreviousBid());

                mDB.insert(DBHelper.TABLE_NAME_CURRENCY1, null, mContentValues2);
            }
        }
        updateDBCurrency();
    }

    private void updateDBCurrency() {
        Cursor cursor2 = mDB.query(DBHelper.TABLE_NAME_CURRENCY2, null, null, null, null, null, null);
        if (cursor2.getCount() < 1) {
            Log.d(Constants.TAG_LOG, "addCurrencyInTable2");
            addCurrencyInTable2();
        } else {
            Log.d(Constants.TAG_LOG, "updateCurrencyInTable2");
            updateCurrencyInTable2();
        }
        updateCurrencyInMainTable();
        cursor2.close();
        Log.d(Constants.TAG_LOG, "updateCurrencyInMainTable");
    }

    private void updateCurrencyInMainTable() {
        Cursor cursor2 = mDB.query(DBHelper.TABLE_NAME_CURRENCY2, null, null, null, null, null, null);
        Cursor cursor = mDB.query(DBHelper.TABLE_NAME_CURRENCY1, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor2.moveToFirst()) {
                    do {
                        if (cursor.getString(cursor.getColumnIndex(DBHelper.ID)).equals(cursor2.getString(cursor2.getColumnIndex(DBHelper.ID))) &&
                                cursor.getString(cursor.getColumnIndex(DBHelper.NAME_CURRENCY)).equals(cursor2.getString(cursor2.getColumnIndex(DBHelper.NAME_CURRENCY)))) {
                            ContentValues mContentValues = new ContentValues();
                            mContentValues.put(DBHelper.ASK, cursor2.getString(cursor2.getColumnIndex(DBHelper.ASK)));
                            mContentValues.put(DBHelper.BID, cursor2.getString(cursor2.getColumnIndex(DBHelper.BID)));
                            mContentValues.put(DBHelper.PREVIOUS_ASK, cursor2.getString(cursor2.getColumnIndex(DBHelper.PREVIOUS_ASK)));
                            mContentValues.put(DBHelper.PREVIOUS_BID, cursor2.getString(cursor2.getColumnIndex(DBHelper.PREVIOUS_BID)));
                            mContentValues.put(DBHelper.ID, cursor2.getString(cursor2.getColumnIndex(DBHelper.ID)));
                            mContentValues.put(DBHelper.NAME_CURRENCY, cursor2.getString(cursor2.getColumnIndex(DBHelper.NAME_CURRENCY)));
                            int rowId = cursor.getInt(cursor.getColumnIndex(DBHelper.UID));
                            mDB.delete(DBHelper.TABLE_NAME_CURRENCY1, DBHelper.UID + " = " + rowId, null);
                            mDB.insert(DBHelper.TABLE_NAME_CURRENCY1, null, mContentValues);
                        }
                    }
                    while (cursor2.moveToNext());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        cursor2.close();
    }

    private void updateCurrencyInTable2() {
        Cursor cursor2 = mDB.query(DBHelper.TABLE_NAME_CURRENCY2, null, null, null, null, null, null);
        for (Organization organization : mGlobalModel.getOrganizations()) {
            for (Currency currency : organization.getCurrenciesReal()) {
                boolean currencyIsInBase = false;
                if (cursor2.moveToFirst()) {
                    do {
                        String currencyIdMod = organization.getId();
                        String currencyIdDB = cursor2.getString(cursor2.getColumnIndex(DBHelper.ID));
                        String currencyNameMod = currency.getNameCurrency();
                        String currencyNameDB = cursor2.getString(cursor2.getColumnIndex(DBHelper.NAME_CURRENCY));

                        if ((currencyIdMod.equals(currencyIdDB)) &&
                                (currencyNameMod.equals(currencyNameDB))) {
                            currencyIsInBase = true;
                            float oldBid = Float.parseFloat(cursor2.getString(cursor2.getColumnIndex(DBHelper.BID)));
                            float oldAsk = Float.parseFloat(cursor2.getString(cursor2.getColumnIndex(DBHelper.ASK)));
                            float bid = Float.parseFloat(currency.getBid());
                            float ask = Float.parseFloat(currency.getAsk());
                            ContentValues mContentValues = new ContentValues();
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
                            mContentValues.put(DBHelper.ID, currencyIdMod);
                            mContentValues.put(DBHelper.NAME_CURRENCY, currencyNameMod);
                            int rowId = cursor2.getInt(cursor2.getColumnIndex(DBHelper.UID));
                            mDB.delete(DBHelper.TABLE_NAME_CURRENCY2, DBHelper.UID + " = " + rowId, null);
                            mDB.insert(DBHelper.TABLE_NAME_CURRENCY2, null, mContentValues);
                        }
                    } while (cursor2.moveToNext());
                }
                if (!currencyIsInBase) {
                    ContentValues mContentValues = new ContentValues();
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
        cursor2.close();
    }

    private void addCurrencyInTable2() {
        Cursor cursor = mDB.query(DBHelper.TABLE_NAME_CURRENCY1, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues mContentValues = new ContentValues();
                mContentValues.put(DBHelper.ID, cursor.getString(cursor.getColumnIndex(DBHelper.ID)));
                mContentValues.put(DBHelper.NAME_CURRENCY, cursor.getString(cursor.getColumnIndex(DBHelper.NAME_CURRENCY)));
                mContentValues.put(DBHelper.ASK, cursor.getString(cursor.getColumnIndex(DBHelper.ASK)));
                mContentValues.put(DBHelper.BID, cursor.getString(cursor.getColumnIndex(DBHelper.BID)));
                mContentValues.put(DBHelper.PREVIOUS_ASK, cursor.getString(cursor.getColumnIndex(DBHelper.PREVIOUS_ASK)));
                mContentValues.put(DBHelper.PREVIOUS_BID, cursor.getString(cursor.getColumnIndex(DBHelper.PREVIOUS_BID)));
                mDB.insert(DBHelper.TABLE_NAME_CURRENCY2, null, mContentValues);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public GlobalModel getGlobalModelFromDB() {

        mGlobalModel = new GlobalModel();
        mDBHelper = new DBHelper(mContext);

        mDB = mDBHelper.getReadableDatabase();
        Cursor cursor = mDB.query(DBHelper.TABLE_NAME_GLOBAL_MADEL, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                mGlobalModel.setSourceId(cursor.getString(cursor.getColumnIndex(DBHelper.SOURCE_ID)));
                mGlobalModel.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.DATA)));
            } while (cursor.moveToNext());
        }
        mGlobalModel.setOrganizations(getOrganizationList());
        mGlobalModel.setOrgTypesReal(getRealDataFromTables(DBHelper.TABLE_NAME_ORG_TYPES_REAL));
        mGlobalModel.setCurrenciesReal(getRealDataFromTables(DBHelper.TABLE_NAME_CURRENCIES_REAL));
        mGlobalModel.setRegionsReal(getRealDataFromTables(DBHelper.TABLE_NAME_REGIONS_REAL));
        mGlobalModel.setCitiesReal(getRealDataFromTables(DBHelper.TABLE_NAME_CITIES_REAL));
        cursor.close();
        mDB.close();
        return mGlobalModel;
    }

    private List<PairedObject> getRealDataFromTables(String tableName) {
        Cursor cursor = mDB.query(tableName, null, null, null, null, null, null);
        List<PairedObject> orgTypeReal = new ArrayList<PairedObject>();

        if (cursor.moveToFirst()) {
            do {
                PairedObject pairedObject = new PairedObject();
                pairedObject.setId(cursor.getString(cursor.getColumnIndex(DBHelper.ID)));
                pairedObject.setName(cursor.getString(cursor.getColumnIndex(DBHelper.NAME)));

                orgTypeReal.add(pairedObject);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orgTypeReal;
    }

    public List<Organization> getOrganizationList() {
        mDBHelper = new DBHelper(mContext);
        mDB = mDBHelper.getReadableDatabase();
        Cursor cursor = mDB.query(DBHelper.TABLE_NAME_ORGANIZATION, null, null, null, null, null, null);
        Cursor cursor2 = mDB.query(DBHelper.TABLE_NAME_CURRENCY1, null, null, null, null, null, null);

        List<Organization> organizations = new ArrayList<Organization>();
        if (cursor.moveToFirst()) {
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
                        }

                    } while (cursor2.moveToNext());
                }
                mOrganization.setCurrenciesReal(mCurrencies);
                organizations.add(mOrganization);
            } while (cursor.moveToNext());
        }
        cursor.close();
        cursor2.close();
        return organizations;
    }
}
