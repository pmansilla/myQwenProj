package me.panpf.sketch.process;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.decode.ResizeCalculator;
import me.panpf.sketch.request.Resize;

/* loaded from: classes2.dex */
public class ResizeImageProcessor implements ImageProcessor {
    @Override // me.panpf.sketch.Key
    @Nullable
    public String getKey() {
        return "Resize";
    }

    @Override // me.panpf.sketch.process.ImageProcessor
    @NonNull
    public Bitmap process(@NonNull Sketch sketch, @NonNull Bitmap bitmap, @Nullable Resize resize, boolean z) {
        if (bitmap.isRecycled() || resize == null || resize.getWidth() == 0 || resize.getHeight() == 0 || (bitmap.getWidth() == resize.getWidth() && bitmap.getHeight() == resize.getHeight())) {
            return bitmap;
        }
        ResizeCalculator.Mapping calculator = sketch.getConfiguration().getResizeCalculator().calculator(bitmap.getWidth(), bitmap.getHeight(), resize.getWidth(), resize.getHeight(), resize.getScaleType(), resize.getMode() == Resize.Mode.EXACTLY_SAME);
        if (calculator == null) {
            return bitmap;
        }
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = z ? Bitmap.Config.ARGB_4444 : Bitmap.Config.ARGB_8888;
        }
        Bitmap orMake = sketch.getConfiguration().getBitmapPool().getOrMake(calculator.imageWidth, calculator.imageHeight, config);
        new Canvas(orMake).drawBitmap(bitmap, calculator.srcRect, calculator.destRect, (Paint) null);
        return orMake;
    }

    @NonNull
    public String toString() {
        return "ResizeImageProcessor";
    }
}
