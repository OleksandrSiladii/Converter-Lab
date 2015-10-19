package com.example.myapplication3.app.loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;
import com.example.myapplication3.app.Constants;
import com.example.myapplication3.app.DB.DBWorker;

/**
 * Created by sasha on 14.10.2015.
 */
public class GetModelCursorLoader extends CursorLoader {
    private Context context;

    public GetModelCursorLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Cursor loadInBackground() {
        Log.d(Constants.TAG_LOG, "work LOADER");

        DBWorker dbWorker = DBWorker.getInstance(context);
        return dbWorker.getGlobalModelCursorFromDB();
    }


}
