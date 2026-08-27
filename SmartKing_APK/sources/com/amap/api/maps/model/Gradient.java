package com.amap.api.maps.model;

import android.graphics.Color;
import android.util.Log;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.utils.SpatialRelationUtil;
import java.util.HashMap;

/* loaded from: classes.dex */
public class Gradient {
    private static final int DEFAULT_COLOR_MAP_SIZE = 1000;
    private boolean isAvailable;
    private int mColorMapSize;
    private int[] mColors;
    private float[] mStartPoints;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a {
        private final int a;
        private final int b;
        private final float c;

        private a(int i, int i2, float f) {
            this.a = i;
            this.b = i2;
            this.c = f;
        }
    }

    public Gradient(int[] iArr, float[] fArr) {
        this(iArr, fArr, 1000);
    }

    private Gradient(int[] iArr, float[] fArr, int i) {
        this.isAvailable = true;
        try {
            if (iArr == null || fArr == null) {
                throw new AMapException("colors and startPoints should not be null");
            }
            if (iArr.length != fArr.length) {
                throw new AMapException("colors and startPoints should be same length");
            }
            if (iArr.length == 0) {
                throw new AMapException("No colors have been defined");
            }
            for (int i2 = 1; i2 < fArr.length; i2++) {
                if (fArr[i2] <= fArr[i2 - 1]) {
                    throw new AMapException("startPoints should be in increasing order");
                }
            }
            this.mColorMapSize = i;
            this.mColors = new int[iArr.length];
            this.mStartPoints = new float[fArr.length];
            System.arraycopy(iArr, 0, this.mColors, 0, iArr.length);
            System.arraycopy(fArr, 0, this.mStartPoints, 0, fArr.length);
            this.isAvailable = true;
        } catch (AMapException e) {
            this.isAvailable = false;
            Log.e("amap", e.getErrorMessage());
            e.printStackTrace();
        }
    }

    static int a(int i, int i2, float f) {
        int alpha = (int) (((Color.alpha(i2) - Color.alpha(i)) * f) + Color.alpha(i));
        float[] fArr = new float[3];
        Color.RGBToHSV(Color.red(i), Color.green(i), Color.blue(i), fArr);
        float[] fArr2 = new float[3];
        Color.RGBToHSV(Color.red(i2), Color.green(i2), Color.blue(i2), fArr2);
        float f2 = 180;
        if (fArr[0] - fArr2[0] > f2) {
            fArr2[0] = fArr2[0] + SpatialRelationUtil.A_CIRCLE_DEGREE;
        } else if (fArr2[0] - fArr[0] > f2) {
            fArr[0] = fArr[0] + SpatialRelationUtil.A_CIRCLE_DEGREE;
        }
        float[] fArr3 = new float[3];
        for (int i3 = 0; i3 < 3; i3++) {
            fArr3[i3] = ((fArr2[i3] - fArr[i3]) * f) + fArr[i3];
        }
        return Color.HSVToColor(alpha, fArr3);
    }

    private HashMap<Integer, a> a() {
        HashMap<Integer, a> hashMap = new HashMap<>(32);
        if (this.mStartPoints[0] != 0.0f) {
            hashMap.put(0, new a(Color.argb(0, Color.red(this.mColors[0]), Color.green(this.mColors[0]), Color.blue(this.mColors[0])), this.mColors[0], this.mColorMapSize * this.mStartPoints[0]));
        }
        for (int i = 1; i < this.mColors.length; i++) {
            int i2 = i - 1;
            hashMap.put(Integer.valueOf((int) (this.mColorMapSize * this.mStartPoints[i2])), new a(this.mColors[i2], this.mColors[i], this.mColorMapSize * (this.mStartPoints[i] - this.mStartPoints[i2])));
        }
        if (this.mStartPoints[this.mStartPoints.length - 1] != 1.0f) {
            int length = this.mStartPoints.length - 1;
            hashMap.put(Integer.valueOf((int) (this.mColorMapSize * this.mStartPoints[length])), new a(this.mColors[length], this.mColors[length], this.mColorMapSize * (1.0f - this.mStartPoints[length])));
        }
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int[] generateColorMap(double d) {
        HashMap<Integer, a> a2 = a();
        int[] iArr = new int[this.mColorMapSize];
        a aVar = a2.get(0);
        int i = 0;
        for (int i2 = 0; i2 < this.mColorMapSize; i2++) {
            if (a2.containsKey(Integer.valueOf(i2))) {
                aVar = a2.get(Integer.valueOf(i2));
                i = i2;
            }
            iArr[i2] = a(aVar.a, aVar.b, (i2 - i) / aVar.c);
        }
        if (d != 1.0d) {
            for (int i3 = 0; i3 < this.mColorMapSize; i3++) {
                int i4 = iArr[i3];
                double alpha = Color.alpha(i4);
                Double.isNaN(alpha);
                iArr[i3] = Color.argb((int) (alpha * d), Color.red(i4), Color.green(i4), Color.blue(i4));
            }
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isAvailable() {
        return this.isAvailable;
    }
}
