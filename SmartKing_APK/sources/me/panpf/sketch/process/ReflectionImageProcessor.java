package me.panpf.sketch.process;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.request.Resize;

/* loaded from: classes2.dex */
public class ReflectionImageProcessor extends WrappedImageProcessor {
    private static final float DEFAULT_REFLECTION_SCALE = 0.3f;
    private static final int DEFAULT_REFLECTION_SPACING = 2;
    private float reflectionScale;
    private int reflectionSpacing;

    public ReflectionImageProcessor() {
        this(2, DEFAULT_REFLECTION_SCALE, null);
    }

    public ReflectionImageProcessor(int i, float f) {
        this(i, f, null);
    }

    public ReflectionImageProcessor(int i, float f, WrappedImageProcessor wrappedImageProcessor) {
        super(wrappedImageProcessor);
        this.reflectionSpacing = i;
        this.reflectionScale = f;
    }

    public ReflectionImageProcessor(WrappedImageProcessor wrappedImageProcessor) {
        this(2, DEFAULT_REFLECTION_SCALE, wrappedImageProcessor);
    }

    public float getReflectionScale() {
        return this.reflectionScale;
    }

    public int getReflectionSpacing() {
        return this.reflectionSpacing;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    public String onGetKey() {
        return String.format("%s(scale=%s,spacing=%d)", "Reflection", Float.valueOf(this.reflectionScale), Integer.valueOf(this.reflectionSpacing));
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public Bitmap onProcess(@NonNull Sketch sketch, @NonNull Bitmap bitmap, Resize resize, boolean z) {
        if (bitmap.isRecycled()) {
            return bitmap;
        }
        int height = bitmap.getHeight();
        int i = (int) (height * this.reflectionScale);
        int i2 = this.reflectionSpacing + height;
        Bitmap orMake = sketch.getConfiguration().getBitmapPool().getOrMake(bitmap.getWidth(), i + i2, z ? Bitmap.Config.ARGB_4444 : Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(orMake);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        Matrix matrix = new Matrix();
        matrix.postScale(1.0f, -1.0f);
        matrix.postTranslate(0.0f, height + i2);
        canvas.drawBitmap(bitmap, matrix, null);
        Paint paint = new Paint();
        float f = i2;
        paint.setShader(new LinearGradient(0.0f, f, 0.0f, orMake.getHeight(), 1895825407, ViewCompat.MEASURED_SIZE_MASK, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0.0f, f, orMake.getWidth(), orMake.getHeight(), paint);
        return orMake;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public String onToString() {
        return String.format("%s(scale=%s,spacing=%d)", "ReflectionImageProcessor", Float.valueOf(this.reflectionScale), Integer.valueOf(this.reflectionSpacing));
    }
}
