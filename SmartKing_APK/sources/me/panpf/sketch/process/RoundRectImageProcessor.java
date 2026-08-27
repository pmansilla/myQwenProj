package me.panpf.sketch.process;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.internal.view.SupportMenu;
import java.util.Arrays;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.decode.ResizeCalculator;
import me.panpf.sketch.request.Resize;

/* loaded from: classes2.dex */
public class RoundRectImageProcessor extends WrappedImageProcessor {

    @NonNull
    private float[] cornerRadius;

    public RoundRectImageProcessor(float f) {
        this(f, f, f, f, null);
    }

    public RoundRectImageProcessor(float f, float f2, float f3, float f4) {
        this(f, f2, f3, f4, null);
    }

    public RoundRectImageProcessor(float f, float f2, float f3, float f4, WrappedImageProcessor wrappedImageProcessor) {
        super(wrappedImageProcessor);
        this.cornerRadius = new float[]{f, f, f2, f2, f3, f3, f4, f4};
    }

    public RoundRectImageProcessor(float f, WrappedImageProcessor wrappedImageProcessor) {
        this(f, f, f, f, wrappedImageProcessor);
    }

    @NonNull
    public float[] getCornerRadius() {
        return this.cornerRadius;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    protected boolean isInterceptResize() {
        return true;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    public String onGetKey() {
        return String.format("%s(%s)", "RoundRect", Arrays.toString(this.cornerRadius));
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public Bitmap onProcess(@NonNull Sketch sketch, @NonNull Bitmap bitmap, Resize resize, boolean z) {
        if (bitmap.isRecycled()) {
            return bitmap;
        }
        ResizeCalculator.Mapping calculator = sketch.getConfiguration().getResizeCalculator().calculator(bitmap.getWidth(), bitmap.getHeight(), resize != null ? resize.getWidth() : bitmap.getWidth(), resize != null ? resize.getHeight() : bitmap.getHeight(), resize != null ? resize.getScaleType() : null, resize != null && resize.getMode() == Resize.Mode.EXACTLY_SAME);
        if (calculator == null) {
            return bitmap;
        }
        Bitmap orMake = sketch.getConfiguration().getBitmapPool().getOrMake(calculator.imageWidth, calculator.imageHeight, z ? Bitmap.Config.ARGB_4444 : Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(orMake);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(SupportMenu.CATEGORY_MASK);
        Path path = new Path();
        path.addRoundRect(new RectF(0.0f, 0.0f, calculator.imageWidth, calculator.imageHeight), this.cornerRadius, Path.Direction.CW);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, calculator.srcRect, calculator.destRect, paint);
        return orMake;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public String onToString() {
        return String.format("%s(%s)", "RoundRectImageProcessor", Arrays.toString(this.cornerRadius));
    }
}
