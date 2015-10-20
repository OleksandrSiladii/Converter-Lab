package com.example.myapplication3.app.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.example.myapplication3.app.Constants;
import com.example.myapplication3.app.DB.DBWorker;
import com.example.myapplication3.app.models.GlobalModel;


public class GetModelFromDBLoader extends AsyncTaskLoader<GlobalModel> {
    private Context context;

    public GetModelFromDBLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public GlobalModel loadInBackground() {
        return DBWorker.getInstance(context).getGlobalModelFromDB();
    }


}
