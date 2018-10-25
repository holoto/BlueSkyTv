package com.example.holoto.blueskytv.UrlDB;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.greendao.database.Database;

public class TvDbApp extends Application
{
    private DaoSession daoSession;

    @Override
    public void onCreate()
    {
        super.onCreate();
        TestLeakcanary();
        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,"TvUrl-Db");
        Database database=helper.getWritableDb();

        daoSession=new DaoMaster(database).newSession();
        Log.e("applicat", "onCreate: "+"ddd" );
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
    protected void TestLeakcanary()
    {
        if (LeakCanary.isInAnalyzerProcess(this))
        {
            return;
        }
        LeakCanary.install(this);
    }
}
