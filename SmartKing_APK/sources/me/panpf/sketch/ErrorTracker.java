package me.panpf.sketch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import me.panpf.sketch.drawable.SketchDrawable;
import me.panpf.sketch.process.ImageProcessor;
import me.panpf.sketch.request.DisplayRequest;
import me.panpf.sketch.request.DownloadRequest;
import me.panpf.sketch.request.LoadRequest;
import me.panpf.sketch.util.SketchUtils;
import me.panpf.sketch.zoom.block.Block;

/* loaded from: classes2.dex */
public class ErrorTracker {
    private static final String NAME = "ErrorTracker";
    private Context context;

    public ErrorTracker(Context context) {
        this.context = context.getApplicationContext();
    }

    public void onBitmapRecycledOnDisplay(@NonNull DisplayRequest displayRequest, @NonNull SketchDrawable sketchDrawable) {
        SLog.e(NAME, "onBitmapRecycledOnDisplay. imageUri=%s, drawable=%s", displayRequest.getUri(), sketchDrawable.getInfo());
    }

    public void onBlockSortError(@NonNull IllegalArgumentException illegalArgumentException, @NonNull List<Block> list, boolean z) {
        SLog.e(NAME, "onBlockSortError. %s%s", z ? "useLegacyMergeSort. " : "", SketchUtils.blockListToString(list));
    }

    public void onDecodeGifImageError(@NonNull Throwable th, @NonNull LoadRequest loadRequest, int i, int i2, @NonNull String str) {
        SLog.e(NAME, "onDecodeGifImageError. outWidth=%d, outHeight=%d + outMimeType=%s. %s", Integer.valueOf(i), Integer.valueOf(i2), str, loadRequest.getKey());
    }

    public void onDecodeNormalImageError(@NonNull Throwable th, @NonNull LoadRequest loadRequest, int i, int i2, @NonNull String str) {
        if (th instanceof OutOfMemoryError) {
            SLog.e(NAME, "OutOfMemoryError. appMemoryInfo: maxMemory=%s, freeMemory=%s, totalMemory=%s", Formatter.formatFileSize(this.context, Runtime.getRuntime().maxMemory()), Formatter.formatFileSize(this.context, Runtime.getRuntime().freeMemory()), Formatter.formatFileSize(this.context, Runtime.getRuntime().totalMemory()));
        }
        SLog.e(NAME, "onDecodeNormalImageError. outWidth=%d, outHeight=%d, outMimeType=%s. %s", Integer.valueOf(i), Integer.valueOf(i2), str, loadRequest.getKey());
    }

    public void onDecodeRegionError(@NonNull String str, int i, int i2, @NonNull String str2, @NonNull Throwable th, @NonNull Rect rect, int i3) {
        SLog.e(NAME, "onDecodeRegionError. imageUri=%s, imageSize=%dx%d, imageMimeType= %s, srcRect=%s, inSampleSize=%d", str, Integer.valueOf(i), Integer.valueOf(i2), str2, rect.toString(), Integer.valueOf(i3));
    }

    public void onDownloadError(@NonNull DownloadRequest downloadRequest, @NonNull Throwable th) {
    }

    public void onInBitmapDecodeError(@NonNull String str, int i, int i2, @NonNull String str2, @NonNull Throwable th, int i3, @NonNull Bitmap bitmap) {
        SLog.e(NAME, "onInBitmapException. imageUri=%s, imageSize=%dx%d, imageMimeType= %s, inSampleSize=%d, inBitmapSize=%dx%d, inBitmapByteCount=%d", str, Integer.valueOf(i), Integer.valueOf(i2), str2, Integer.valueOf(i3), Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), Integer.valueOf(SketchUtils.getByteCount(bitmap)));
    }

    public void onInstallDiskCacheError(@NonNull Exception exc, @NonNull File file) {
        SLog.e(NAME, "onInstallDiskCacheError. %s: %s. SDCardState: %s. cacheDir: %s", exc.getClass().getSimpleName(), exc.getMessage(), Environment.getExternalStorageState(), file.getPath());
    }

    public void onNotFoundGifSoError(@NonNull Throwable th) {
        SLog.e(NAME, "Didn't find “libpl_droidsonroids_gif.so” file, unable decode the GIF images. Please go to “https://github.com/panpf/sketch” find how to import the sketch-gif library");
        SLog.e(NAME, "abis=%s", Build.VERSION.SDK_INT >= 21 ? Arrays.toString(Build.SUPPORTED_ABIS) : Arrays.toString(new String[]{Build.CPU_ABI, Build.CPU_ABI2}));
    }

    public void onProcessImageError(@NonNull Throwable th, @NonNull String str, @NonNull ImageProcessor imageProcessor) {
        if (th instanceof OutOfMemoryError) {
            SLog.d(NAME, "OutOfMemoryError. appMemoryInfo: maxMemory=%s, freeMemory=%s, totalMemory=%s", Formatter.formatFileSize(this.context, Runtime.getRuntime().maxMemory()), Formatter.formatFileSize(this.context, Runtime.getRuntime().freeMemory()), Formatter.formatFileSize(this.context, Runtime.getRuntime().totalMemory()));
        }
        SLog.e(NAME, "onProcessImageError. imageUri: %s. processor: %s", str, imageProcessor.toString());
    }

    @NonNull
    public String toString() {
        return NAME;
    }
}
