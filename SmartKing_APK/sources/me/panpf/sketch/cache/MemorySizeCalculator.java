package me.panpf.sketch.cache;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import me.panpf.sketch.SLog;

/* loaded from: classes2.dex */
public class MemorySizeCalculator {
    static final int BITMAP_POOL_TARGET_SCREENS = 3;
    static final int BYTES_PER_ARGB_8888_PIXEL = 4;
    static final float LOW_MEMORY_MAX_SIZE_MULTIPLIER = 0.33f;
    static final float MAX_SIZE_MULTIPLIER = 0.4f;
    static final int MEMORY_CACHE_TARGET_SCREENS = 3;
    private static final String NAME = "MemorySizeCalculator";
    private final int bitmapPoolSize;
    private final int memoryCacheSize;

    public MemorySizeCalculator(Context context) {
        Context applicationContext = context.getApplicationContext();
        ActivityManager activityManager = (ActivityManager) applicationContext.getSystemService("activity");
        DisplayMetrics displayMetrics = applicationContext.getResources().getDisplayMetrics();
        int maxSize = getMaxSize(activityManager);
        int i = displayMetrics.widthPixels * displayMetrics.heightPixels * 4 * 3;
        int i2 = i + i;
        if (i2 <= maxSize) {
            this.memoryCacheSize = i;
            this.bitmapPoolSize = BitmapPoolUtils.sdkSupportInBitmap() ? i : 0;
        } else {
            int round = Math.round(maxSize / 6.0f) * 3;
            this.memoryCacheSize = round;
            this.bitmapPoolSize = BitmapPoolUtils.sdkSupportInBitmap() ? round : 0;
        }
        if (SLog.isLoggable(131074)) {
            Object[] objArr = new Object[6];
            objArr[0] = toMb(applicationContext, this.memoryCacheSize);
            objArr[1] = toMb(applicationContext, this.bitmapPoolSize);
            objArr[2] = Boolean.valueOf(i2 > maxSize);
            objArr[3] = toMb(applicationContext, maxSize);
            objArr[4] = Integer.valueOf(activityManager.getMemoryClass());
            objArr[5] = Boolean.valueOf(isLowMemoryDevice(activityManager));
            SLog.d(NAME, "Calculated memory cache size: %s pool size: %s memory class limited? %s max size: %s memoryClass: %d isLowMemoryDevice: %s", objArr);
        }
    }

    private static int getMaxSize(ActivityManager activityManager) {
        return Math.round(activityManager.getMemoryClass() * 1024 * 1024 * (isLowMemoryDevice(activityManager) ? LOW_MEMORY_MAX_SIZE_MULTIPLIER : MAX_SIZE_MULTIPLIER));
    }

    @TargetApi(19)
    private static boolean isLowMemoryDevice(ActivityManager activityManager) {
        int i = Build.VERSION.SDK_INT;
        return i < 11 || (i >= 19 && activityManager.isLowRamDevice());
    }

    private static String toMb(Context context, int i) {
        return Formatter.formatFileSize(context, i);
    }

    public int getBitmapPoolSize() {
        return this.bitmapPoolSize;
    }

    public int getMemoryCacheSize() {
        return this.memoryCacheSize;
    }
}
