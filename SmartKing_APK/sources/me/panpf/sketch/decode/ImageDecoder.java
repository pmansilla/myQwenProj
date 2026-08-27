package me.panpf.sketch.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import me.panpf.sketch.SLog;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.LoadRequest;
import me.panpf.sketch.uri.GetDataSourceException;

/* loaded from: classes2.dex */
public class ImageDecoder {
    private static final String NAME = "ImageDecoder";
    private DecodeTimeAnalyze timeAnalyze = new DecodeTimeAnalyze();
    private List<DecodeHelper> decodeHelperList = new LinkedList();
    private List<ResultProcessor> resultProcessorList = new LinkedList();

    public ImageDecoder() {
        this.decodeHelperList.add(new ProcessedCacheDecodeHelper());
        this.decodeHelperList.add(new GifDecodeHelper());
        this.decodeHelperList.add(new ThumbnailModeDecodeHelper());
        this.decodeHelperList.add(new NormalDecodeHelper());
        this.resultProcessorList.add(new ProcessImageResultProcessor());
        this.resultProcessorList.add(new ProcessedResultCacheProcessor());
    }

    @NonNull
    private DecodeResult doDecode(LoadRequest loadRequest) throws DecodeException {
        DecodeResult decodeResult;
        try {
            DataSource dataSourceWithPressedCache = loadRequest.getDataSourceWithPressedCache();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try {
                ImageDecodeUtils.decodeBitmap(dataSourceWithPressedCache, options);
                if (options.outWidth <= 1 || options.outHeight <= 1) {
                    String format = String.format("Image width or height less than or equal to 1px. imageSize: %dx%d", Integer.valueOf(options.outWidth), Integer.valueOf(options.outHeight));
                    ImageDecodeUtils.decodeError(loadRequest, dataSourceWithPressedCache, NAME, format, null);
                    throw new DecodeException(format, ErrorCause.DECODE_BOUND_RESULT_IMAGE_SIZE_INVALID);
                }
                int readExifOrientation = !loadRequest.getOptions().isCorrectImageOrientationDisabled() ? loadRequest.getConfiguration().getOrientationCorrector().readExifOrientation(options.outMimeType, dataSourceWithPressedCache) : 0;
                ImageType valueOfMimeType = ImageType.valueOfMimeType(options.outMimeType);
                BitmapFactory.Options options2 = new BitmapFactory.Options();
                if (Build.VERSION.SDK_INT >= 10 && loadRequest.getOptions().isInPreferQualityOverSpeed()) {
                    options2.inPreferQualityOverSpeed = true;
                }
                Bitmap.Config bitmapConfig = loadRequest.getOptions().getBitmapConfig();
                if (bitmapConfig == null && valueOfMimeType != null) {
                    bitmapConfig = valueOfMimeType.getConfig(loadRequest.getOptions().isLowQualityImage());
                }
                if (bitmapConfig != null) {
                    options2.inPreferredConfig = bitmapConfig;
                }
                Iterator<DecodeHelper> it = this.decodeHelperList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        decodeResult = null;
                        break;
                    }
                    DecodeHelper next = it.next();
                    if (next.match(loadRequest, dataSourceWithPressedCache, valueOfMimeType, options)) {
                        decodeResult = next.decode(loadRequest, dataSourceWithPressedCache, valueOfMimeType, options, options2, readExifOrientation);
                        break;
                    }
                }
                if (decodeResult != null) {
                    decodeResult.setImageFrom(dataSourceWithPressedCache.getImageFrom());
                    return decodeResult;
                }
                ImageDecodeUtils.decodeError(loadRequest, null, NAME, "No matching DecodeHelper", null);
                throw new DecodeException("No matched DecodeHelper", ErrorCause.DECODE_NO_MATCHING_DECODE_HELPER);
            } catch (Throwable th) {
                ImageDecodeUtils.decodeError(loadRequest, dataSourceWithPressedCache, NAME, "Unable read bound information", th);
                throw new DecodeException("Unable read bound information", th, ErrorCause.DECODE_UNABLE_READ_BOUND_INFORMATION);
            }
        } catch (GetDataSourceException e) {
            ImageDecodeUtils.decodeError(loadRequest, null, NAME, "Unable create DataSource", e);
            throw new DecodeException("Unable create DataSource", e, ErrorCause.DECODE_UNABLE_CREATE_DATA_SOURCE);
        }
    }

    private void doProcess(LoadRequest loadRequest, DecodeResult decodeResult) throws ProcessException {
        if (decodeResult == null || decodeResult.isBanProcess()) {
            return;
        }
        Iterator<ResultProcessor> it = this.resultProcessorList.iterator();
        while (it.hasNext()) {
            it.next().process(loadRequest, decodeResult);
        }
    }

    @NonNull
    public DecodeResult decode(@NonNull LoadRequest loadRequest) throws DecodeException {
        DecodeResult decodeResult;
        long decodeStart;
        try {
            decodeStart = SLog.isLoggable(262146) ? this.timeAnalyze.decodeStart() : 0L;
            decodeResult = doDecode(loadRequest);
        } catch (DecodeException e) {
            e = e;
            decodeResult = null;
        } catch (Throwable th) {
            th = th;
            decodeResult = null;
        }
        try {
            if (SLog.isLoggable(262146)) {
                this.timeAnalyze.decodeEnd(decodeStart, NAME, loadRequest.getKey());
            }
            try {
                doProcess(loadRequest, decodeResult);
                return decodeResult;
            } catch (ProcessException e2) {
                decodeResult.recycle(loadRequest.getConfiguration().getBitmapPool());
                throw new DecodeException(e2, ErrorCause.DECODE_PROCESS_IMAGE_FAIL);
            }
        } catch (DecodeException e3) {
            e = e3;
            if (decodeResult != null) {
                decodeResult.recycle(loadRequest.getConfiguration().getBitmapPool());
            }
            throw e;
        } catch (Throwable th2) {
            th = th2;
            if (decodeResult != null) {
                decodeResult.recycle(loadRequest.getConfiguration().getBitmapPool());
            }
            throw new DecodeException(th, ErrorCause.DECODE_UNKNOWN_EXCEPTION);
        }
    }

    @NonNull
    public String toString() {
        return NAME;
    }
}
