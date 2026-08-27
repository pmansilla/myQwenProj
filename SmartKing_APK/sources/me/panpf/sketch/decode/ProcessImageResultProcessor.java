package me.panpf.sketch.decode;

import android.graphics.Bitmap;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.process.ImageProcessor;
import me.panpf.sketch.request.BaseRequest;
import me.panpf.sketch.request.LoadOptions;
import me.panpf.sketch.request.LoadRequest;

/* loaded from: classes2.dex */
public class ProcessImageResultProcessor implements ResultProcessor {
    @Override // me.panpf.sketch.decode.ResultProcessor
    public void process(LoadRequest loadRequest, DecodeResult decodeResult) throws ProcessException {
        BitmapDecodeResult bitmapDecodeResult;
        Bitmap bitmap;
        LoadOptions options;
        ImageProcessor processor;
        Bitmap bitmap2;
        if (decodeResult.isBanProcess() || !(decodeResult instanceof BitmapDecodeResult) || (bitmap = (bitmapDecodeResult = (BitmapDecodeResult) decodeResult).getBitmap()) == null || (processor = (options = loadRequest.getOptions()).getProcessor()) == null) {
            return;
        }
        loadRequest.setStatus(BaseRequest.Status.PROCESSING);
        try {
            bitmap2 = processor.process(loadRequest.getSketch(), bitmap, options.getResize(), options.isLowQualityImage());
        } catch (Throwable th) {
            th.printStackTrace();
            loadRequest.getConfiguration().getErrorTracker().onProcessImageError(th, loadRequest.getKey(), processor);
            bitmap2 = null;
        }
        if (bitmap2 == null || bitmap2.isRecycled()) {
            throw new ProcessException("Process result bitmap null or recycled");
        }
        if (bitmap2 != bitmap) {
            BitmapPoolUtils.freeBitmapToPool(bitmap, loadRequest.getConfiguration().getBitmapPool());
            bitmapDecodeResult.setBitmap(bitmap2);
        }
        decodeResult.setProcessed(true);
    }
}
