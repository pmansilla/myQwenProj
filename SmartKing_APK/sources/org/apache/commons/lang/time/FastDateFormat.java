package org.apache.commons.lang.time;

import com.czw.smartkit.BuildConfig;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang.Validate;

/* loaded from: classes2.dex */
public class FastDateFormat extends Format {
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private static String cDefaultPattern = null;
    private static final long serialVersionUID = 1;
    private final Locale mLocale;
    private final boolean mLocaleForced;
    private transient int mMaxLengthEstimate;
    private final String mPattern;
    private transient Rule[] mRules;
    private final TimeZone mTimeZone;
    private final boolean mTimeZoneForced;
    private static final Map cInstanceCache = new HashMap(7);
    private static final Map cDateInstanceCache = new HashMap(7);
    private static final Map cTimeInstanceCache = new HashMap(7);
    private static final Map cDateTimeInstanceCache = new HashMap(7);
    private static final Map cTimeZoneDisplayCache = new HashMap(7);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class CharacterLiteral implements Rule {
        private final char mValue;

        CharacterLiteral(char c) {
            this.mValue = c;
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            stringBuffer.append(this.mValue);
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public interface NumberRule extends Rule {
        void appendTo(StringBuffer stringBuffer, int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class PaddedNumberField implements NumberRule {
        private final int mField;
        private final int mSize;

        PaddedNumberField(int i, int i2) {
            if (i2 < 3) {
                throw new IllegalArgumentException();
            }
            this.mField = i;
            this.mSize = i2;
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer stringBuffer, int i) {
            int length;
            if (i < 100) {
                int i2 = this.mSize;
                while (true) {
                    i2--;
                    if (i2 < 2) {
                        stringBuffer.append((char) ((i / 10) + 48));
                        stringBuffer.append((char) ((i % 10) + 48));
                        return;
                    }
                    stringBuffer.append('0');
                }
            } else {
                if (i < 1000) {
                    length = 3;
                } else {
                    Validate.isTrue(i > -1, "Negative values should not be possible", i);
                    length = Integer.toString(i).length();
                }
                int i3 = this.mSize;
                while (true) {
                    i3--;
                    if (i3 < length) {
                        stringBuffer.append(Integer.toString(i));
                        return;
                    }
                    stringBuffer.append('0');
                }
            }
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            appendTo(stringBuffer, calendar.get(this.mField));
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return 4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class Pair {
        private final Object mObj1;
        private final Object mObj2;

        public Pair(Object obj, Object obj2) {
            this.mObj1 = obj;
            this.mObj2 = obj2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Pair)) {
                return false;
            }
            Pair pair = (Pair) obj;
            if (this.mObj1 != null ? this.mObj1.equals(pair.mObj1) : pair.mObj1 == null) {
                if (this.mObj2 == null) {
                    if (pair.mObj2 == null) {
                        return true;
                    }
                } else if (this.mObj2.equals(pair.mObj2)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return (this.mObj1 == null ? 0 : this.mObj1.hashCode()) + (this.mObj2 != null ? this.mObj2.hashCode() : 0);
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[");
            stringBuffer.append(this.mObj1);
            stringBuffer.append(':');
            stringBuffer.append(this.mObj2);
            stringBuffer.append(']');
            return stringBuffer.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public interface Rule {
        void appendTo(StringBuffer stringBuffer, Calendar calendar);

        int estimateLength();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class StringLiteral implements Rule {
        private final String mValue;

        StringLiteral(String str) {
            this.mValue = str;
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            stringBuffer.append(this.mValue);
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return this.mValue.length();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class TextField implements Rule {
        private final int mField;
        private final String[] mValues;

        TextField(int i, String[] strArr) {
            this.mField = i;
            this.mValues = strArr;
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            stringBuffer.append(this.mValues[calendar.get(this.mField)]);
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            int length = this.mValues.length;
            int i = 0;
            while (true) {
                length--;
                if (length < 0) {
                    return i;
                }
                int length2 = this.mValues[length].length();
                if (length2 > i) {
                    i = length2;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class TimeZoneDisplayKey {
        private final Locale mLocale;
        private final int mStyle;
        private final TimeZone mTimeZone;

        TimeZoneDisplayKey(TimeZone timeZone, boolean z, int i, Locale locale) {
            this.mTimeZone = timeZone;
            this.mStyle = z ? i | Integer.MIN_VALUE : i;
            this.mLocale = locale;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TimeZoneDisplayKey)) {
                return false;
            }
            TimeZoneDisplayKey timeZoneDisplayKey = (TimeZoneDisplayKey) obj;
            return this.mTimeZone.equals(timeZoneDisplayKey.mTimeZone) && this.mStyle == timeZoneDisplayKey.mStyle && this.mLocale.equals(timeZoneDisplayKey.mLocale);
        }

        public int hashCode() {
            return (this.mStyle * 31) + this.mLocale.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class TimeZoneNameRule implements Rule {
        private final String mDaylight;
        private final Locale mLocale;
        private final String mStandard;
        private final int mStyle;
        private final TimeZone mTimeZone;
        private final boolean mTimeZoneForced;

        TimeZoneNameRule(TimeZone timeZone, boolean z, Locale locale, int i) {
            this.mTimeZone = timeZone;
            this.mTimeZoneForced = z;
            this.mLocale = locale;
            this.mStyle = i;
            if (z) {
                this.mStandard = FastDateFormat.getTimeZoneDisplay(timeZone, false, i, locale);
                this.mDaylight = FastDateFormat.getTimeZoneDisplay(timeZone, true, i, locale);
            } else {
                this.mStandard = null;
                this.mDaylight = null;
            }
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            if (this.mTimeZoneForced) {
                if (!this.mTimeZone.useDaylightTime() || calendar.get(16) == 0) {
                    stringBuffer.append(this.mStandard);
                    return;
                } else {
                    stringBuffer.append(this.mDaylight);
                    return;
                }
            }
            TimeZone timeZone = calendar.getTimeZone();
            if (!timeZone.useDaylightTime() || calendar.get(16) == 0) {
                stringBuffer.append(FastDateFormat.getTimeZoneDisplay(timeZone, false, this.mStyle, this.mLocale));
            } else {
                stringBuffer.append(FastDateFormat.getTimeZoneDisplay(timeZone, true, this.mStyle, this.mLocale));
            }
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return this.mTimeZoneForced ? Math.max(this.mStandard.length(), this.mDaylight.length()) : this.mStyle == 0 ? 4 : 40;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class TimeZoneNumberRule implements Rule {
        static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
        static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
        final boolean mColon;

        TimeZoneNumberRule(boolean z) {
            this.mColon = z;
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            int i = calendar.get(15) + calendar.get(16);
            if (i < 0) {
                stringBuffer.append('-');
                i = -i;
            } else {
                stringBuffer.append('+');
            }
            int i2 = i / DateUtils.MILLIS_IN_HOUR;
            stringBuffer.append((char) ((i2 / 10) + 48));
            stringBuffer.append((char) ((i2 % 10) + 48));
            if (this.mColon) {
                stringBuffer.append(':');
            }
            int i3 = (i / DateUtils.MILLIS_IN_MINUTE) - (i2 * 60);
            stringBuffer.append((char) ((i3 / 10) + 48));
            stringBuffer.append((char) ((i3 % 10) + 48));
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return 5;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class TwelveHourField implements NumberRule {
        private final NumberRule mRule;

        TwelveHourField(NumberRule numberRule) {
            this.mRule = numberRule;
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.NumberRule
        public void appendTo(StringBuffer stringBuffer, int i) {
            this.mRule.appendTo(stringBuffer, i);
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            int i = calendar.get(10);
            if (i == 0) {
                i = calendar.getLeastMaximum(10) + 1;
            }
            this.mRule.appendTo(stringBuffer, i);
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return this.mRule.estimateLength();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class TwentyFourHourField implements NumberRule {
        private final NumberRule mRule;

        TwentyFourHourField(NumberRule numberRule) {
            this.mRule = numberRule;
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.NumberRule
        public void appendTo(StringBuffer stringBuffer, int i) {
            this.mRule.appendTo(stringBuffer, i);
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            int i = calendar.get(11);
            if (i == 0) {
                i = calendar.getMaximum(11) + 1;
            }
            this.mRule.appendTo(stringBuffer, i);
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return this.mRule.estimateLength();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class TwoDigitMonthField implements NumberRule {
        static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();

        TwoDigitMonthField() {
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer stringBuffer, int i) {
            stringBuffer.append((char) ((i / 10) + 48));
            stringBuffer.append((char) ((i % 10) + 48));
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            appendTo(stringBuffer, calendar.get(2) + 1);
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class TwoDigitNumberField implements NumberRule {
        private final int mField;

        TwoDigitNumberField(int i) {
            this.mField = i;
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer stringBuffer, int i) {
            if (i >= 100) {
                stringBuffer.append(Integer.toString(i));
            } else {
                stringBuffer.append((char) ((i / 10) + 48));
                stringBuffer.append((char) ((i % 10) + 48));
            }
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            appendTo(stringBuffer, calendar.get(this.mField));
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class TwoDigitYearField implements NumberRule {
        static final TwoDigitYearField INSTANCE = new TwoDigitYearField();

        TwoDigitYearField() {
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer stringBuffer, int i) {
            stringBuffer.append((char) ((i / 10) + 48));
            stringBuffer.append((char) ((i % 10) + 48));
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            appendTo(stringBuffer, calendar.get(1) % 100);
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class UnpaddedMonthField implements NumberRule {
        static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();

        UnpaddedMonthField() {
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer stringBuffer, int i) {
            if (i < 10) {
                stringBuffer.append((char) (i + 48));
            } else {
                stringBuffer.append((char) ((i / 10) + 48));
                stringBuffer.append((char) ((i % 10) + 48));
            }
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            appendTo(stringBuffer, calendar.get(2) + 1);
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class UnpaddedNumberField implements NumberRule {
        static final UnpaddedNumberField INSTANCE_YEAR = new UnpaddedNumberField(1);
        private final int mField;

        UnpaddedNumberField(int i) {
            this.mField = i;
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.NumberRule
        public final void appendTo(StringBuffer stringBuffer, int i) {
            if (i < 10) {
                stringBuffer.append((char) (i + 48));
            } else if (i >= 100) {
                stringBuffer.append(Integer.toString(i));
            } else {
                stringBuffer.append((char) ((i / 10) + 48));
                stringBuffer.append((char) ((i % 10) + 48));
            }
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public void appendTo(StringBuffer stringBuffer, Calendar calendar) {
            appendTo(stringBuffer, calendar.get(this.mField));
        }

        @Override // org.apache.commons.lang.time.FastDateFormat.Rule
        public int estimateLength() {
            return 4;
        }
    }

    protected FastDateFormat(String str, TimeZone timeZone, Locale locale) {
        if (str == null) {
            throw new IllegalArgumentException("The pattern must not be null");
        }
        this.mPattern = str;
        this.mTimeZoneForced = timeZone != null;
        this.mTimeZone = timeZone == null ? TimeZone.getDefault() : timeZone;
        this.mLocaleForced = locale != null;
        this.mLocale = locale == null ? Locale.getDefault() : locale;
    }

    public static FastDateFormat getDateInstance(int i) {
        return getDateInstance(i, null, null);
    }

    public static FastDateFormat getDateInstance(int i, Locale locale) {
        return getDateInstance(i, null, locale);
    }

    public static FastDateFormat getDateInstance(int i, TimeZone timeZone) {
        return getDateInstance(i, timeZone, null);
    }

    public static synchronized FastDateFormat getDateInstance(int i, TimeZone timeZone, Locale locale) {
        FastDateFormat fastDateFormat;
        synchronized (FastDateFormat.class) {
            Object num = new Integer(i);
            if (timeZone != null) {
                num = new Pair(num, timeZone);
            }
            if (locale == null) {
                locale = Locale.getDefault();
            }
            Pair pair = new Pair(num, locale);
            fastDateFormat = (FastDateFormat) cDateInstanceCache.get(pair);
            if (fastDateFormat == null) {
                try {
                    fastDateFormat = getInstance(((SimpleDateFormat) DateFormat.getDateInstance(i, locale)).toPattern(), timeZone, locale);
                    cDateInstanceCache.put(pair, fastDateFormat);
                } catch (ClassCastException unused) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("No date pattern for locale: ");
                    stringBuffer.append(locale);
                    throw new IllegalArgumentException(stringBuffer.toString());
                }
            }
        }
        return fastDateFormat;
    }

    public static FastDateFormat getDateTimeInstance(int i, int i2) {
        return getDateTimeInstance(i, i2, null, null);
    }

    public static FastDateFormat getDateTimeInstance(int i, int i2, Locale locale) {
        return getDateTimeInstance(i, i2, null, locale);
    }

    public static FastDateFormat getDateTimeInstance(int i, int i2, TimeZone timeZone) {
        return getDateTimeInstance(i, i2, timeZone, null);
    }

    public static synchronized FastDateFormat getDateTimeInstance(int i, int i2, TimeZone timeZone, Locale locale) {
        FastDateFormat fastDateFormat;
        synchronized (FastDateFormat.class) {
            Pair pair = new Pair(new Integer(i), new Integer(i2));
            if (timeZone != null) {
                pair = new Pair(pair, timeZone);
            }
            if (locale == null) {
                locale = Locale.getDefault();
            }
            Pair pair2 = new Pair(pair, locale);
            fastDateFormat = (FastDateFormat) cDateTimeInstanceCache.get(pair2);
            if (fastDateFormat == null) {
                try {
                    fastDateFormat = getInstance(((SimpleDateFormat) DateFormat.getDateTimeInstance(i, i2, locale)).toPattern(), timeZone, locale);
                    cDateTimeInstanceCache.put(pair2, fastDateFormat);
                } catch (ClassCastException unused) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("No date time pattern for locale: ");
                    stringBuffer.append(locale);
                    throw new IllegalArgumentException(stringBuffer.toString());
                }
            }
        }
        return fastDateFormat;
    }

    private static synchronized String getDefaultPattern() {
        String str;
        synchronized (FastDateFormat.class) {
            if (cDefaultPattern == null) {
                cDefaultPattern = new SimpleDateFormat().toPattern();
            }
            str = cDefaultPattern;
        }
        return str;
    }

    public static FastDateFormat getInstance() {
        return getInstance(getDefaultPattern(), null, null);
    }

    public static FastDateFormat getInstance(String str) {
        return getInstance(str, null, null);
    }

    public static FastDateFormat getInstance(String str, Locale locale) {
        return getInstance(str, null, locale);
    }

    public static FastDateFormat getInstance(String str, TimeZone timeZone) {
        return getInstance(str, timeZone, null);
    }

    public static synchronized FastDateFormat getInstance(String str, TimeZone timeZone, Locale locale) {
        FastDateFormat fastDateFormat;
        synchronized (FastDateFormat.class) {
            FastDateFormat fastDateFormat2 = new FastDateFormat(str, timeZone, locale);
            fastDateFormat = (FastDateFormat) cInstanceCache.get(fastDateFormat2);
            if (fastDateFormat == null) {
                fastDateFormat2.init();
                cInstanceCache.put(fastDateFormat2, fastDateFormat2);
                fastDateFormat = fastDateFormat2;
            }
        }
        return fastDateFormat;
    }

    public static FastDateFormat getTimeInstance(int i) {
        return getTimeInstance(i, null, null);
    }

    public static FastDateFormat getTimeInstance(int i, Locale locale) {
        return getTimeInstance(i, null, locale);
    }

    public static FastDateFormat getTimeInstance(int i, TimeZone timeZone) {
        return getTimeInstance(i, timeZone, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v5, types: [org.apache.commons.lang.time.FastDateFormat$Pair] */
    /* JADX WARN: Type inference failed for: r2v6, types: [org.apache.commons.lang.time.FastDateFormat$Pair] */
    public static synchronized FastDateFormat getTimeInstance(int i, TimeZone timeZone, Locale locale) {
        FastDateFormat fastDateFormat;
        synchronized (FastDateFormat.class) {
            Integer num = new Integer(i);
            if (timeZone != null) {
                num = new Pair(num, timeZone);
            }
            if (locale != null) {
                num = new Pair(num, locale);
            }
            fastDateFormat = (FastDateFormat) cTimeInstanceCache.get(num);
            if (fastDateFormat == null) {
                if (locale == null) {
                    locale = Locale.getDefault();
                }
                try {
                    fastDateFormat = getInstance(((SimpleDateFormat) DateFormat.getTimeInstance(i, locale)).toPattern(), timeZone, locale);
                    cTimeInstanceCache.put(num, fastDateFormat);
                } catch (ClassCastException unused) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("No date pattern for locale: ");
                    stringBuffer.append(locale);
                    throw new IllegalArgumentException(stringBuffer.toString());
                }
            }
        }
        return fastDateFormat;
    }

    static synchronized String getTimeZoneDisplay(TimeZone timeZone, boolean z, int i, Locale locale) {
        String str;
        synchronized (FastDateFormat.class) {
            TimeZoneDisplayKey timeZoneDisplayKey = new TimeZoneDisplayKey(timeZone, z, i, locale);
            str = (String) cTimeZoneDisplayCache.get(timeZoneDisplayKey);
            if (str == null) {
                str = timeZone.getDisplayName(z, i, locale);
                cTimeZoneDisplayCache.put(timeZoneDisplayKey, str);
            }
        }
        return str;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init();
    }

    protected StringBuffer applyRules(Calendar calendar, StringBuffer stringBuffer) {
        Rule[] ruleArr = this.mRules;
        int length = this.mRules.length;
        for (int i = 0; i < length; i++) {
            ruleArr[i].appendTo(stringBuffer, calendar);
        }
        return stringBuffer;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof FastDateFormat)) {
            return false;
        }
        FastDateFormat fastDateFormat = (FastDateFormat) obj;
        return (this.mPattern == fastDateFormat.mPattern || this.mPattern.equals(fastDateFormat.mPattern)) && (this.mTimeZone == fastDateFormat.mTimeZone || this.mTimeZone.equals(fastDateFormat.mTimeZone)) && ((this.mLocale == fastDateFormat.mLocale || this.mLocale.equals(fastDateFormat.mLocale)) && this.mTimeZoneForced == fastDateFormat.mTimeZoneForced && this.mLocaleForced == fastDateFormat.mLocaleForced);
    }

    public String format(long j) {
        return format(new Date(j));
    }

    public String format(Calendar calendar) {
        return format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    public String format(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(this.mTimeZone);
        gregorianCalendar.setTime(date);
        return applyRules(gregorianCalendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    public StringBuffer format(long j, StringBuffer stringBuffer) {
        return format(new Date(j), stringBuffer);
    }

    @Override // java.text.Format
    public StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        if (obj instanceof Date) {
            return format((Date) obj, stringBuffer);
        }
        if (obj instanceof Calendar) {
            return format((Calendar) obj, stringBuffer);
        }
        if (obj instanceof Long) {
            return format(((Long) obj).longValue(), stringBuffer);
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("Unknown class: ");
        stringBuffer2.append(obj == null ? "<null>" : obj.getClass().getName());
        throw new IllegalArgumentException(stringBuffer2.toString());
    }

    public StringBuffer format(Calendar calendar, StringBuffer stringBuffer) {
        if (this.mTimeZoneForced) {
            calendar = (Calendar) calendar.clone();
            calendar.setTimeZone(this.mTimeZone);
        }
        return applyRules(calendar, stringBuffer);
    }

    public StringBuffer format(Date date, StringBuffer stringBuffer) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(this.mTimeZone);
        gregorianCalendar.setTime(date);
        return applyRules(gregorianCalendar, stringBuffer);
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public int getMaxLengthEstimate() {
        return this.mMaxLengthEstimate;
    }

    public String getPattern() {
        return this.mPattern;
    }

    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    public boolean getTimeZoneOverridesCalendar() {
        return this.mTimeZoneForced;
    }

    public int hashCode() {
        return this.mPattern.hashCode() + 0 + this.mTimeZone.hashCode() + (this.mTimeZoneForced ? 1 : 0) + this.mLocale.hashCode() + (this.mLocaleForced ? 1 : 0);
    }

    protected void init() {
        List parsePattern = parsePattern();
        this.mRules = (Rule[]) parsePattern.toArray(new Rule[parsePattern.size()]);
        int length = this.mRules.length;
        int i = 0;
        while (true) {
            length--;
            if (length < 0) {
                this.mMaxLengthEstimate = i;
                return;
            }
            i += this.mRules[length].estimateLength();
        }
    }

    @Override // java.text.Format
    public Object parseObject(String str, ParsePosition parsePosition) {
        parsePosition.setIndex(0);
        parsePosition.setErrorIndex(0);
        return null;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x004a. Please report as an issue. */
    protected List parsePattern() {
        Rule characterLiteral;
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(this.mLocale);
        ArrayList arrayList = new ArrayList();
        String[] eras = dateFormatSymbols.getEras();
        String[] months = dateFormatSymbols.getMonths();
        String[] shortMonths = dateFormatSymbols.getShortMonths();
        String[] weekdays = dateFormatSymbols.getWeekdays();
        String[] shortWeekdays = dateFormatSymbols.getShortWeekdays();
        String[] amPmStrings = dateFormatSymbols.getAmPmStrings();
        int length = this.mPattern.length();
        int i = 1;
        int[] iArr = new int[1];
        int i2 = 0;
        int i3 = 0;
        while (i3 < length) {
            iArr[i2] = i3;
            String parseToken = parseToken(this.mPattern, iArr);
            int i4 = iArr[i2];
            int length2 = parseToken.length();
            if (length2 == 0) {
                return arrayList;
            }
            switch (parseToken.charAt(i2)) {
                case '\'':
                    String substring = parseToken.substring(i);
                    characterLiteral = substring.length() == i ? new CharacterLiteral(substring.charAt(0)) : new StringLiteral(substring);
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'D':
                    characterLiteral = selectNumberRule(6, length2);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'E':
                    characterLiteral = new TextField(7, length2 < 4 ? shortWeekdays : weekdays);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'F':
                    characterLiteral = selectNumberRule(8, length2);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'G':
                    characterLiteral = new TextField(0, eras);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'H':
                    characterLiteral = selectNumberRule(11, length2);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'K':
                    characterLiteral = selectNumberRule(10, length2);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'M':
                    characterLiteral = length2 >= 4 ? new TextField(2, months) : length2 == 3 ? new TextField(2, shortMonths) : length2 == 2 ? TwoDigitMonthField.INSTANCE : UnpaddedMonthField.INSTANCE;
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'S':
                    characterLiteral = selectNumberRule(14, length2);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'W':
                    characterLiteral = selectNumberRule(4, length2);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'Z':
                    characterLiteral = length2 == i ? TimeZoneNumberRule.INSTANCE_NO_COLON : TimeZoneNumberRule.INSTANCE_COLON;
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'a':
                    characterLiteral = new TextField(9, amPmStrings);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'd':
                    characterLiteral = selectNumberRule(5, length2);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'h':
                    characterLiteral = new TwelveHourField(selectNumberRule(10, length2));
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'k':
                    characterLiteral = new TwentyFourHourField(selectNumberRule(11, length2));
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'm':
                    characterLiteral = selectNumberRule(12, length2);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 's':
                    characterLiteral = selectNumberRule(13, length2);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case 'w':
                    characterLiteral = selectNumberRule(3, length2);
                    i = 1;
                    arrayList.add(characterLiteral);
                    i3 = i4 + 1;
                    i2 = 0;
                case BuildConfig.VERSION_CODE /* 121 */:
                    if (length2 >= 4) {
                        i = 1;
                        characterLiteral = selectNumberRule(1, length2);
                        arrayList.add(characterLiteral);
                        i3 = i4 + 1;
                        i2 = 0;
                    } else {
                        characterLiteral = TwoDigitYearField.INSTANCE;
                        i = 1;
                        arrayList.add(characterLiteral);
                        i3 = i4 + 1;
                        i2 = 0;
                    }
                case 'z':
                    if (length2 >= 4) {
                        characterLiteral = new TimeZoneNameRule(this.mTimeZone, this.mTimeZoneForced, this.mLocale, i);
                        arrayList.add(characterLiteral);
                        i3 = i4 + 1;
                        i2 = 0;
                    } else {
                        characterLiteral = new TimeZoneNameRule(this.mTimeZone, this.mTimeZoneForced, this.mLocale, 0);
                        i = 1;
                        arrayList.add(characterLiteral);
                        i3 = i4 + 1;
                        i2 = 0;
                    }
                default:
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("Illegal pattern component: ");
                    stringBuffer.append(parseToken);
                    throw new IllegalArgumentException(stringBuffer.toString());
            }
        }
        return arrayList;
    }

    protected String parseToken(String str, int[] iArr) {
        StringBuffer stringBuffer = new StringBuffer();
        int i = iArr[0];
        int length = str.length();
        char charAt = str.charAt(i);
        if ((charAt >= 'A' && charAt <= 'Z') || (charAt >= 'a' && charAt <= 'z')) {
            stringBuffer.append(charAt);
            while (true) {
                int i2 = i + 1;
                if (i2 >= length || str.charAt(i2) != charAt) {
                    break;
                }
                stringBuffer.append(charAt);
                i = i2;
            }
        } else {
            stringBuffer.append('\'');
            boolean z = false;
            while (i < length) {
                char charAt2 = str.charAt(i);
                if (charAt2 != '\'') {
                    if (!z && ((charAt2 >= 'A' && charAt2 <= 'Z') || (charAt2 >= 'a' && charAt2 <= 'z'))) {
                        i--;
                        break;
                    }
                    stringBuffer.append(charAt2);
                } else {
                    int i3 = i + 1;
                    if (i3 >= length || str.charAt(i3) != '\'') {
                        z = !z;
                    } else {
                        stringBuffer.append(charAt2);
                        i = i3;
                    }
                }
                i++;
            }
        }
        iArr[0] = i;
        return stringBuffer.toString();
    }

    protected NumberRule selectNumberRule(int i, int i2) {
        switch (i2) {
            case 1:
                return new UnpaddedNumberField(i);
            case 2:
                return new TwoDigitNumberField(i);
            default:
                return new PaddedNumberField(i, i2);
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("FastDateFormat[");
        stringBuffer.append(this.mPattern);
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
