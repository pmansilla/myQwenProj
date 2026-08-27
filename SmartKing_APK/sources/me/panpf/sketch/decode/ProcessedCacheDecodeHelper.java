package me.panpf.sketch.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.Locale;
import me.panpf.sketch.ErrorTracker;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.datasource.DiskCacheDataSource;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.LoadRequest;
import me.panpf.sketch.uri.GetDataSourceException;

/* loaded from: classes2.dex */
public class ProcessedCacheDecodeHelper extends DecodeHelper {
    private static final String NAME = "ProcessedCacheDecodeHelper";

    @Override // me.panpf.sketch.decode.DecodeHelper
    @NonNull
    public DecodeResult decode(@NonNull LoadRequest loadRequest, @NonNull DataSource dataSource, @Nullable ImageType imageType, @NonNull BitmapFactory.Options options, @NonNull BitmapFactory.Options options2, int i) throws DecodeException {
        Bitmap decodeBitmap;
        ImageAttrs imageAttrs;
        options2.inSampleSize = 1;
        if (BitmapPoolUtils.sdkSupportInBitmap() && !loadRequest.getOptions().isBitmapPoolDisabled()) {
            BitmapPoolUtils.setInBitmapFromPool(options2, options.outWidth, options.outHeight, options.outMimeType, loadRequest.getConfiguration().getBitmapPool());
        }
        try {
            decodeBitmap = ImageDecodeUtils.decodeBitmap(dataSource, options2);
        } catch (Throwable th) {
            ErrorTracker errorTracker = loadRequest.getConfiguration().getErrorTracker();
            BitmapPool bitmapPool = loadRequest.getConfiguration().getBitmapPool();
            if (!ImageDecodeUtils.isInBitmapDecodeError(th, options2, false)) {
                errorTracker.onDecodeNormalImageError(th, loadRequest, options.outWidth, options.outHeight, options.outMimeType);
                throw new DecodeException(th, ErrorCause.DECODE_UNKNOWN_EXCEPTION);
            }
            ImageDecodeUtils.recycleInBitmapOnDecodeError(errorTracker, bitmapPool, loadRequest.getUri(), options.outWidth, options.outHeight, options.outMimeType, th, options2, false);
            try {
                decodeBitmap = ImageDecodeUtils.decodeBitmap(dataSource, options2);
            } catch (Throwable th2) {
                errorTracker.onDecodeNormalImageError(th2, loadRequest, options.outWidth, options.outHeight, options.outMimeType);
                throw new DecodeException("InBitmap retry", th, ErrorCause.DECODE_UNKNOWN_EXCEPTION);
            }
        }
        Bitmap bitmap = decodeBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            ImageDecodeUtils.decodeError(loadRequest, dataSource, NAME, "Bitmap invalid", null);
            throw new DecodeException("Bitmap invalid", ErrorCause.DECODE_RESULT_BITMAP_INVALID);
        }
        if (bitmap.getWidth() <= 1 || bitmap.getHeight() <= 1) {
            String format = String.format(Locale.US, "Bitmap width or height less than or equal to 1px. imageSize: %dx%d. bitmapSize: %dx%d", Integer.valueOf(options.outWidth), Integer.valueOf(options.outHeight), Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()));
            ImageDecodeUtils.decodeError(loadRequest, dataSource, NAME, format, null);
            bitmap.recycle();
            throw new DecodeException(format, ErrorCause.DECODE_RESULT_BITMAP_SIZE_INVALID);
        }
        try {
            DataSource dataSource2 = loadRequest.getDataSource();
            BitmapFactory.Options options3 = new BitmapFactory.Options();
            options3.inJustDecodeBounds = true;
            try {
                ImageDecodeUtils.decodeBitmap(dataSource2, options3);
            } catch (Throwable th3) {
                th3.printStackTrace();
            }
            ImageOrientationCorrector orientationCorrector = loadRequest.getConfiguration().getOrientationCorrector();
            if (TextUtils.isEmpty(options3.outMimeType)) {
                imageAttrs = new ImageAttrs(options.outMimeType, options.outWidth, options.outHeight, i);
            } else {
                imageAttrs = new ImageAttrs(options3.outMimeType, options3.outWidth, options3.outHeight, loadRequest.getOptions().isCorrectImageOrientationDisabled() ? 0 : orientationCorrector.readExifOrientation(options3.outMimeType, dataSource2));
            }
            ImageAttrs imageAttrs2 = imageAttrs;
            orientationCorrector.rotateSize(imageAttrs2, imageAttrs2.getExifOrientation());
            ImageDecodeUtils.decodeSuccess(bitmap, options.outWidth, options.outHeight, options2.inSampleSize, loadRequest, NAME);
            return new BitmapDecodeResult(imageAttrs2, bitmap).setBanProcess(true);
        } catch (GetDataSourceException e) {
            ImageDecodeUtils.decodeError(loadRequest, null, NAME, "Unable create DataSource", e);
            throw new DecodeException(e, ErrorCause.DECODE_UNABLE_CREATE_DATA_SOURCE);
        }
    }

    @Override // me.panpf.sketch.decode.DecodeHelper
    public boolean match(@NonNull LoadRequest loadRequest, @NonNull DataSource dataSource, @Nullable ImageType imageType, @NonNull BitmapFactory.Options options) {
        return (dataSource instanceof DiskCacheDataSource) && ((DiskCacheDataSource) dataSource).isFromProcessedCache();
    }
}
