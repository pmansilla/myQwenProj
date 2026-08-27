package org.apache.commons.lang.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TimeZone;

/* loaded from: classes2.dex */
public class DateUtils {
    public static final int MILLIS_IN_DAY = 86400000;
    public static final int MILLIS_IN_HOUR = 3600000;
    public static final int MILLIS_IN_MINUTE = 60000;
    public static final int MILLIS_IN_SECOND = 1000;
    public static final long MILLIS_PER_DAY = 86400000;
    public static final long MILLIS_PER_HOUR = 3600000;
    public static final long MILLIS_PER_MINUTE = 60000;
    public static final long MILLIS_PER_SECOND = 1000;
    public static final int RANGE_MONTH_MONDAY = 6;
    public static final int RANGE_MONTH_SUNDAY = 5;
    public static final int RANGE_WEEK_CENTER = 4;
    public static final int RANGE_WEEK_MONDAY = 2;
    public static final int RANGE_WEEK_RELATIVE = 3;
    public static final int RANGE_WEEK_SUNDAY = 1;
    public static final int SEMI_MONTH = 1001;
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
    private static final int[][] fields = {new int[]{14}, new int[]{13}, new int[]{12}, new int[]{11, 10}, new int[]{5, 5, 9}, new int[]{2, 1001}, new int[]{1}, new int[]{0}};

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class DateIterator implements Iterator {
        private final Calendar endFinal;
        private final Calendar spot;

        DateIterator(Calendar calendar, Calendar calendar2) {
            this.endFinal = calendar2;
            this.spot = calendar;
            this.spot.add(5, -1);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.spot.before(this.endFinal);
        }

        @Override // java.util.Iterator
        public Object next() {
            if (this.spot.equals(this.endFinal)) {
                throw new NoSuchElementException();
            }
            this.spot.add(5, 1);
            return this.spot.clone();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static Date add(Date date, int i, int i2) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(i, i2);
        return calendar.getTime();
    }

    public static Date addDays(Date date, int i) {
        return add(date, 5, i);
    }

    public static Date addHours(Date date, int i) {
        return add(date, 11, i);
    }

    public static Date addMilliseconds(Date date, int i) {
        return add(date, 14, i);
    }

    public static Date addMinutes(Date date, int i) {
        return add(date, 12, i);
    }

    public static Date addMonths(Date date, int i) {
        return add(date, 2, i);
    }

    public static Date addSeconds(Date date, int i) {
        return add(date, 13, i);
    }

    public static Date addWeeks(Date date, int i) {
        return add(date, 3, i);
    }

    public static Date addYears(Date date, int i) {
        return add(date, 1, i);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0024. Please report as an issue. */
    private static long getFragment(Calendar calendar, int i, int i2) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        long millisPerUnit = getMillisPerUnit(i2);
        long j = 0;
        switch (i) {
            case 1:
                j = 0 + ((calendar.get(6) * MILLIS_PER_DAY) / millisPerUnit);
                break;
            case 2:
                j = 0 + ((calendar.get(5) * MILLIS_PER_DAY) / millisPerUnit);
                break;
        }
        switch (i) {
            case 1:
            case 2:
            case 5:
            case 6:
                j += (calendar.get(11) * MILLIS_PER_HOUR) / millisPerUnit;
                j += (calendar.get(12) * MILLIS_PER_MINUTE) / millisPerUnit;
                j += (calendar.get(13) * 1000) / millisPerUnit;
                return j + ((calendar.get(14) * 1) / millisPerUnit);
            case 3:
            case 4:
            case 7:
            case 8:
            case 9:
            case 10:
            default:
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("The fragment ");
                stringBuffer.append(i);
                stringBuffer.append(" is not supported");
                throw new IllegalArgumentException(stringBuffer.toString());
            case 11:
                j += (calendar.get(12) * MILLIS_PER_MINUTE) / millisPerUnit;
                j += (calendar.get(13) * 1000) / millisPerUnit;
                return j + ((calendar.get(14) * 1) / millisPerUnit);
            case 12:
                j += (calendar.get(13) * 1000) / millisPerUnit;
                return j + ((calendar.get(14) * 1) / millisPerUnit);
            case 13:
                return j + ((calendar.get(14) * 1) / millisPerUnit);
            case 14:
                return j;
        }
    }

    private static long getFragment(Date date, int i, int i2) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getFragment(calendar, i, i2);
    }

    public static long getFragmentInDays(Calendar calendar, int i) {
        return getFragment(calendar, i, 6);
    }

    public static long getFragmentInDays(Date date, int i) {
        return getFragment(date, i, 6);
    }

    public static long getFragmentInHours(Calendar calendar, int i) {
        return getFragment(calendar, i, 11);
    }

    public static long getFragmentInHours(Date date, int i) {
        return getFragment(date, i, 11);
    }

    public static long getFragmentInMilliseconds(Calendar calendar, int i) {
        return getFragment(calendar, i, 14);
    }

    public static long getFragmentInMilliseconds(Date date, int i) {
        return getFragment(date, i, 14);
    }

    public static long getFragmentInMinutes(Calendar calendar, int i) {
        return getFragment(calendar, i, 12);
    }

    public static long getFragmentInMinutes(Date date, int i) {
        return getFragment(date, i, 12);
    }

    public static long getFragmentInSeconds(Calendar calendar, int i) {
        return getFragment(calendar, i, 13);
    }

    public static long getFragmentInSeconds(Date date, int i) {
        return getFragment(date, i, 13);
    }

    private static long getMillisPerUnit(int i) {
        switch (i) {
            case 5:
            case 6:
                return MILLIS_PER_DAY;
            default:
                switch (i) {
                    case 11:
                        return MILLIS_PER_HOUR;
                    case 12:
                        return MILLIS_PER_MINUTE;
                    case 13:
                        return 1000L;
                    case 14:
                        return 1L;
                    default:
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("The unit ");
                        stringBuffer.append(i);
                        stringBuffer.append(" cannot be represented is milleseconds");
                        throw new IllegalArgumentException(stringBuffer.toString());
                }
        }
    }

    public static boolean isSameDay(Calendar calendar, Calendar calendar2) {
        if (calendar == null || calendar2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return calendar.get(0) == calendar2.get(0) && calendar.get(1) == calendar2.get(1) && calendar.get(6) == calendar2.get(6);
    }

    public static boolean isSameDay(Date date, Date date2) {
        if (date == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return isSameDay(calendar, calendar2);
    }

    public static boolean isSameInstant(Calendar calendar, Calendar calendar2) {
        if (calendar == null || calendar2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return calendar.getTime().getTime() == calendar2.getTime().getTime();
    }

    public static boolean isSameInstant(Date date, Date date2) {
        if (date == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return date.getTime() == date2.getTime();
    }

    public static boolean isSameLocalTime(Calendar calendar, Calendar calendar2) {
        if (calendar == null || calendar2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return calendar.get(14) == calendar2.get(14) && calendar.get(13) == calendar2.get(13) && calendar.get(12) == calendar2.get(12) && calendar.get(10) == calendar2.get(10) && calendar.get(6) == calendar2.get(6) && calendar.get(1) == calendar2.get(1) && calendar.get(0) == calendar2.get(0) && calendar.getClass() == calendar2.getClass();
    }

    public static Iterator iterator(Object obj, int i) {
        if (obj == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (obj instanceof Date) {
            return iterator((Date) obj, i);
        }
        if (obj instanceof Calendar) {
            return iterator((Calendar) obj, i);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Could not iterate based on ");
        stringBuffer.append(obj);
        throw new ClassCastException(stringBuffer.toString());
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0007. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x007a A[LOOP:0: B:20:0x0074->B:22:0x007a, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0084 A[LOOP:1: B:24:0x007e->B:26:0x0084, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.Iterator iterator(java.util.Calendar r7, int r8) {
        /*
            if (r7 == 0) goto L8e
            r0 = -1
            r1 = 2
            r2 = 5
            r3 = 1
            r4 = 7
            switch(r8) {
                case 1: goto L42;
                case 2: goto L42;
                case 3: goto L42;
                case 4: goto L42;
                case 5: goto L26;
                case 6: goto L26;
                default: goto La;
            }
        La:
            java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            java.lang.String r1 = "The range style "
            r0.append(r1)
            r0.append(r8)
            java.lang.String r8 = " is not valid."
            r0.append(r8)
            java.lang.String r8 = r0.toString()
            r7.<init>(r8)
            throw r7
        L26:
            java.util.Calendar r7 = truncate(r7, r1)
            java.lang.Object r5 = r7.clone()
            java.util.Calendar r5 = (java.util.Calendar) r5
            r5.add(r1, r3)
            r5.add(r2, r0)
            r6 = 6
            if (r8 != r6) goto L3d
            r6 = r5
            r5 = r7
        L3b:
            r7 = 1
            goto L64
        L3d:
            r6 = r5
            r1 = 1
            r5 = r7
            r7 = 7
            goto L64
        L42:
            java.util.Calendar r5 = truncate(r7, r2)
            java.util.Calendar r6 = truncate(r7, r2)
            switch(r8) {
                case 1: goto L62;
                case 2: goto L3b;
                case 3: goto L5b;
                case 4: goto L4e;
                default: goto L4d;
            }
        L4d:
            goto L62
        L4e:
            int r8 = r7.get(r4)
            int r1 = r8 + (-3)
            int r7 = r7.get(r4)
            int r7 = r7 + 3
            goto L64
        L5b:
            int r1 = r7.get(r4)
            int r7 = r1 + (-1)
            goto L64
        L62:
            r7 = 7
            r1 = 1
        L64:
            if (r1 >= r3) goto L68
            int r1 = r1 + 7
        L68:
            if (r1 <= r4) goto L6c
            int r1 = r1 + (-7)
        L6c:
            if (r7 >= r3) goto L70
            int r7 = r7 + 7
        L70:
            if (r7 <= r4) goto L74
            int r7 = r7 + (-7)
        L74:
            int r8 = r5.get(r4)
            if (r8 == r1) goto L7e
            r5.add(r2, r0)
            goto L74
        L7e:
            int r8 = r6.get(r4)
            if (r8 == r7) goto L88
            r6.add(r2, r3)
            goto L7e
        L88:
            org.apache.commons.lang.time.DateUtils$DateIterator r7 = new org.apache.commons.lang.time.DateUtils$DateIterator
            r7.<init>(r5, r6)
            return r7
        L8e:
            java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
            java.lang.String r8 = "The date must not be null"
            r7.<init>(r8)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.time.DateUtils.iterator(java.util.Calendar, int):java.util.Iterator");
    }

    public static Iterator iterator(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return iterator(calendar, i);
    }

    /* JADX WARN: Code restructure failed: missing block: B:63:0x00bd, code lost:
    
        if (r3 > 7) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00d7, code lost:
    
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00d8, code lost:
    
        r6 = r3;
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x00d5, code lost:
    
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x00d3, code lost:
    
        if (r3 > 6) goto L69;
     */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x011e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0107  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void modify(java.util.Calendar r12, int r13, boolean r14) {
        /*
            Method dump skipped, instructions count: 326
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.time.DateUtils.modify(java.util.Calendar, int, boolean):void");
    }

    public static Date parseDate(String str, String[] strArr) throws ParseException {
        if (str == null || strArr == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
        ParsePosition parsePosition = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = null;
        for (int i = 0; i < strArr.length; i++) {
            if (i == 0) {
                simpleDateFormat = new SimpleDateFormat(strArr[0]);
            } else {
                simpleDateFormat.applyPattern(strArr[i]);
            }
            parsePosition.setIndex(0);
            Date parse = simpleDateFormat.parse(str, parsePosition);
            if (parse != null && parsePosition.getIndex() == str.length()) {
                return parse;
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Unable to parse the date: ");
        stringBuffer.append(str);
        throw new ParseException(stringBuffer.toString(), -1);
    }

    public static Calendar round(Calendar calendar, int i) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar2 = (Calendar) calendar.clone();
        modify(calendar2, i, true);
        return calendar2;
    }

    public static Date round(Object obj, int i) {
        if (obj == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (obj instanceof Date) {
            return round((Date) obj, i);
        }
        if (obj instanceof Calendar) {
            return round((Calendar) obj, i).getTime();
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Could not round ");
        stringBuffer.append(obj);
        throw new ClassCastException(stringBuffer.toString());
    }

    public static Date round(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        modify(calendar, i, true);
        return calendar.getTime();
    }

    private static Date set(Date date, int i, int i2) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(date);
        calendar.set(i, i2);
        return calendar.getTime();
    }

    public static Date setDays(Date date, int i) {
        return set(date, 5, i);
    }

    public static Date setHours(Date date, int i) {
        return set(date, 11, i);
    }

    public static Date setMilliseconds(Date date, int i) {
        return set(date, 14, i);
    }

    public static Date setMinutes(Date date, int i) {
        return set(date, 12, i);
    }

    public static Date setMonths(Date date, int i) {
        return set(date, 2, i);
    }

    public static Date setSeconds(Date date, int i) {
        return set(date, 13, i);
    }

    public static Date setYears(Date date, int i) {
        return set(date, 1, i);
    }

    public static Calendar truncate(Calendar calendar, int i) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar2 = (Calendar) calendar.clone();
        modify(calendar2, i, false);
        return calendar2;
    }

    public static Date truncate(Object obj, int i) {
        if (obj == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (obj instanceof Date) {
            return truncate((Date) obj, i);
        }
        if (obj instanceof Calendar) {
            return truncate((Calendar) obj, i).getTime();
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Could not truncate ");
        stringBuffer.append(obj);
        throw new ClassCastException(stringBuffer.toString());
    }

    public static Date truncate(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        modify(calendar, i, false);
        return calendar.getTime();
    }
}
