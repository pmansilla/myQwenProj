package me.panpf.sketch.decode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Field;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.request.DisplayRequest;
import me.panpf.sketch.request.FixedSize;
import me.panpf.sketch.request.LoadRequest;
import me.panpf.sketch.request.MaxSize;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ImageSizeCalculator {
    private static final String KEY = "ImageSizeCalculator";
    private int openGLMaxTextureSize = -1;
    private float targetSizeScale = 1.1f;

    private static int getHeight(SketchView sketchView, boolean z, boolean z2, boolean z3) {
        int i = 0;
        if (sketchView == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = sketchView.getLayoutParams();
        if (layoutParams != null) {
            i = layoutParams.height;
            if (z3 && i > 0 && (i - sketchView.getPaddingTop()) - sketchView.getPaddingBottom() > 0) {
                return i - (sketchView.getPaddingTop() + sketchView.getPaddingBottom());
            }
        }
        if (i <= 0 && z) {
            i = getViewFieldValue(sketchView, "mMaxHeight");
        }
        if (i > 0 || !z2 || layoutParams == null || layoutParams.height != -2) {
            return i;
        }
        return -1;
    }

    private static int getViewFieldValue(Object obj, String str) {
        try {
            Field declaredField = ImageView.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            int intValue = ((Integer) declaredField.get(obj)).intValue();
            if (intValue <= 0 || intValue >= Integer.MAX_VALUE) {
                return 0;
            }
            return intValue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int getWidth(SketchView sketchView, boolean z, boolean z2, boolean z3) {
        int i = 0;
        if (sketchView == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = sketchView.getLayoutParams();
        if (layoutParams != null) {
            i = layoutParams.width;
            if (z3 && i > 0 && (i - sketchView.getPaddingLeft()) - sketchView.getPaddingRight() > 0) {
                return i - (sketchView.getPaddingLeft() + sketchView.getPaddingRight());
            }
        }
        if (i <= 0 && z) {
            i = getViewFieldValue(sketchView, "mMaxWidth");
        }
        if (i > 0 || !z2 || layoutParams == null || layoutParams.width != -2) {
            return i;
        }
        return -1;
    }

    public FixedSize calculateImageFixedSize(SketchView sketchView) {
        ViewGroup.LayoutParams layoutParams = sketchView.getLayoutParams();
        if (layoutParams == null || layoutParams.width <= 0 || layoutParams.height <= 0) {
            return null;
        }
        int paddingLeft = layoutParams.width - (sketchView.getPaddingLeft() + sketchView.getPaddingRight());
        int paddingTop = layoutParams.height - (sketchView.getPaddingTop() + sketchView.getPaddingBottom());
        int openGLMaxTextureSize = getOpenGLMaxTextureSize();
        if (paddingLeft > openGLMaxTextureSize || paddingTop > openGLMaxTextureSize) {
            float f = paddingLeft;
            float f2 = openGLMaxTextureSize;
            float f3 = paddingTop;
            float max = Math.max(f / f2, f3 / f2);
            paddingLeft = (int) (f / max);
            paddingTop = (int) (f3 / max);
        }
        return new FixedSize(paddingLeft, paddingTop);
    }

    public MaxSize calculateImageMaxSize(SketchView sketchView) {
        int width = getWidth(sketchView, true, true, false);
        int height = getHeight(sketchView, true, true, false);
        if (width <= 0 && height <= 0) {
            return null;
        }
        DisplayMetrics displayMetrics = sketchView.getResources().getDisplayMetrics();
        int i = (int) (displayMetrics.widthPixels * 1.5f);
        int i2 = (int) (displayMetrics.heightPixels * 1.5f);
        if (width > i || height > i2) {
            float f = width;
            float f2 = f / i;
            float f3 = height;
            float f4 = f3 / i2;
            if (f2 > f4) {
                f4 = f2;
            }
            width = (int) (f / f4);
            height = (int) (f3 / f4);
        }
        return new MaxSize(width, height);
    }

    public int calculateInSampleSize(int i, int i2, int i3, int i4, boolean z) {
        int i5 = (int) (i3 * this.targetSizeScale);
        int i6 = (int) (i4 * this.targetSizeScale);
        int openGLMaxTextureSize = getOpenGLMaxTextureSize();
        if (i5 > openGLMaxTextureSize) {
            i5 = openGLMaxTextureSize;
        }
        if (i6 > openGLMaxTextureSize) {
            i6 = openGLMaxTextureSize;
        }
        int i7 = 1;
        if (i5 <= 0 && i6 <= 0) {
            return 1;
        }
        if (i5 >= i && i6 >= i2) {
            return 1;
        }
        if (i5 <= 0) {
            while (SketchUtils.ceil(i2, i7) > i6) {
                i7 *= 2;
            }
            return i7;
        }
        if (i6 <= 0) {
            while (SketchUtils.ceil(i, i7) > i5) {
                i7 *= 2;
            }
            return i7;
        }
        while (true) {
            float f = i7;
            if (SketchUtils.ceil(i, f) * SketchUtils.ceil(i2, f) <= i5 * i6) {
                break;
            }
            i7 *= 2;
        }
        while (true) {
            float f2 = i7;
            if (SketchUtils.ceil(i, f2) <= openGLMaxTextureSize && SketchUtils.ceil(i2, f2) <= openGLMaxTextureSize) {
                break;
            }
            i7 *= 2;
        }
        if (z && i7 == 2) {
            return 4;
        }
        return i7;
    }

    public boolean canUseReadModeByHeight(int i, int i2) {
        return i2 > i * 2;
    }

    public boolean canUseReadModeByWidth(int i, int i2) {
        return i > i2 * 3;
    }

    public boolean canUseSmallerThumbnails(LoadRequest loadRequest, ImageType imageType) {
        return (loadRequest instanceof DisplayRequest) && ((DisplayRequest) loadRequest).getViewInfo().isUseSmallerThumbnails() && SketchUtils.sdkSupportBitmapRegionDecoder() && SketchUtils.formatSupportBitmapRegionDecoder(imageType);
    }

    public boolean canUseThumbnailMode(int i, int i2, int i3, int i4) {
        if (i3 > i && i4 > i2) {
            return false;
        }
        float f = i3 / i4;
        float f2 = i / i2;
        return Math.max(f, f2) > Math.min(f, f2) * 1.5f;
    }

    public MaxSize getDefaultImageMaxSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return new MaxSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public int getOpenGLMaxTextureSize() {
        if (this.openGLMaxTextureSize == -1) {
            this.openGLMaxTextureSize = SketchUtils.getOpenGLMaxTextureSize();
        }
        return this.openGLMaxTextureSize;
    }

    public float getTargetSizeScale() {
        return this.targetSizeScale;
    }

    public void setOpenGLMaxTextureSize(int i) {
        this.openGLMaxTextureSize = i;
    }

    public void setTargetSizeScale(float f) {
        this.targetSizeScale = f;
    }

    @NonNull
    public String toString() {
        return KEY;
    }
}
