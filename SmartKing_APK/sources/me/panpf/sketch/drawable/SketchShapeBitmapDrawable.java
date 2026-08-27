package me.panpf.sketch.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.decode.ResizeCalculator;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.request.ShapeSize;
import me.panpf.sketch.shaper.ImageShaper;

/* loaded from: classes2.dex */
public class SketchShapeBitmapDrawable extends Drawable implements SketchRefDrawable {
    private static final int DEFAULT_PAINT_FLAGS = 6;
    private BitmapDrawable bitmapDrawable;
    private BitmapShader bitmapShader;
    private Paint paint;
    private SketchRefDrawable refDrawable;
    private ResizeCalculator resizeCalculator;
    private ShapeSize shapeSize;
    private ImageShaper shaper;
    private SketchDrawable sketchDrawable;
    private Rect srcRect;

    public SketchShapeBitmapDrawable(Context context, BitmapDrawable bitmapDrawable, ShapeSize shapeSize) {
        this(context, bitmapDrawable, shapeSize, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SketchShapeBitmapDrawable(Context context, BitmapDrawable bitmapDrawable, ShapeSize shapeSize, ImageShaper imageShaper) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (bitmap == null || bitmap.isRecycled()) {
            throw new IllegalArgumentException(bitmap == null ? "bitmap is null" : "bitmap recycled");
        }
        if (shapeSize == null && imageShaper == null) {
            throw new IllegalArgumentException("shapeSize is null and shapeImage is null");
        }
        this.bitmapDrawable = bitmapDrawable;
        this.paint = new Paint(6);
        this.srcRect = new Rect();
        this.resizeCalculator = Sketch.with(context).getConfiguration().getResizeCalculator();
        setShapeSize(shapeSize);
        setShaper(imageShaper);
        if (bitmapDrawable instanceof SketchRefDrawable) {
            this.refDrawable = (SketchRefDrawable) bitmapDrawable;
        }
        if (bitmapDrawable instanceof SketchDrawable) {
            this.sketchDrawable = (SketchDrawable) bitmapDrawable;
        }
    }

    public SketchShapeBitmapDrawable(Context context, BitmapDrawable bitmapDrawable, ImageShaper imageShaper) {
        this(context, bitmapDrawable, null, imageShaper);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        Bitmap bitmap = this.bitmapDrawable.getBitmap();
        if (bounds.isEmpty() || bitmap == null || bitmap.isRecycled()) {
            return;
        }
        if (this.shaper == null || this.bitmapShader == null) {
            canvas.drawBitmap(bitmap, (this.srcRect == null || this.srcRect.isEmpty()) ? null : this.srcRect, bounds, this.paint);
        } else {
            this.shaper.draw(canvas, this.paint, bounds);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.paint.getAlpha();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public Bitmap.Config getBitmapConfig() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getBitmapConfig();
        }
        return null;
    }

    public BitmapDrawable getBitmapDrawable() {
        return this.bitmapDrawable;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getByteCount() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getByteCount();
        }
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.paint.getColorFilter();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getExifOrientation() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getExifOrientation();
        }
        return 0;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public ImageFrom getImageFrom() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getImageFrom();
        }
        return null;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getInfo() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getInfo();
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.shapeSize != null ? this.shapeSize.getHeight() : this.bitmapDrawable.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.shapeSize != null ? this.shapeSize.getWidth() : this.bitmapDrawable.getIntrinsicWidth();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getKey() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getKey();
        }
        return null;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getMimeType() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getMimeType();
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return (this.bitmapDrawable.getBitmap().hasAlpha() || this.paint.getAlpha() < 255) ? -3 : -1;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getOriginHeight() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getOriginHeight();
        }
        return 0;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getOriginWidth() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getOriginWidth();
        }
        return 0;
    }

    public ShapeSize getShapeSize() {
        return this.shapeSize;
    }

    public ImageShaper getShaper() {
        return this.shaper;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getUri() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getUri();
        }
        return null;
    }

    @Override // me.panpf.sketch.drawable.SketchRefDrawable
    public boolean isRecycled() {
        return this.refDrawable == null || this.refDrawable.isRecycled();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        int width = rect.width();
        int height = rect.height();
        int width2 = this.bitmapDrawable.getBitmap().getWidth();
        int height2 = this.bitmapDrawable.getBitmap().getHeight();
        if (width == 0 || height == 0 || width2 == 0 || height2 == 0) {
            this.srcRect.setEmpty();
        } else if (width2 / height2 == width / height) {
            this.srcRect.set(0, 0, width2, height2);
        } else {
            this.srcRect.set(this.resizeCalculator.calculator(width2, height2, width, height, this.shapeSize != null ? this.shapeSize.getScaleType() : ImageView.ScaleType.FIT_CENTER, true).srcRect);
        }
        if (this.shaper == null || this.bitmapShader == null) {
            return;
        }
        Matrix matrix = new Matrix();
        float max = Math.max(width / width2, height / height2);
        matrix.postScale(max, max);
        if (this.srcRect != null && !this.srcRect.isEmpty()) {
            matrix.postTranslate((-this.srcRect.left) * max, (-this.srcRect.top) * max);
        }
        this.shaper.onUpdateShaderMatrix(matrix, rect, width2, height2, this.shapeSize, this.srcRect);
        this.bitmapShader.setLocalMatrix(matrix);
        this.paint.setShader(this.bitmapShader);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (i != this.paint.getAlpha()) {
            this.paint.setAlpha(i);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean z) {
        this.paint.setDither(z);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean z) {
        this.paint.setFilterBitmap(z);
        invalidateSelf();
    }

    @Override // me.panpf.sketch.drawable.SketchRefDrawable
    public void setIsDisplayed(String str, boolean z) {
        if (this.refDrawable != null) {
            this.refDrawable.setIsDisplayed(str, z);
        }
    }

    @Override // me.panpf.sketch.drawable.SketchRefDrawable
    public void setIsWaitingUse(String str, boolean z) {
        if (this.refDrawable != null) {
            this.refDrawable.setIsWaitingUse(str, z);
        }
    }

    public void setShapeSize(ShapeSize shapeSize) {
        this.shapeSize = shapeSize;
        invalidateSelf();
    }

    public void setShaper(ImageShaper imageShaper) {
        this.shaper = imageShaper;
        if (this.shaper != null) {
            if (this.bitmapShader == null) {
                this.bitmapShader = new BitmapShader(this.bitmapDrawable.getBitmap(), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                this.paint.setShader(this.bitmapShader);
            }
        } else if (this.bitmapShader != null) {
            this.bitmapShader = null;
            this.paint.setShader(null);
        }
        invalidateSelf();
    }
}
