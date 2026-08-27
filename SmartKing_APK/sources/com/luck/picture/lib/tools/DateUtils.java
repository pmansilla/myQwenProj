package com.luck.picture.lib.tools;

import com.amap.location.common.model.AmapLoc;
import java.text.SimpleDateFormat;

/* loaded from: classes.dex */
public class DateUtils {
    private static SimpleDateFormat msFormat = new SimpleDateFormat("mm:ss");

    public static String cdTime(long j, long j2) {
        long j3 = j2 - j;
        if (j3 > 1000) {
            return (j3 / 1000) + "秒";
        }
        return j3 + "毫秒";
    }

    public static int dateDiffer(long j) {
        try {
            return (int) Math.abs(Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0, 10)) - j);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String timeParse(long j) {
        String str = "";
        if (j > 1000) {
            return timeParseMinute(j);
        }
        long j2 = j / org.apache.commons.lang.time.DateUtils.MILLIS_PER_MINUTE;
        long round = Math.round(((float) (j % org.apache.commons.lang.time.DateUtils.MILLIS_PER_MINUTE)) / 1000.0f);
        if (j2 < 10) {
            str = "" + AmapLoc.RESULT_TYPE_GPS;
        }
        String str2 = str + j2 + ":";
        if (round < 10) {
            str2 = str2 + AmapLoc.RESULT_TYPE_GPS;
        }
        return str2 + round;
    }

    public static String timeParseMinute(long j) {
        try {
            return msFormat.format(Long.valueOf(j));
        } catch (Exception e) {
            e.printStackTrace();
            return "0:00";
        }
    }
}
