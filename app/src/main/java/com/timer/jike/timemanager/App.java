package com.timer.jike.timemanager;

import android.app.Application;

import com.timer.jike.timemanager.utils.UtilBmob;
import com.timer.jike.timemanager.utils.UtilDB;


/**
 * Created by jike on 2017/5/6.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        UtilDB.init(this);
        UtilBmob.init(this);

    }

}
