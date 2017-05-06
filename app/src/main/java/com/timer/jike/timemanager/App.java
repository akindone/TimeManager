package com.timer.jike.timemanager;

import android.app.Application;

import com.timer.jike.timemanager.bean.DaoMaster;
import com.timer.jike.timemanager.bean.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by jike on 2017/5/6.
 */

public class App extends Application{
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "events-db-encrypted" : "events-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
