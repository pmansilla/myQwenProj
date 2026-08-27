package com.amap.api.maps.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.util.LongSparseArray;
import android.util.Log;
import com.amap.api.mapcore.util.ev;
import com.amap.api.maps.AMapException;
import com.autonavi.amap.mapcore.DPoint;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: classes.dex */
public class HeatmapTileProvider implements TileProvider {
    private static final int DEFAULT_MAX_ZOOM = 11;
    private static final int DEFAULT_MIN_ZOOM = 5;
    public static final double DEFAULT_OPACITY = 0.6d;
    public static final int DEFAULT_RADIUS = 12;
    private static final int MAX_RADIUS = 50;
    private static final int MAX_ZOOM_LEVEL = 21;
    private static final int MIN_RADIUS = 10;
    private static final int SCREEN_SIZE = 1280;
    private static final int TILE_DIM = 256;
    private ev mBounds;
    private int[] mColorMap;
    private Collection<WeightedLatLng> mData;
    private Gradient mGradient;
    private double[] mKernel;
    private double[] mMaxIntensity;
    private double mOpacity;
    private int mRadius;
    private a mTree;
    private static final int[] DEFAULT_GRADIENT_COLORS = {Color.rgb(102, 225, 0), Color.rgb(255, 0, 0)};
    private static final float[] DEFAULT_GRADIENT_START_POINTS = {0.2f, 1.0f};
    public static final Gradient DEFAULT_GRADIENT = new Gradient(DEFAULT_GRADIENT_COLORS, DEFAULT_GRADIENT_START_POINTS);

    /* loaded from: classes.dex */
    public static class Builder {
        private Collection<WeightedLatLng> data;
        private int radius = 12;
        private Gradient gradient = HeatmapTileProvider.DEFAULT_GRADIENT;
        private double opacity = 0.6d;

        public HeatmapTileProvider build() {
            if (this.data != null && this.data.size() != 0) {
                try {
                    return new HeatmapTileProvider(this);
                } catch (Throwable th) {
                    th.printStackTrace();
                    return null;
                }
            }
            try {
                throw new AMapException("No input points.");
            } catch (AMapException e) {
                Log.e("amap", e.getErrorMessage());
                e.printStackTrace();
                return null;
            }
        }

        public Builder data(Collection<LatLng> collection) {
            return weightedData(HeatmapTileProvider.d(collection));
        }

        public Builder gradient(Gradient gradient) {
            this.gradient = gradient;
            return this;
        }

        public Builder radius(int i) {
            this.radius = Math.max(10, Math.min(i, 50));
            return this;
        }

        public Builder transparency(double d) {
            this.opacity = Math.max(0.0d, Math.min(d, 1.0d));
            return this;
        }

        public Builder weightedData(Collection<WeightedLatLng> collection) {
            this.data = collection;
            return this;
        }
    }

    private HeatmapTileProvider(Builder builder) {
        this.mData = builder.data;
        this.mRadius = builder.radius;
        this.mGradient = builder.gradient;
        if (this.mGradient == null || !this.mGradient.isAvailable()) {
            this.mGradient = DEFAULT_GRADIENT;
        }
        this.mOpacity = builder.opacity;
        int i = this.mRadius;
        double d = this.mRadius;
        Double.isNaN(d);
        this.mKernel = a(i, d / 3.0d);
        a(this.mGradient);
        c(this.mData);
    }

    static double a(Collection<WeightedLatLng> collection, ev evVar, int i, int i2) {
        double d = evVar.a;
        double d2 = evVar.c;
        double d3 = evVar.b;
        double d4 = d2 - d;
        double d5 = evVar.d - d3;
        if (d4 <= d5) {
            d4 = d5;
        }
        double d6 = i2 / (i * 2);
        Double.isNaN(d6);
        double d7 = (int) (d6 + 0.5d);
        Double.isNaN(d7);
        double d8 = d7 / d4;
        LongSparseArray longSparseArray = new LongSparseArray();
        double d9 = 0.0d;
        for (WeightedLatLng weightedLatLng : collection) {
            double d10 = weightedLatLng.getPoint().x;
            int i3 = (int) ((weightedLatLng.getPoint().y - d3) * d8);
            long j = (int) ((d10 - d) * d8);
            LongSparseArray longSparseArray2 = (LongSparseArray) longSparseArray.get(j);
            if (longSparseArray2 == null) {
                longSparseArray2 = new LongSparseArray();
                longSparseArray.put(j, longSparseArray2);
            }
            long j2 = i3;
            Double d11 = (Double) longSparseArray2.get(j2);
            if (d11 == null) {
                d11 = Double.valueOf(0.0d);
            }
            LongSparseArray longSparseArray3 = longSparseArray;
            double d12 = d;
            Double valueOf = Double.valueOf(d11.doubleValue() + weightedLatLng.intensity);
            longSparseArray2.put(j2, valueOf);
            if (valueOf.doubleValue() > d9) {
                d9 = valueOf.doubleValue();
            }
            d = d12;
            longSparseArray = longSparseArray3;
        }
        return d9;
    }

    static Bitmap a(double[][] dArr, int[] iArr, double d) {
        int i = iArr[iArr.length - 1];
        double length = iArr.length - 1;
        Double.isNaN(length);
        double d2 = length / d;
        int length2 = dArr.length;
        int[] iArr2 = new int[length2 * length2];
        for (int i2 = 0; i2 < length2; i2++) {
            for (int i3 = 0; i3 < length2; i3++) {
                double d3 = dArr[i3][i2];
                int i4 = (i2 * length2) + i3;
                int i5 = (int) (d3 * d2);
                if (d3 == 0.0d) {
                    iArr2[i4] = 0;
                } else if (i5 < iArr.length) {
                    iArr2[i4] = iArr[i5];
                } else {
                    iArr2[i4] = i;
                }
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(length2, length2, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr2, 0, length2, 0, 0, length2, length2);
        return createBitmap;
    }

    static ev a(Collection<WeightedLatLng> collection) {
        Iterator<WeightedLatLng> it = collection.iterator();
        WeightedLatLng next = it.next();
        double d = next.getPoint().x;
        double d2 = next.getPoint().x;
        double d3 = d;
        double d4 = d2;
        double d5 = next.getPoint().y;
        double d6 = next.getPoint().y;
        while (it.hasNext()) {
            WeightedLatLng next2 = it.next();
            double d7 = next2.getPoint().x;
            double d8 = next2.getPoint().y;
            if (d7 < d3) {
                d3 = d7;
            }
            if (d7 > d4) {
                d4 = d7;
            }
            if (d8 < d5) {
                d5 = d8;
            }
            if (d8 > d6) {
                d6 = d8;
            }
        }
        return new ev(d3, d4, d5, d6);
    }

    private static Tile a(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Tile.obtain(256, 256, byteArrayOutputStream.toByteArray());
    }

    private void a(Gradient gradient) {
        this.mGradient = gradient;
        this.mColorMap = gradient.generateColorMap(this.mOpacity);
    }

    private double[] a(int i) {
        int i2;
        double[] dArr = new double[21];
        int i3 = 5;
        while (true) {
            if (i3 >= 11) {
                break;
            }
            dArr[i3] = a(this.mData, this.mBounds, i, (int) (Math.pow(2.0d, i3) * 1280.0d));
            if (i3 == 5) {
                for (int i4 = 0; i4 < i3; i4++) {
                    dArr[i4] = dArr[i3];
                }
            }
            i3++;
        }
        for (i2 = 11; i2 < 21; i2++) {
            dArr[i2] = dArr[10];
        }
        return dArr;
    }

    static double[] a(int i, double d) {
        double[] dArr = new double[(i * 2) + 1];
        for (int i2 = -i; i2 <= i; i2++) {
            double d2 = (-i2) * i2;
            Double.isNaN(d2);
            dArr[i2 + i] = Math.exp(d2 / ((2.0d * d) * d));
        }
        return dArr;
    }

    static double[][] a(double[][] dArr, double[] dArr2) {
        double length = dArr2.length;
        Double.isNaN(length);
        int floor = (int) Math.floor(length / 2.0d);
        int length2 = dArr.length;
        int i = length2 - (floor * 2);
        int i2 = (floor + i) - 1;
        double[][] dArr3 = (double[][]) Array.newInstance((Class<?>) double.class, length2, length2);
        for (int i3 = 0; i3 < length2; i3++) {
            for (int i4 = 0; i4 < length2; i4++) {
                double d = dArr[i3][i4];
                if (d != 0.0d) {
                    int i5 = i3 + floor;
                    if (i2 < i5) {
                        i5 = i2;
                    }
                    int i6 = i5 + 1;
                    int i7 = i3 - floor;
                    for (int i8 = floor > i7 ? floor : i7; i8 < i6; i8++) {
                        double[] dArr4 = dArr3[i8];
                        dArr4[i4] = dArr4[i4] + (dArr2[i8 - i7] * d);
                    }
                }
            }
        }
        double[][] dArr5 = (double[][]) Array.newInstance((Class<?>) double.class, i, i);
        for (int i9 = floor; i9 < i2 + 1; i9++) {
            for (int i10 = 0; i10 < length2; i10++) {
                double d2 = dArr3[i9][i10];
                if (d2 != 0.0d) {
                    int i11 = i10 + floor;
                    if (i2 < i11) {
                        i11 = i2;
                    }
                    int i12 = i11 + 1;
                    int i13 = i10 - floor;
                    for (int i14 = floor > i13 ? floor : i13; i14 < i12; i14++) {
                        double[] dArr6 = dArr5[i9 - floor];
                        int i15 = i14 - floor;
                        dArr6[i15] = dArr6[i15] + (dArr2[i14 - i13] * d2);
                    }
                }
            }
        }
        return dArr5;
    }

    private void c(Collection<WeightedLatLng> collection) {
        try {
            ArrayList arrayList = new ArrayList();
            for (WeightedLatLng weightedLatLng : collection) {
                if (weightedLatLng.latLng.latitude < 85.0d && weightedLatLng.latLng.latitude > -85.0d) {
                    arrayList.add(weightedLatLng);
                }
            }
            this.mData = arrayList;
            this.mBounds = a(this.mData);
            this.mTree = new a(this.mBounds);
            Iterator<WeightedLatLng> it = this.mData.iterator();
            while (it.hasNext()) {
                this.mTree.a(it.next());
            }
            this.mMaxIntensity = a(this.mRadius);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Collection<WeightedLatLng> d(Collection<LatLng> collection) {
        ArrayList arrayList = new ArrayList();
        Iterator<LatLng> it = collection.iterator();
        while (it.hasNext()) {
            arrayList.add(new WeightedLatLng(it.next()));
        }
        return arrayList;
    }

    @Override // com.amap.api.maps.model.TileProvider
    public Tile getTile(int i, int i2, int i3) {
        double d = 1.0d;
        double pow = 1.0d / Math.pow(2.0d, i3);
        double d2 = this.mRadius;
        Double.isNaN(d2);
        double d3 = (d2 * pow) / 256.0d;
        double d4 = (this.mRadius * 2) + 256;
        Double.isNaN(d4);
        double d5 = ((2.0d * d3) + pow) / d4;
        double d6 = i;
        Double.isNaN(d6);
        double d7 = (d6 * pow) - d3;
        double d8 = i + 1;
        Double.isNaN(d8);
        double d9 = (d8 * pow) + d3;
        double d10 = i2;
        Double.isNaN(d10);
        double d11 = (d10 * pow) - d3;
        double d12 = i2 + 1;
        Double.isNaN(d12);
        double d13 = (d12 * pow) + d3;
        Collection<WeightedLatLng> arrayList = new ArrayList<>();
        if (d7 < 0.0d) {
            d = -1.0d;
            arrayList = this.mTree.a(new ev(d7 + 1.0d, 1.0d, d11, d13));
        } else if (d9 > 1.0d) {
            arrayList = this.mTree.a(new ev(0.0d, d9 - 1.0d, d11, d13));
        } else {
            d = 0.0d;
        }
        ev evVar = new ev(d7, d9, d11, d13);
        if (!evVar.a(new ev(this.mBounds.a - d3, this.mBounds.c + d3, this.mBounds.b - d3, this.mBounds.d + d3))) {
            return TileProvider.NO_TILE;
        }
        Collection<WeightedLatLng> a = this.mTree.a(evVar);
        if (a.isEmpty()) {
            return TileProvider.NO_TILE;
        }
        double[][] dArr = (double[][]) Array.newInstance((Class<?>) double.class, (this.mRadius * 2) + 256, (this.mRadius * 2) + 256);
        for (Iterator<WeightedLatLng> it = a.iterator(); it.hasNext(); it = it) {
            WeightedLatLng next = it.next();
            DPoint point = next.getPoint();
            int i4 = (int) ((point.x - d7) / d5);
            int i5 = (int) ((point.y - d11) / d5);
            double[] dArr2 = dArr[i4];
            dArr2[i5] = dArr2[i5] + next.intensity;
        }
        for (WeightedLatLng weightedLatLng : arrayList) {
            DPoint point2 = weightedLatLng.getPoint();
            int i6 = (int) (((point2.x + d) - d7) / d5);
            int i7 = (int) ((point2.y - d11) / d5);
            double[] dArr3 = dArr[i6];
            dArr3[i7] = dArr3[i7] + weightedLatLng.intensity;
        }
        return a(a(a(dArr, this.mKernel), this.mColorMap, this.mMaxIntensity[i3]));
    }

    @Override // com.amap.api.maps.model.TileProvider
    public int getTileHeight() {
        return 256;
    }

    @Override // com.amap.api.maps.model.TileProvider
    public int getTileWidth() {
        return 256;
    }
}
