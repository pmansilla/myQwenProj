package me.panpf.sketch.process;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.request.Resize;

/* loaded from: classes2.dex */
public abstract class WrappedImageProcessor extends ResizeImageProcessor {
    private WrappedImageProcessor wrappedProcessor;

    /* JADX INFO: Access modifiers changed from: protected */
    public WrappedImageProcessor(WrappedImageProcessor wrappedImageProcessor) {
        this.wrappedProcessor = wrappedImageProcessor;
    }

    @Override // me.panpf.sketch.process.ResizeImageProcessor, me.panpf.sketch.Key
    @Nullable
    public String getKey() {
        String onGetKey = onGetKey();
        String key = this.wrappedProcessor != null ? this.wrappedProcessor.getKey() : null;
        if (!TextUtils.isEmpty(onGetKey)) {
            return !TextUtils.isEmpty(key) ? String.format("%s->%s", onGetKey, key) : onGetKey;
        }
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return key;
    }

    public WrappedImageProcessor getWrappedProcessor() {
        return this.wrappedProcessor;
    }

    protected boolean isInterceptResize() {
        return false;
    }

    @Nullable
    public abstract String onGetKey();

    @NonNull
    public abstract Bitmap onProcess(@NonNull Sketch sketch, @NonNull Bitmap bitmap, @Nullable Resize resize, boolean z);

    @NonNull
    public abstract String onToString();

    @Override // me.panpf.sketch.process.ResizeImageProcessor, me.panpf.sketch.process.ImageProcessor
    @NonNull
    public final Bitmap process(@NonNull Sketch sketch, @NonNull Bitmap bitmap, @Nullable Resize resize, boolean z) {
        Bitmap process;
        if (bitmap == null || bitmap.isRecycled()) {
            return bitmap;
        }
        Bitmap process2 = !isInterceptResize() ? super.process(sketch, bitmap, resize, z) : bitmap;
        if (this.wrappedProcessor != null && (process = this.wrappedProcessor.process(sketch, process2, resize, z)) != process2) {
            if (process2 != bitmap) {
                BitmapPoolUtils.freeBitmapToPool(process2, sketch.getConfiguration().getBitmapPool());
            }
            process2 = process;
        }
        return onProcess(sketch, process2, resize, z);
    }

    @Override // me.panpf.sketch.process.ResizeImageProcessor
    @NonNull
    public String toString() {
        String onToString = onToString();
        String wrappedImageProcessor = this.wrappedProcessor != null ? this.wrappedProcessor.toString() : null;
        return TextUtils.isEmpty(wrappedImageProcessor) ? onToString : String.format("%s->%s", onToString, wrappedImageProcessor);
    }
}
