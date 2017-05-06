package com.timer.jike.timemanager.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.text.DecimalFormat;

/**
 * Created by jike on 2017/5/6.
 */

public class UtilString {
    public static String getTimeString(long seconds) {
        long t = seconds;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                stringBuilder.insert(0, format(t % 60, 2, "0"));
                t = t / 60;
                stringBuilder.insert(0, " ");
            } else if (i == 1) {
                stringBuilder.insert(0, format(t % 60, 2, "0"));
                t = t / 60;
                stringBuilder.insert(0, ":");
            } else if (i == 2) {
                stringBuilder.insert(0, format(t, 2, "0"));
            }
        }
        return stringBuilder.toString();
    }

    public static SpannableString getTimeSpan(long seconds) {
        String timeString = getTimeString(seconds);
        String[] split = timeString.split(" ");
        return getTextSpan(split, new float[]{1, 0.5f}, null);
    }

    public static String format(long l, int length, String replacement) {
        String s = String.valueOf(l);
        int i = length - s.length();
        String result = "";
        for (int j = 0; j < i; j++) {
            result = replacement.concat(result);
        }
        return result.concat(s);
    }

    public static SpannableString getTextSpan(String[] texts, float[] scales, int[] colors) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < texts.length; i++) {
            stringBuilder.append(texts[i]);
        }
        SpannableString spannableString = new SpannableString(stringBuilder.toString());
        int start = 0;
        for (int i = 0; i < texts.length; i++) {
            int length = texts[i].length();
            if (scales != null)
                spannableString.setSpan(new RelativeSizeSpan(scales[i]), start, start + length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (colors != null)
                spannableString.setSpan(new ForegroundColorSpan(colors[i]), start, start + length, Spanned
                        .SPAN_EXCLUSIVE_EXCLUSIVE);
            start += length;
        }
        return spannableString;

    }

    public static String getHour(long duration) {
        if (duration < 0)
            return "--";
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        DecimalFormat decimalFormat2 = new DecimalFormat("0");
        double seconds = duration / 1000.00;
        if (seconds / 3600 >= 1) {
            return decimalFormat.format(seconds / 3600) + "h";
        } else if (seconds / 60 >= 1) {
            return decimalFormat.format(seconds / 60) + "min";
        } else
            return decimalFormat2.format(seconds) + "s";
    }
}
