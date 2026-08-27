package me.panpf.sketch.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Locale;
import me.panpf.sketch.ErrorTracker;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.LoadRequest;
import me.panpf.sketch.request.MaxSize;

/* loaded from: classes2.dex */
public class NormalDecodeHelper extends DecodeHelper {
    private static final String NAME = "NormalDecodeHelper";

    @Override // me.panpf.sketch.decode.DecodeHelper
    @NonNull
    public DecodeResult decode(@NonNull LoadRequest loadRequest, @NonNull DataSource dataSource, @Nullable ImageType imageType, @NonNull BitmapFactory.Options options, @NonNull BitmapFactory.Options options2, int i) throws DecodeException {
        char c;
        Bitmap decodeBitmap;
        ImageOrientationCorrector orientationCorrector = loadRequest.getConfiguration().getOrientationCorrector();
        orientationCorrector.rotateSize(options, i);
        MaxSize maxSize = loadRequest.getOptions().getMaxSize();
        if (maxSize != null) {
            ImageSizeCalculator sizeCalculator = loadRequest.getConfiguration().getSizeCalculator();
            options2.inSampleSize = sizeCalculator.calculateInSampleSize(options.outWidth, options.outHeight, maxSize.getWidth(), maxSize.getHeight(), sizeCalculator.canUseSmallerThumbnails(loadRequest, imageType));
        }
        if (BitmapPoolUtils.sdkSupportInBitmap() && !loadRequest.getOptions().isBitmapPoolDisabled()) {
            BitmapPoolUtils.setInBitmapFromPool(options2, options.outWidth, options.outHeight, options.outMimeType, loadRequest.getConfiguration().getBitmapPool());
        }
        try {
            decodeBitmap = ImageDecodeUtils.decodeBitmap(dataSource, options2);
            c = 0;
        } catch (Throwable th) {
            ErrorTracker errorTracker = loadRequest.getConfiguration().getErrorTracker();
            BitmapPool bitmapPool = loadRequest.getConfiguration().getBitmapPool();
            if (!ImageDecodeUtils.isInBitmapDecodeError(th, options2, false)) {
                errorTracker.onDecodeNormalImageError(th, loadRequest, options.outWidth, options.outHeight, options.outMimeType);
                throw new DecodeException(th, ErrorCause.DECODE_UNKNOWN_EXCEPTION);
            }
            c = 0;
            ImageDecodeUtils.recycleInBitmapOnDecodeError(errorTracker, bitmapPool, loadRequest.getUri(), options.outWidth, options.outHeight, options.outMimeType, th, options2, false);
            try {
                decodeBitmap = ImageDecodeUtils.decodeBitmap(dataSource, options2);
            } catch (Throwable th2) {
                errorTracker.onDecodeNormalImageError(th2, loadRequest, options.outWidth, options.outHeight, options.outMimeType);
                throw new DecodeException("InBitmap retry", th, ErrorCause.DECODE_UNKNOWN_EXCEPTION);
            }
        }
        if (decodeBitmap == null || decodeBitmap.isRecycled()) {
            ImageDecodeUtils.decodeError(loadRequest, dataSource, NAME, "Bitmap invalid", null);
            throw new DecodeException("Bitmap invalid", ErrorCause.DECODE_RESULT_BITMAP_INVALID);
        }
        if (decodeBitmap.getWidth() > 1 && decodeBitmap.getHeight() > 1) {
            BitmapDecodeResult processed = new BitmapDecodeResult(new ImageAttrs(options.outMimeType, options.outWidth, options.outHeight, i), decodeBitmap).setProcessed(loadRequest.getConfiguration().getProcessedImageCache().canUseCacheProcessedImageInDisk(options2.inSampleSize));
            try {
                correctOrientation(orientationCorrector, processed, i, loadRequest);
                ImageDecodeUtils.decodeSuccess(decodeBitmap, options.outWidth, options.outHeight, options2.inSampleSize, loadRequest, NAME);
                return processed;
            } catch (CorrectOrientationException e) {
                throw new DecodeException(e, ErrorCause.DECODE_CORRECT_ORIENTATION_FAIL);
            }
        }
        Locale locale = Locale.US;
        Object[] objArr = new Object[4];
        objArr[c] = Integer.valueOf(options.outWidth);
        objArr[1] = Integer.valueOf(options.outHeight);
        objArr[2] = Integer.valueOf(decodeBitmap.getWidth());
        objArr[3] = Integer.valueOf(decodeBitmap.getHeight());
        String format = String.format(locale, "Bitmap width or height less than or equal to 1px. imageSize: %dx%d. bitmapSize: %dx%d", objArr);
        ImageDecodeUtils.decodeError(loadRequest, dataSource, NAME, format, null);
        decodeBitmap.recycle();
        throw new DecodeException(format, ErrorCause.DECODE_RESULT_BITMAP_SIZE_INVALID);
    }

    @Override // me.panpf.sketch.decode.DecodeHelper
    public boolean match(@NonNull LoadRequest loadRequest, @NonNull DataSource dataSource, @Nullable ImageType imageType, @NonNull BitmapFactory.Options options) {
        return true;
    }
}
