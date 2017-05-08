package com.timer.jike.timemanager.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.timer.jike.timemanager.bean.ColorRuleDao;
import com.timer.jike.timemanager.bean.DaoMaster;
import com.timer.jike.timemanager.bean.EventDao;

/**
 * Created by jike on 2017/5/8.
 */

public class UtilSQLiteOpenHelper extends DaoMaster.OpenHelper {
    public UtilSQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    public UtilSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        MigrationHelper.migrate(db,EventDao.class,ColorRuleDao.class);

    }
}
