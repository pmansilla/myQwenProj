package me.panpf.sketch.process;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.request.Resize;

/* loaded from: classes2.dex */
public class MaskImageProcessor extends WrappedImageProcessor {
    private int maskColor;
    private Paint paint;

    public MaskImageProcessor(int i) {
        this(i, null);
    }

    public MaskImageProcessor(int i, WrappedImageProcessor wrappedImageProcessor) {
        super(wrappedImageProcessor);
        this.maskColor = i;
    }

    public int getMaskColor() {
        return this.maskColor;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    public String onGetKey() {
        return String.format("%s(%d)", "Mask", Integer.valueOf(this.maskColor));
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public Bitmap onProcess(@NonNull Sketch sketch, @NonNull Bitmap bitmap, Resize resize, boolean z) {
        Bitmap orMake;
        if (bitmap.isRecycled()) {
            return bitmap;
        }
        BitmapPool bitmapPool = sketch.getConfiguration().getBitmapPool();
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = z ? Bitmap.Config.ARGB_4444 : Bitmap.Config.ARGB_8888;
        }
        boolean z2 = false;
        if (bitmap.isMutable()) {
            orMake = bitmap;
        } else {
            orMake = bitmapPool.getOrMake(bitmap.getWidth(), bitmap.getHeight(), config);
            z2 = true;
        }
        Canvas canvas = new Canvas(orMake);
        if (z2) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        }
        if (this.paint == null) {
            this.paint = new Paint();
            this.paint.setColor(this.maskColor);
        }
        this.paint.setXfermode(null);
        int saveLayer = canvas.saveLayer(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight(), this.paint, 31);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight(), this.paint);
        canvas.restoreToCount(saveLayer);
        return orMake;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public String onToString() {
        return String.format("%s(%d)", "MaskImageProcessor", Integer.valueOf(this.maskColor));
    }
}
