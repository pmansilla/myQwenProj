package me.panpf.sketch.decode;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.widget.ImageView;

/* loaded from: classes2.dex */
public class ResizeCalculator {
    private static final String KEY = "ResizeCalculator";

    /* loaded from: classes2.dex */
    public static class Mapping {
        public Rect destRect;
        public int imageHeight;
        public int imageWidth;
        public Rect srcRect;
    }

    public static int[] scaleTargetSize(int i, int i2, int i3, int i4) {
        if (i3 > i || i4 > i2) {
            float f = Math.abs(i3 - i) < Math.abs(i4 - i2) ? i3 / i : i4 / i2;
            i3 = Math.round(i3 / f);
            i4 = Math.round(i4 / f);
        }
        return new int[]{i3, i4};
    }

    public static Rect srcMappingCenterRect(int i, int i2, int i3, int i4) {
        float f = i3;
        float f2 = i / f;
        float f3 = i4;
        float f4 = i2 / f3;
        if (f2 >= f4) {
            f2 = f4;
        }
        int i5 = (int) (f * f2);
        int i6 = (int) (f3 * f2);
        int i7 = (i - i5) / 2;
        int i8 = (i2 - i6) / 2;
        return new Rect(i7, i8, i5 + i7, i6 + i8);
    }

    public static Rect srcMappingEndRect(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        float f = i3;
        float f2 = i / f;
        float f3 = i4;
        float f4 = i2 / f3;
        if (f2 >= f4) {
            f2 = f4;
        }
        int i7 = (int) (f * f2);
        int i8 = (int) (f3 * f2);
        if (i > i2) {
            i5 = i - i7;
            i6 = i2 - i8;
        } else {
            i5 = i - i7;
            i6 = i2 - i8;
        }
        return new Rect(i5, i6, i7 + i5, i8 + i6);
    }

    public static Rect srcMappingStartRect(int i, int i2, int i3, int i4) {
        float f = i3;
        float f2 = i / f;
        float f3 = i4;
        float f4 = i2 / f3;
        if (f2 >= f4) {
            f2 = f4;
        }
        return new Rect(0, 0, ((int) (f * f2)) + 0, ((int) (f3 * f2)) + 0);
    }

    public static Rect srcMatrixRect(int i, int i2, int i3, int i4) {
        if (i > i3 && i2 > i4) {
            return new Rect(0, 0, i3, i4);
        }
        float f = i3 - i > i4 - i2 ? i3 / i : i4 / i2;
        return new Rect(0, 0, ((int) (i3 / f)) + 0, ((int) (i4 / f)) + 0);
    }

    public Mapping calculator(int i, int i2, int i3, int i4, ImageView.ScaleType scaleType, boolean z) {
        if (i == i3 && i2 == i4) {
            Mapping mapping = new Mapping();
            mapping.imageWidth = i;
            mapping.imageHeight = i2;
            mapping.srcRect = new Rect(0, 0, i, i2);
            mapping.destRect = mapping.srcRect;
            return mapping;
        }
        if (scaleType == null) {
            scaleType = ImageView.ScaleType.FIT_CENTER;
        }
        if (!z) {
            int[] scaleTargetSize = scaleTargetSize(i, i2, i3, i4);
            int i5 = scaleTargetSize[0];
            i4 = scaleTargetSize[1];
            i3 = i5;
        }
        Rect rect = new Rect(0, 0, i3, i4);
        Rect srcMappingCenterRect = (scaleType == ImageView.ScaleType.CENTER || scaleType == ImageView.ScaleType.CENTER_CROP || scaleType == ImageView.ScaleType.CENTER_INSIDE) ? srcMappingCenterRect(i, i2, i3, i4) : scaleType == ImageView.ScaleType.FIT_START ? srcMappingStartRect(i, i2, i3, i4) : scaleType == ImageView.ScaleType.FIT_CENTER ? srcMappingCenterRect(i, i2, i3, i4) : scaleType == ImageView.ScaleType.FIT_END ? srcMappingEndRect(i, i2, i3, i4) : scaleType == ImageView.ScaleType.FIT_XY ? new Rect(0, 0, i, i2) : scaleType == ImageView.ScaleType.MATRIX ? srcMatrixRect(i, i2, i3, i4) : srcMappingCenterRect(i, i2, i3, i4);
        Mapping mapping2 = new Mapping();
        mapping2.imageWidth = i3;
        mapping2.imageHeight = i4;
        mapping2.srcRect = srcMappingCenterRect;
        mapping2.destRect = rect;
        return mapping2;
    }

    @NonNull
    public String toString() {
        return KEY;
    }
}
