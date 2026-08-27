package com.czw.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes.dex */
public class DateUtil {
    private static final int FIRST_DAY = 2;
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DateUtil() {
    }

    public static Date afterMonthDate(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, i);
        return calendar.getTime();
    }

    public static int[] currentYearMonth() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new int[]{calendar.get(1), calendar.get(2) + 1};
    }

    public static int dayForWeek(String str) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(yyyyMMdd.parse(str));
        if (calendar.get(7) == 1) {
            return 7;
        }
        return calendar.get(7) - 1;
    }

    public static int daysBetween(Date date, Date date2) {
        return (int) (((float) (date.getTime() - date2.getTime())) / 8.64E7f);
    }

    public static String formatDate(String str, long j) {
        return new SimpleDateFormat(str).format(Long.valueOf(j));
    }

    public static String formatDate(String str, Date date) {
        return new SimpleDateFormat(str).format(date);
    }

    private static long getDayNumber(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    public static String getFirstDayOfMonth(int i, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, i);
        calendar.set(2, i2 - 1);
        calendar.set(5, calendar.getMinimum(5));
        return new SimpleDateFormat("yyyy-MM-dd ").format(calendar.getTime());
    }

    public static int getLastDayOfMonth(int i, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i2, 0);
        return calendar.get(5);
    }

    public static Date getTheDayAfterDate(String str, String str2, int i) {
        try {
            Date parse = new SimpleDateFormat(str).parse(str2);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            calendar.add(5, i);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getTheDayAfterDate(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, i);
        return calendar.getTime();
    }

    public static long[] getWeekdays(int i) {
        long[] jArr = new long[7];
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, i * 7);
        setToFirstDay(calendar);
        for (int i2 = 0; i2 < 7; i2++) {
            jArr[i2] = getDayNumber(calendar);
            calendar.add(5, 1);
        }
        return jArr;
    }

    public static int getYearMonthAndDayCount(int i, int i2) {
        int[] iArr = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.add(1, i);
        calendar.add(2, i2);
        return calendar.getActualMaximum(5);
    }

    public static void main(String[] strArr) {
        System.out.println(System.currentTimeMillis());
        System.out.println(yyyyMMdd.format(new Date(1523106000000L)));
    }

    public static Date parserFromStr(String str, String str2) {
        try {
            return new SimpleDateFormat(str).parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void setToFirstDay(Calendar calendar) {
        while (calendar.get(7) != 2) {
            calendar.add(5, -1);
        }
    }
}
