package com.loc;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: DateUtil.java */
/* loaded from: classes.dex */
public final class et {
    private static long a(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(j));
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static long a(long j, long j2, int i) {
        if (i > 0) {
            try {
                if (Math.abs(j - j2) > i * 31536000000L) {
                    long a = a(j2) + (j - a(j));
                    long abs = Math.abs(a - j2);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(a));
                    int i2 = calendar.get(11);
                    if (i2 == 23 && abs >= 82800000) {
                        a -= DateUtils.MILLIS_PER_DAY;
                    }
                    return (i2 != 0 || abs < 82800000) ? a : a + DateUtils.MILLIS_PER_DAY;
                }
            } catch (Throwable unused) {
            }
        }
        return j;
    }
}
