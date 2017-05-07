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
    public static final String EVENT_TITLE = "EVENT_START_TIME";


    public static SharedPreferences getSPSetting(Context context) {
        return context.getSharedPreferences(TIME_MANAGER_SP, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSPSetting(Context context, String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

}
