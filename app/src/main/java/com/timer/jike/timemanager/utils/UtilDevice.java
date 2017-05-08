package com.timer.jike.timemanager.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by jike on 2017/5/8.
 */

public class UtilDevice {
    private static String sDeviceId;

    public static String getDeviceId(Context context) {
        if (sDeviceId == null) {
            sDeviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        if (sDeviceId == null) {
            sDeviceId = Build.SERIAL;
        }
        return sDeviceId;
    }
}
