package me.panpf.sketch.decode;

import me.panpf.sketch.request.LoadRequest;

/* loaded from: classes2.dex */
public class ProcessedResultCacheProcessor implements ResultProcessor {
    @Override // me.panpf.sketch.decode.ResultProcessor
    public void process(LoadRequest loadRequest, DecodeResult decodeResult) {
        if (!decodeResult.isBanProcess() && (decodeResult instanceof BitmapDecodeResult)) {
            ProcessedImageCache processedImageCache = loadRequest.getConfiguration().getProcessedImageCache();
            if (processedImageCache.canUse(loadRequest.getOptions()) && decodeResult.isProcessed()) {
                processedImageCache.saveToDiskCache(loadRequest, ((BitmapDecodeResult) decodeResult).getBitmap());
            }
        }
    }
}
