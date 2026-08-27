package me.panpf.sketch.viewfun;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.SLog;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.request.CancelCause;
import me.panpf.sketch.request.DisplayCache;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.shaper.ImageShaper;
import me.panpf.sketch.uri.UriModel;

/* loaded from: classes2.dex */
public class ShowDownloadProgressFunction extends ViewFunction {
    static final int DEFAULT_MASK_COLOR = 570425344;
    private static final String NAME = "ShowProgressFunction";
    private static final int NONE = -1;
    private Rect bounds;
    private Paint maskPaint;
    private ImageShaper maskShaper;
    private FunctionPropertyView view;
    private int maskColor = DEFAULT_MASK_COLOR;
    private float progress = -1.0f;

    public ShowDownloadProgressFunction(@NonNull FunctionPropertyView functionPropertyView) {
        this.view = functionPropertyView;
    }

    private ImageShaper getMaskShaper() {
        if (this.maskShaper != null) {
            return this.maskShaper;
        }
        DisplayCache displayCache = this.view.getDisplayCache();
        ImageShaper shaper = displayCache != null ? displayCache.options.getShaper() : null;
        if (shaper != null) {
            return shaper;
        }
        ImageShaper shaper2 = this.view.getOptions().getShaper();
        if (shaper2 != null) {
            return shaper2;
        }
        return null;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDisplayCanceled(@NonNull CancelCause cancelCause) {
        this.progress = -1.0f;
        return false;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDisplayCompleted(@NonNull Drawable drawable, @NonNull ImageFrom imageFrom, @NonNull ImageAttrs imageAttrs) {
        this.progress = -1.0f;
        return true;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDisplayError(@NonNull ErrorCause errorCause) {
        this.progress = -1.0f;
        return true;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public void onDraw(@NonNull Canvas canvas) {
        if (this.progress == -1.0f) {
            return;
        }
        ImageShaper maskShaper = getMaskShaper();
        if (maskShaper != null) {
            canvas.save();
            try {
                if (this.bounds == null) {
                    this.bounds = new Rect();
                }
                this.bounds.set(this.view.getPaddingLeft(), this.view.getPaddingTop(), this.view.getWidth() - this.view.getPaddingRight(), this.view.getHeight() - this.view.getPaddingBottom());
                canvas.clipPath(maskShaper.getPath(this.bounds));
            } catch (UnsupportedOperationException e) {
                SLog.e(NAME, "The current environment doesn't support clipPath has shut down automatically hardware acceleration");
                if (Build.VERSION.SDK_INT >= 11) {
                    this.view.setLayerType(1, null);
                }
                e.printStackTrace();
            }
        }
        if (this.maskPaint == null) {
            this.maskPaint = new Paint();
            this.maskPaint.setColor(this.maskColor);
            this.maskPaint.setAntiAlias(true);
        }
        canvas.drawRect(this.view.getPaddingLeft(), this.view.getPaddingTop() + (this.progress * this.view.getHeight()), (this.view.getWidth() - this.view.getPaddingLeft()) - this.view.getPaddingRight(), (this.view.getHeight() - this.view.getPaddingTop()) - this.view.getPaddingBottom(), this.maskPaint);
        if (maskShaper != null) {
            canvas.restore();
        }
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onReadyDisplay(@Nullable UriModel uriModel) {
        float f = (float) ((uriModel == null || !uriModel.isFromNet()) ? -1L : 0L);
        boolean z = this.progress != f;
        this.progress = f;
        return z;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onUpdateDownloadProgress(int i, int i2) {
        this.progress = i2 / i;
        return true;
    }

    public boolean setMaskColor(@ColorInt int i) {
        if (this.maskColor == i) {
            return false;
        }
        this.maskColor = i;
        if (this.maskPaint == null) {
            return true;
        }
        this.maskPaint.setColor(i);
        return true;
    }

    public boolean setMaskShaper(@Nullable ImageShaper imageShaper) {
        if (this.maskShaper == imageShaper) {
            return false;
        }
        this.maskShaper = imageShaper;
        return true;
    }
}
