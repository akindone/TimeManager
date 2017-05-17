package com.timer.jike.timemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * by heyongjian
 * on 15/8/27
 */
public class UtilSP {
    public static final String TIME_MANAGER_SP = "TIME_MANAGER_SP";

    public static final String EVENT_START_TIME = "EVENT_START_TIME";
    public static final String EVENT_TITLE = "EVENT_TITLE";

    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PWD = "USER_PWD";

    public static final String SYNC_TIME = "SYNC_TIME";

    public static final String EVENT_PROPERTY_TYPE_DEFAULT = "EVENT_PROPERTY_TYPE_DEFAULT";
    public static final String EVENT_PROPERTY_PREDICT_DEFAULT = "EVENT_PROPERTY_PREDICT_DEFAULT";
    public static final String EVENT_PROPERTY_IMPORTANT_DEFAULT = "EVENT_PROPERTY_IMPORTANT_DEFAULT";

//    用BmobUser的objectId去作为key


    public static SharedPreferences getSPSetting(Context context) {
        return context.getSharedPreferences(TIME_MANAGER_SP, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSPSetting(Context context, String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

}
