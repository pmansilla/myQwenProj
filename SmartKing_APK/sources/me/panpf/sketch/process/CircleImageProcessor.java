package me.panpf.sketch.process;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.internal.view.SupportMenu;
import android.widget.ImageView;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.decode.ResizeCalculator;
import me.panpf.sketch.request.Resize;

/* loaded from: classes2.dex */
public class CircleImageProcessor extends WrappedImageProcessor {
    private static CircleImageProcessor instance;

    private CircleImageProcessor() {
        this(null);
    }

    public CircleImageProcessor(WrappedImageProcessor wrappedImageProcessor) {
        super(wrappedImageProcessor);
    }

    public static CircleImageProcessor getInstance() {
        if (instance == null) {
            synchronized (CircleImageProcessor.class) {
                if (instance == null) {
                    instance = new CircleImageProcessor();
                }
            }
        }
        return instance;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    protected boolean isInterceptResize() {
        return true;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    public String onGetKey() {
        return "Circle";
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public Bitmap onProcess(@NonNull Sketch sketch, @NonNull Bitmap bitmap, @Nullable Resize resize, boolean z) {
        if (bitmap.isRecycled()) {
            return bitmap;
        }
        int width = resize != null ? resize.getWidth() : bitmap.getWidth();
        int height = resize != null ? resize.getHeight() : bitmap.getHeight();
        int i = width < height ? width : height;
        ResizeCalculator.Mapping calculator = sketch.getConfiguration().getResizeCalculator().calculator(bitmap.getWidth(), bitmap.getHeight(), i, i, resize != null ? resize.getScaleType() : ImageView.ScaleType.FIT_CENTER, resize != null && resize.getMode() == Resize.Mode.EXACTLY_SAME);
        if (calculator == null) {
            return bitmap;
        }
        Bitmap orMake = sketch.getConfiguration().getBitmapPool().getOrMake(calculator.imageWidth, calculator.imageHeight, z ? Bitmap.Config.ARGB_4444 : Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(orMake);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(SupportMenu.CATEGORY_MASK);
        canvas.drawCircle(calculator.imageWidth / 2, calculator.imageHeight / 2, (calculator.imageWidth < calculator.imageHeight ? calculator.imageWidth : calculator.imageHeight) / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, calculator.srcRect, calculator.destRect, paint);
        return orMake;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public String onToString() {
        return "CircleImageProcessor";
    }
}
