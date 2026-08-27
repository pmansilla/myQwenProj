package me.panpf.sketch.cache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import me.panpf.sketch.SLog;
import me.panpf.sketch.decode.ImageType;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class BitmapPoolUtils {
    private static final String NAME = "BitmapPoolUtils";

    public static boolean freeBitmapToPool(Bitmap bitmap, BitmapPool bitmapPool) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        boolean put = bitmapPool.put(bitmap);
        if (!put) {
            if (SLog.isLoggable(131074)) {
                StackTraceElement[] stackTrace = new Exception().getStackTrace();
                StackTraceElement stackTraceElement = stackTrace.length > 1 ? stackTrace[1] : stackTrace[0];
                SLog.d(NAME, "Recycle bitmap. info:%dx%d,%s,%s - %s.%s:%d", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), bitmap.getConfig(), SketchUtils.toHexString(bitmap), stackTraceElement.getClassName(), stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber()));
            }
            bitmap.recycle();
        } else if (SLog.isLoggable(131074)) {
            StackTraceElement[] stackTrace2 = new Exception().getStackTrace();
            StackTraceElement stackTraceElement2 = stackTrace2.length > 1 ? stackTrace2[1] : stackTrace2[0];
            SLog.d(NAME, "Put to bitmap pool. info:%dx%d,%s,%s - %s.%s:%d", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), bitmap.getConfig(), SketchUtils.toHexString(bitmap), stackTraceElement2.getClassName(), stackTraceElement2.getMethodName(), Integer.valueOf(stackTraceElement2.getLineNumber()));
        }
        return put;
    }

    public static boolean freeBitmapToPoolForRegionDecoder(Bitmap bitmap, BitmapPool bitmapPool) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        boolean z = sdkSupportInBitmapForRegionDecoder() && bitmapPool.put(bitmap);
        if (!z) {
            if (SLog.isLoggable(131074)) {
                StackTraceElement[] stackTrace = new Exception().getStackTrace();
                StackTraceElement stackTraceElement = stackTrace.length > 1 ? stackTrace[1] : stackTrace[0];
                SLog.d(NAME, "Recycle bitmap. info:%dx%d,%s,%s - %s.%s:%d", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), bitmap.getConfig(), SketchUtils.toHexString(bitmap), stackTraceElement.getClassName(), stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber()));
            }
            bitmap.recycle();
        } else if (SLog.isLoggable(131074)) {
            StackTraceElement[] stackTrace2 = new Exception().getStackTrace();
            StackTraceElement stackTraceElement2 = stackTrace2.length > 1 ? stackTrace2[1] : stackTrace2[0];
            SLog.d(NAME, "Put to bitmap pool. info:%dx%d,%s,%s - %s.%s:%d", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), bitmap.getConfig(), SketchUtils.toHexString(bitmap), stackTraceElement2.getClassName(), stackTraceElement2.getMethodName(), Integer.valueOf(stackTraceElement2.getLineNumber()));
        }
        return z;
    }

    public static boolean sdkSupportInBitmap() {
        return Build.VERSION.SDK_INT >= 11;
    }

    public static boolean sdkSupportInBitmapForRegionDecoder() {
        return Build.VERSION.SDK_INT >= 16;
    }

    @TargetApi(11)
    public static boolean setInBitmapFromPool(BitmapFactory.Options options, int i, int i2, String str, BitmapPool bitmapPool) {
        if (!sdkSupportInBitmap()) {
            return false;
        }
        if (i == 0 || i2 == 0) {
            SLog.e(NAME, "outWidth or ourHeight is 0");
            return false;
        }
        if (TextUtils.isEmpty(str)) {
            SLog.e(NAME, "outMimeType is empty");
            return false;
        }
        if (options.inSampleSize <= 0) {
            options.inSampleSize = 1;
        }
        int i3 = options.inSampleSize;
        ImageType valueOfMimeType = ImageType.valueOfMimeType(str);
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= 19) {
            float f = i3;
            bitmap = bitmapPool.get(SketchUtils.ceil(i, f), SketchUtils.ceil(i2, f), options.inPreferredConfig);
        } else if (Build.VERSION.SDK_INT >= 11 && i3 == 1 && (valueOfMimeType == ImageType.JPEG || valueOfMimeType == ImageType.PNG)) {
            bitmap = bitmapPool.get(i, i2, options.inPreferredConfig);
        }
        if (bitmap != null && SLog.isLoggable(131074)) {
            SLog.d(NAME, "setInBitmapFromPool. options=%dx%d,%s,%d,%d. inBitmap=%s,%d", Integer.valueOf(i), Integer.valueOf(i2), options.inPreferredConfig, Integer.valueOf(i3), Integer.valueOf(SketchUtils.computeByteCount(i, i2, options.inPreferredConfig)), Integer.toHexString(bitmap.hashCode()), Integer.valueOf(SketchUtils.getByteCount(bitmap)));
        }
        options.inBitmap = bitmap;
        options.inMutable = true;
        return bitmap != null;
    }

    @TargetApi(16)
    public static boolean setInBitmapFromPoolForRegionDecoder(BitmapFactory.Options options, Rect rect, BitmapPool bitmapPool) {
        if (!sdkSupportInBitmapForRegionDecoder()) {
            return false;
        }
        int i = options.inSampleSize >= 1 ? options.inSampleSize : 1;
        Bitmap.Config config = options.inPreferredConfig;
        float f = i;
        int ceil = SketchUtils.ceil(rect.width(), f);
        int ceil2 = SketchUtils.ceil(rect.height(), f);
        Bitmap bitmap = bitmapPool.get(ceil, ceil2, config);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(ceil, ceil2, config);
        } else if (SLog.isLoggable(131074)) {
            SLog.d(NAME, "setInBitmapFromPoolForRegionDecoder. options=%dx%d,%s,%d,%d. inBitmap=%s,%d", Integer.valueOf(ceil), Integer.valueOf(ceil2), config, Integer.valueOf(i), Integer.valueOf(SketchUtils.computeByteCount(ceil, ceil2, config)), Integer.toHexString(bitmap.hashCode()), Integer.valueOf(SketchUtils.getByteCount(bitmap)));
        }
        options.inBitmap = bitmap;
        return bitmap != null;
    }
}
