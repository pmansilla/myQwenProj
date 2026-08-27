package me.panpf.sketch.process;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.amap.api.maps.utils.SpatialRelationUtil;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.request.Resize;

/* loaded from: classes2.dex */
public class RotateImageProcessor extends WrappedImageProcessor {
    private int degrees;

    public RotateImageProcessor(int i) {
        this(i, null);
    }

    public RotateImageProcessor(int i, WrappedImageProcessor wrappedImageProcessor) {
        super(wrappedImageProcessor);
        this.degrees = i;
    }

    public static Bitmap rotate(Bitmap bitmap, int i, BitmapPool bitmapPool) {
        Matrix matrix = new Matrix();
        matrix.setRotate(i);
        RectF rectF = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        matrix.mapRect(rectF);
        int width = (int) rectF.width();
        int height = (int) rectF.height();
        Bitmap.Config config = bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888;
        if (i % 90 != 0 && config != Bitmap.Config.ARGB_8888) {
            config = Bitmap.Config.ARGB_8888;
        }
        Bitmap orMake = bitmapPool.getOrMake(width, height, config);
        matrix.postTranslate(-rectF.left, -rectF.top);
        new Canvas(orMake).drawBitmap(bitmap, matrix, new Paint(6));
        return orMake;
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @Nullable
    public String onGetKey() {
        if (this.degrees % SpatialRelationUtil.A_CIRCLE_DEGREE == 0) {
            return null;
        }
        return String.format("%s(%d)", "Rotate", Integer.valueOf(this.degrees));
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public Bitmap onProcess(@NonNull Sketch sketch, @NonNull Bitmap bitmap, Resize resize, boolean z) {
        return (bitmap.isRecycled() || this.degrees % SpatialRelationUtil.A_CIRCLE_DEGREE == 0) ? bitmap : rotate(bitmap, this.degrees, sketch.getConfiguration().getBitmapPool());
    }

    @Override // me.panpf.sketch.process.WrappedImageProcessor
    @NonNull
    public String onToString() {
        return String.format("%s(%d)", "RotateImageProcessor", Integer.valueOf(this.degrees));
    }
}
