package me.panpf.sketch.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Locale;
import me.panpf.sketch.ErrorTracker;
import me.panpf.sketch.SLog;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.decode.ResizeCalculator;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.LoadOptions;
import me.panpf.sketch.request.LoadRequest;
import me.panpf.sketch.request.Resize;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ThumbnailModeDecodeHelper extends DecodeHelper {
    private static final String NAME = "ThumbnailModeDecodeHelper";

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r15v1 */
    /* JADX WARN: Type inference failed for: r15v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r15v3 */
    @Override // me.panpf.sketch.decode.DecodeHelper
    @NonNull
    public DecodeResult decode(@NonNull LoadRequest loadRequest, @NonNull DataSource dataSource, @Nullable ImageType imageType, @NonNull BitmapFactory.Options options, @NonNull BitmapFactory.Options options2, int i) throws DecodeException {
        ImageOrientationCorrector imageOrientationCorrector;
        ?? r15;
        Bitmap decodeRegionBitmap;
        ImageOrientationCorrector orientationCorrector = loadRequest.getConfiguration().getOrientationCorrector();
        orientationCorrector.rotateSize(options, i);
        if (Build.VERSION.SDK_INT >= 10 && !options2.inPreferQualityOverSpeed) {
            options2.inPreferQualityOverSpeed = true;
        }
        LoadOptions options3 = loadRequest.getOptions();
        Resize resize = options3.getResize();
        ResizeCalculator.Mapping calculator = loadRequest.getConfiguration().getResizeCalculator().calculator(options.outWidth, options.outHeight, resize.getWidth(), resize.getHeight(), resize.getScaleType(), false);
        ImageSizeCalculator sizeCalculator = loadRequest.getConfiguration().getSizeCalculator();
        options2.inSampleSize = sizeCalculator.calculateInSampleSize(calculator.srcRect.width(), calculator.srcRect.height(), resize.getWidth(), resize.getHeight(), sizeCalculator.canUseSmallerThumbnails(loadRequest, imageType));
        orientationCorrector.reverseRotate(calculator.srcRect, options.outWidth, options.outHeight, i);
        if (BitmapPoolUtils.sdkSupportInBitmapForRegionDecoder() && !options3.isBitmapPoolDisabled()) {
            BitmapPoolUtils.setInBitmapFromPoolForRegionDecoder(options2, calculator.srcRect, loadRequest.getConfiguration().getBitmapPool());
        }
        try {
            decodeRegionBitmap = ImageDecodeUtils.decodeRegionBitmap(dataSource, calculator.srcRect, options2);
            imageOrientationCorrector = orientationCorrector;
            r15 = 1;
        } catch (Throwable th) {
            ErrorTracker errorTracker = loadRequest.getConfiguration().getErrorTracker();
            BitmapPool bitmapPool = loadRequest.getConfiguration().getBitmapPool();
            if (!ImageDecodeUtils.isInBitmapDecodeError(th, options2, true)) {
                if (ImageDecodeUtils.isSrcRectDecodeError(th, options.outWidth, options.outHeight, calculator.srcRect)) {
                    errorTracker.onDecodeRegionError(loadRequest.getUri(), options.outWidth, options.outHeight, options.outMimeType, th, calculator.srcRect, options2.inSampleSize);
                    throw new DecodeException("Because srcRect", th, ErrorCause.DECODE_UNKNOWN_EXCEPTION);
                }
                errorTracker.onDecodeNormalImageError(th, loadRequest, options.outWidth, options.outHeight, options.outMimeType);
                throw new DecodeException(th, ErrorCause.DECODE_UNKNOWN_EXCEPTION);
            }
            imageOrientationCorrector = orientationCorrector;
            r15 = 1;
            ImageDecodeUtils.recycleInBitmapOnDecodeError(errorTracker, bitmapPool, loadRequest.getUri(), options.outWidth, options.outHeight, options.outMimeType, th, options2, true);
            try {
                decodeRegionBitmap = ImageDecodeUtils.decodeRegionBitmap(dataSource, calculator.srcRect, options2);
            } catch (Throwable th2) {
                errorTracker.onDecodeNormalImageError(th2, loadRequest, options.outWidth, options.outHeight, options.outMimeType);
                throw new DecodeException("InBitmap retry", th, ErrorCause.DECODE_UNKNOWN_EXCEPTION);
            }
        }
        if (decodeRegionBitmap == null || decodeRegionBitmap.isRecycled()) {
            ImageDecodeUtils.decodeError(loadRequest, dataSource, NAME, "Bitmap invalid", null);
            throw new DecodeException("Bitmap invalid", ErrorCause.DECODE_RESULT_BITMAP_INVALID);
        }
        if (decodeRegionBitmap.getWidth() > r15 && decodeRegionBitmap.getHeight() > r15) {
            BitmapDecodeResult processed = new BitmapDecodeResult(new ImageAttrs(options.outMimeType, options.outWidth, options.outHeight, i), decodeRegionBitmap).setProcessed((boolean) r15);
            try {
                correctOrientation(imageOrientationCorrector, processed, i, loadRequest);
                ImageDecodeUtils.decodeSuccess(decodeRegionBitmap, options.outWidth, options.outHeight, options2.inSampleSize, loadRequest, NAME);
                return processed;
            } catch (CorrectOrientationException e) {
                throw new DecodeException(e, ErrorCause.DECODE_CORRECT_ORIENTATION_FAIL);
            }
        }
        Locale locale = Locale.US;
        Object[] objArr = new Object[4];
        objArr[0] = Integer.valueOf(options.outWidth);
        objArr[r15] = Integer.valueOf(options.outHeight);
        objArr[2] = Integer.valueOf(decodeRegionBitmap.getWidth());
        objArr[3] = Integer.valueOf(decodeRegionBitmap.getHeight());
        String format = String.format(locale, "Bitmap width or height less than or equal to 1px. imageSize: %dx%d. bitmapSize: %dx%d", objArr);
        ImageDecodeUtils.decodeError(loadRequest, dataSource, NAME, format, null);
        decodeRegionBitmap.recycle();
        throw new DecodeException(format, ErrorCause.DECODE_RESULT_BITMAP_SIZE_INVALID);
    }

    @Override // me.panpf.sketch.decode.DecodeHelper
    public boolean match(@NonNull LoadRequest loadRequest, @NonNull DataSource dataSource, @Nullable ImageType imageType, @NonNull BitmapFactory.Options options) {
        LoadOptions options2 = loadRequest.getOptions();
        if (!options2.isThumbnailMode() || !SketchUtils.sdkSupportBitmapRegionDecoder() || !SketchUtils.formatSupportBitmapRegionDecoder(imageType)) {
            return false;
        }
        Resize resize = options2.getResize();
        if (resize != null) {
            return loadRequest.getConfiguration().getSizeCalculator().canUseThumbnailMode(options.outWidth, options.outHeight, resize.getWidth(), resize.getHeight());
        }
        SLog.e(NAME, "thumbnailMode need resize ");
        return false;
    }
}
