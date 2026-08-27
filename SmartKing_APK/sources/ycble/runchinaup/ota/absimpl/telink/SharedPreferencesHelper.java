package ycble.runchinaup.ota.absimpl.telink;

import android.content.Context;

/* loaded from: classes2.dex */
class SharedPreferencesHelper {

    /* loaded from: classes2.dex */
    public final class Keys {
        private static final long DEFAULT_OTA_START_DELAY_SECOND = 0;
        private static final long DEFAULT_PKT_DELAY_MILL_SECOND = 20;
        private static final long DEFAULT_SCAN_PERIOD_SECOND = 10;
        public static final String IS_NEED_PAIR = "com.android.rcc.IS_NEED_PAIR";
        public static final String IS_READ_SUPPORT = "com.android.rcc.IS_READ_SUPPORT";
        public static final String OTA_DELAY = "com.android.rcc.OTA_DELAY";
        public static final String OTA_START_DELAY = "com.android.rcc.OTA_START_DELAY";
        public static final String SCAN_PERIOD = "com.android.rcc.ScanPeriod";
        public static final String SHARED_NAME = "com.android.rcc.Telink_OTA";

        public Keys() {
        }
    }

    SharedPreferencesHelper() {
    }

    public static long getOTAStartDelay(Context context) {
        return context.getSharedPreferences(Keys.SHARED_NAME, 0).getLong(Keys.OTA_START_DELAY, 0L);
    }

    public static boolean getPairInfo(Context context) {
        return context.getSharedPreferences(Keys.SHARED_NAME, 0).getBoolean(Keys.IS_NEED_PAIR, false);
    }

    public static long getPktDelay(Context context) {
        return context.getSharedPreferences(Keys.SHARED_NAME, 0).getLong(Keys.OTA_DELAY, 20L);
    }

    public static boolean getReadSupport(Context context) {
        return context.getSharedPreferences(Keys.SHARED_NAME, 0).getBoolean(Keys.IS_READ_SUPPORT, true);
    }

    public static long getScanPeriod(Context context) {
        return context.getSharedPreferences(Keys.SHARED_NAME, 0).getLong(Keys.SCAN_PERIOD, 10L);
    }

    public static void saveOTAStartDelay(Context context, long j) {
        context.getSharedPreferences(Keys.SHARED_NAME, 0).edit().putLong(Keys.OTA_START_DELAY, j).apply();
    }

    public static void savePairInfo(Context context, boolean z) {
        context.getSharedPreferences(Keys.SHARED_NAME, 0).edit().putBoolean(Keys.IS_NEED_PAIR, z).apply();
    }

    public static void savePktDelay(Context context, long j) {
        context.getSharedPreferences(Keys.SHARED_NAME, 0).edit().putLong(Keys.OTA_DELAY, j).apply();
    }

    public static void saveReadSupport(Context context, boolean z) {
        context.getSharedPreferences(Keys.SHARED_NAME, 0).edit().putBoolean(Keys.IS_READ_SUPPORT, z).apply();
    }

    public static void saveScanPeriod(Context context, long j) {
        context.getSharedPreferences(Keys.SHARED_NAME, 0).edit().putLong(Keys.SCAN_PERIOD, j).apply();
    }
}
