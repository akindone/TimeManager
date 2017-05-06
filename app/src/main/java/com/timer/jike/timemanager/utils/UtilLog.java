package com.timer.jike.timemanager.utils;

import android.util.Log;

import com.timer.jike.timemanager.BuildConfig;


/**
 * Created by jike
 * on 2016/12/15.
 */

public class UtilLog {

    private static final int PART_LIMIT = 3500;


    private static void v(int priority, String tag, Object... msg) {
        if (!BuildConfig.DEBUG )
            return;
        if (msg == null)
            return;

        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < msg.length; i++) {
            sb.append(msg[i]);  //将输出内容进行拼接
            if (i < msg.length - 1)
                sb.append(" || ");
        }
        String result = sb.toString();
        multiPartLog(priority, tag, result);
    }

    public static void e(String tag, Object... msg) {
        v(Log.ERROR, tag, msg);
    }

    public static void w(String tag, Object... msg) {
        v(Log.WARN, tag, msg);
    }

    public static void i(String tag, Object... msg) {
        v(Log.INFO, tag, msg);
    }

    public static void d(String tag, Object... msg) {
        v(Log.DEBUG, tag, msg);
    }

    public static void v(String tag, Object... msg) {
        v(Log.VERBOSE, tag, msg);
    }

    private static void multiPartLog(int priority, String tag, String result) {
        if (result.length() > PART_LIMIT) {
            for (int i = 0; i < result.length(); i += PART_LIMIT) {
                if (i + PART_LIMIT < result.length())
                    log(priority, tag, "multiPartLog--" + (i / PART_LIMIT) + "  " + result.substring(i, i + PART_LIMIT));
                else
                    log(priority, tag, "multiPartLog--" + (i / PART_LIMIT) + "  " + result.substring(i, result.length()));
            }
        } else
            log(priority, tag, result);
    }

    private static void log(int priority, String tag, String result) {
        switch (priority) {
            case Log.ERROR:
                Log.e(tag, result);
                break;
            case Log.WARN:
                Log.w(tag, result);
                break;
            case Log.INFO:
                Log.i(tag, result);
                break;
            case Log.DEBUG:
                Log.d(tag, result);
                break;
            case Log.VERBOSE:
                Log.v(tag, result);
                break;
        }
    }

}
