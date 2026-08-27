package me.panpf.sketch.decode;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import me.panpf.sketch.ErrorTracker;
import me.panpf.sketch.SLog;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.datasource.DiskCacheDataSource;
import me.panpf.sketch.datasource.FileDataSource;
import me.panpf.sketch.request.LoadRequest;
import me.panpf.sketch.request.MaxSize;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ImageDecodeUtils {
    public static Bitmap decodeBitmap(DataSource dataSource, BitmapFactory.Options options) throws IOException {
        InputStream inputStream;
        try {
            inputStream = dataSource.getInputStream();
            try {
                Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
                SketchUtils.close(inputStream);
                return decodeStream;
            } catch (Throwable th) {
                th = th;
                SketchUtils.close(inputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void decodeError(LoadRequest loadRequest, DataSource dataSource, String str, String str2, Throwable th) {
        if (th != null) {
            SLog.e(str, Log.getStackTraceString(th));
        }
        if (dataSource instanceof DiskCacheDataSource) {
            DiskCache.Entry diskCacheEntry = ((DiskCacheDataSource) dataSource).getDiskCacheEntry();
            File file = diskCacheEntry.getFile();
            if (diskCacheEntry.delete()) {
                SLog.e(str, "Decode failed. %s. Disk cache deleted. fileLength=%d. %s", str2, Long.valueOf(file.length()), loadRequest.getKey(), th);
                return;
            } else {
                SLog.e(str, "Decode failed. %s. Disk cache can not be deleted. fileLength=%d. %s", str2, Long.valueOf(file.length()), loadRequest.getKey());
                return;
            }
        }
        if (!(dataSource instanceof FileDataSource)) {
            SLog.e(str, "Decode failed. %s. %s", str2, loadRequest.getUri());
            return;
        }
        File file2 = ((FileDataSource) dataSource).getFile(null, null);
        Object[] objArr = new Object[4];
        objArr[0] = str2;
        objArr[1] = file2.getPath();
        objArr[2] = Long.valueOf(file2.exists() ? file2.length() : -1L);
        objArr[3] = loadRequest.getKey();
        SLog.e(str, "Decode failed. %s. filePath=%s, fileLength=%d. %s", objArr);
    }

    @SuppressLint({"ObsoleteSdkInt"})
    public static Bitmap decodeRegionBitmap(DataSource dataSource, Rect rect, BitmapFactory.Options options) {
        if (Build.VERSION.SDK_INT < 10) {
            return null;
        }
        try {
            InputStream inputStream = dataSource.getInputStream();
            try {
                BitmapRegionDecoder newInstance = BitmapRegionDecoder.newInstance(inputStream, false);
                SketchUtils.close(inputStream);
                Bitmap decodeRegion = newInstance.decodeRegion(rect, options);
                newInstance.recycle();
                return decodeRegion;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                SketchUtils.close(inputStream);
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void decodeSuccess(@NonNull Bitmap bitmap, int i, int i2, int i3, LoadRequest loadRequest, String str) {
        if (SLog.isLoggable(65538)) {
            if (loadRequest.getOptions().getMaxSize() == null) {
                SLog.d(str, "Decode bitmap. bitmapSize=%dx%d. %s", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), loadRequest.getKey());
            } else {
                MaxSize maxSize = loadRequest.getOptions().getMaxSize();
                SLog.d(str, "Decode bitmap. originalSize=%dx%d, targetSize=%dx%d, targetSizeScale=%s, inSampleSize=%d, finalSize=%dx%d. %s", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(maxSize.getWidth()), Integer.valueOf(maxSize.getHeight()), Float.valueOf(loadRequest.getConfiguration().getSizeCalculator().getTargetSizeScale()), Integer.valueOf(i3), Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), loadRequest.getKey());
            }
        }
    }

    @TargetApi(11)
    public static boolean isInBitmapDecodeError(Throwable th, BitmapFactory.Options options, boolean z) {
        String message;
        if (z) {
            if (!BitmapPoolUtils.sdkSupportInBitmapForRegionDecoder()) {
                return false;
            }
        } else if (!BitmapPoolUtils.sdkSupportInBitmap()) {
            return false;
        }
        if (!(th instanceof IllegalArgumentException) || options.inBitmap == null || (message = th.getMessage()) == null) {
            return false;
        }
        return message.equals("Problem decoding into existing bitmap") || message.contains("bitmap");
    }

    public static boolean isSrcRectDecodeError(Throwable th, int i, int i2, Rect rect) {
        if (!SketchUtils.sdkSupportBitmapRegionDecoder() || !(th instanceof IllegalArgumentException)) {
            return false;
        }
        if (rect.left < i || rect.top < i2 || rect.right > i || rect.bottom > i2) {
            return true;
        }
        String message = th.getMessage();
        if (message != null) {
            return message.equals("rectangle is outside the image srcRect") || message.contains("srcRect");
        }
        return false;
    }

    @TargetApi(11)
    public static void recycleInBitmapOnDecodeError(ErrorTracker errorTracker, BitmapPool bitmapPool, String str, int i, int i2, String str2, Throwable th, BitmapFactory.Options options, boolean z) {
        if (z) {
            if (!BitmapPoolUtils.sdkSupportInBitmapForRegionDecoder()) {
                return;
            }
        } else if (!BitmapPoolUtils.sdkSupportInBitmap()) {
            return;
        }
        errorTracker.onInBitmapDecodeError(str, i, i2, str2, th, options.inSampleSize, options.inBitmap);
        BitmapPoolUtils.freeBitmapToPool(options.inBitmap, bitmapPool);
        options.inBitmap = null;
    }
}
