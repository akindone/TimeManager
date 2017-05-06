package com.timer.jike.timemanager;

import android.app.Application;

import com.timer.jike.timemanager.bean.DaoMaster;
import com.timer.jike.timemanager.bean.DaoSession;
import com.timer.jike.timemanager.utils.UtilDB;

import org.greenrobot.greendao.database.Database;

/**
 * Created by jike on 2017/5/6.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        UtilDB.init(this);

    }

}
