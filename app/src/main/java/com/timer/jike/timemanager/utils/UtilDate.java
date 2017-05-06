package com.timer.jike.timemanager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jike on 2017/5/6.
 */

public class UtilDate {
    public static Calendar date2calendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Date string2Date(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date dt = null;
        try {
            dt = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }


    public static String date2String(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = df.format(date);
        return time;
    }

    public static String calendar2String(Calendar time) {
        return date2String(time.getTime());
    }
}
