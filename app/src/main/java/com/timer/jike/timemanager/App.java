package com.timer.jike.timemanager;

import android.app.Application;

import com.timer.jike.timemanager.utils.UtilBmob;
import com.timer.jike.timemanager.utils.UtilDB;
import com.timer.jike.timemanager.utils.UtilLog;


/**
 * Created by jike on 2017/5/6.
 */

public class App extends Application{

    private static final String TAG = "App";
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        UtilDB.init(this);
        UtilBmob.init(this);

    }

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {//不能保证一定会被调用
        super.onTerminate();
        UtilLog.i(TAG,"------onTerminate");
    }

    @Override
    public void onLowMemory() {//在内存比较紧张时,根据优先级把后台程序杀死时,系统回调他
        super.onLowMemory();
        UtilLog.i(TAG,"------onTerminate");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        UtilLog.i(TAG,"------onTrimMemory",level);
    }
}
