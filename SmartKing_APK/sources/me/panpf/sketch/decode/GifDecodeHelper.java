package me.panpf.sketch.decode;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.IOException;
import me.panpf.sketch.SLog;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.drawable.SketchGifFactory;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.LoadRequest;

/* loaded from: classes2.dex */
public class GifDecodeHelper extends DecodeHelper {
    @Override // me.panpf.sketch.decode.DecodeHelper
    @NonNull
    public DecodeResult decode(@NonNull LoadRequest loadRequest, @NonNull DataSource dataSource, @Nullable ImageType imageType, @NonNull BitmapFactory.Options options, @NonNull BitmapFactory.Options options2, int i) throws DecodeException {
        try {
            ImageAttrs imageAttrs = new ImageAttrs(options.outMimeType, options.outWidth, options.outHeight, i);
            return new GifDecodeResult(imageAttrs, dataSource.makeGifDrawable(loadRequest.getKey(), loadRequest.getUri(), imageAttrs, loadRequest.getConfiguration().getBitmapPool())).setBanProcess(true);
        } catch (IOException e) {
            throw new DecodeException(e, ErrorCause.DECODE_FILE_IO_EXCEPTION);
        } catch (ExceptionInInitializerError | UnsatisfiedLinkError e2) {
            loadRequest.getConfiguration().getErrorTracker().onNotFoundGifSoError(e2);
            throw new DecodeException(e2, ErrorCause.DECODE_NO_MATCHING_GIF_SO);
        } catch (NotFoundGifLibraryException e3) {
            throw new DecodeException(e3, ErrorCause.DECODE_NOT_FOUND_GIF_LIBRARY);
        } catch (Throwable th) {
            loadRequest.getConfiguration().getErrorTracker().onDecodeGifImageError(th, loadRequest, options.outWidth, options.outHeight, options.outMimeType);
            throw new DecodeException(th, ErrorCause.DECODE_UNABLE_CREATE_GIF_DRAWABLE);
        }
    }

    @Override // me.panpf.sketch.decode.DecodeHelper
    public boolean match(@NonNull LoadRequest loadRequest, @NonNull DataSource dataSource, @Nullable ImageType imageType, @NonNull BitmapFactory.Options options) {
        if (imageType == null || imageType != ImageType.GIF || !loadRequest.getOptions().isDecodeGifImage()) {
            return false;
        }
        if (SketchGifFactory.isExistGifLibrary()) {
            return true;
        }
        SLog.e("GifDecodeHelper", "Not found libpl_droidsonroids_gif.so. Please go to “https://github.com/panpf/sketch” find how to import the sketch-gif library");
        return false;
    }
}
