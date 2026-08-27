package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.amap.api.mapcore.util.ho;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BaseHoleOptions;
import com.amap.api.maps.model.CircleHoleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolygonHoleOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.AbstractCameraUpdateMessage;
import com.autonavi.amap.mapcore.AeUtil;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.FPoint3;
import com.autonavi.amap.mapcore.FileUtil;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.VirtualEarthProjection;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* compiled from: Util.java */
/* loaded from: classes.dex */
public class fr {
    private static FPoint[] a = {FPoint.obtain(), FPoint.obtain(), FPoint.obtain(), FPoint.obtain()};
    private static List<Float> b = new ArrayList(4);
    private static List<Float> c = new ArrayList(4);

    public static double a(double d, double d2, double d3, double d4, double d5, double d6) {
        return ((d3 - d) * (d6 - d2)) - ((d5 - d) * (d4 - d2));
    }

    public static double a(float f, double d, double d2) {
        double d3 = f;
        Double.isNaN(d3);
        return 20.0d - (Math.log(d2 / (d * d3)) / Math.log(2.0d));
    }

    public static float a(float f, float f2) {
        if (f <= 40.0f) {
            return f;
        }
        float f3 = f2 <= 15.0f ? 40 : f2 <= 16.0f ? 56 : f2 <= 17.0f ? 66 : f2 <= 18.0f ? 74 : f2 <= 18.0f ? 78 : 80;
        return f > f3 ? f3 : f;
    }

    private static float a(float f, float f2, double d) {
        double pow = Math.pow(2.0d, 20.0f - f2);
        double d2 = f;
        Double.isNaN(d2);
        return (float) (d / (pow * d2));
    }

    private static float a(float f, float f2, float f3) {
        double d = f3;
        double pow = Math.pow(2.0d, 20.0f - f2);
        Double.isNaN(d);
        double d2 = f;
        Double.isNaN(d2);
        return (float) (d * pow * d2);
    }

    public static float a(DPoint dPoint, DPoint dPoint2) {
        if (dPoint == null || dPoint2 == null) {
            return 0.0f;
        }
        double d = dPoint.x;
        double d2 = dPoint2.x;
        return (float) ((Math.atan2(dPoint2.y - dPoint.y, d2 - d) / 3.141592653589793d) * 180.0d);
    }

    public static float a(MapConfig mapConfig, float f) {
        if (mapConfig != null) {
            return f > mapConfig.maxZoomLevel ? mapConfig.maxZoomLevel : f < mapConfig.minZoomLevel ? mapConfig.minZoomLevel : f;
        }
        if (f > 20.0f) {
            return 20.0f;
        }
        if (f < 3.0f) {
            return 3.0f;
        }
        return f;
    }

    public static int a(int i, int i2) {
        return a(0, Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888), true);
    }

    public static int a(int i, Bitmap bitmap, int i2, int i3) {
        if (bitmap == null || bitmap.isRecycled()) {
            return 0;
        }
        if (i == 0) {
            int[] iArr = {0};
            GLES20.glGenTextures(1, iArr, 0);
            i = iArr[0];
        }
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLUtils.texSubImage2D(3553, 0, i2, i3, bitmap);
        return i;
    }

    public static int a(int i, Bitmap bitmap, boolean z) {
        int b2 = b(i, bitmap, z);
        if (bitmap != null) {
            bitmap.recycle();
        }
        return b2;
    }

    public static int a(Bitmap bitmap) {
        return a(bitmap, false);
    }

    public static int a(Bitmap bitmap, boolean z) {
        return a(0, bitmap, z);
    }

    public static int a(Object[] objArr) {
        return Arrays.hashCode(objArr);
    }

    public static Bitmap a(int i, int i2, int i3, int i4) {
        int i5 = i3 * i4;
        try {
            int[] iArr = new int[i5];
            int[] iArr2 = new int[i5];
            IntBuffer wrap = IntBuffer.wrap(iArr);
            wrap.position(0);
            GLES20.glReadPixels(i, i2, i3, i4, 6408, 5121, wrap);
            for (int i6 = 0; i6 < i4; i6++) {
                for (int i7 = 0; i7 < i3; i7++) {
                    int i8 = iArr[(i6 * i3) + i7];
                    iArr2[(((i4 - i6) - 1) * i3) + i7] = (i8 & (-16711936)) | ((i8 << 16) & 16711680) | ((i8 >> 16) & 255);
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(i3, i4, Bitmap.Config.ARGB_8888);
            createBitmap.setPixels(iArr2, 0, i3, 0, 0, i3, i4);
            return createBitmap;
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImpGLSurfaceView", "SavePixels");
            th.printStackTrace();
            return null;
        }
    }

    public static Bitmap a(Context context, String str) {
        try {
            InputStream open = fl.a(context).open(str);
            Bitmap decodeStream = BitmapFactory.decodeStream(open);
            open.close();
            return decodeStream;
        } catch (Throwable th) {
            ic.c(th, "Util", "fromAsset");
            return null;
        }
    }

    public static Bitmap a(Bitmap bitmap, float f) {
        if (bitmap == null) {
            return null;
        }
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * f), (int) (bitmap.getHeight() * f), true);
    }

    public static Bitmap a(View view) {
        try {
            b(view);
            view.destroyDrawingCache();
            view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            Bitmap drawingCache = view.getDrawingCache();
            if (drawingCache != null) {
                return drawingCache.copy(Bitmap.Config.ARGB_8888, false);
            }
            return null;
        } catch (Throwable th) {
            ic.c(th, "Utils", "getBitmapFromView");
            th.printStackTrace();
            return null;
        }
    }

    public static Pair<Float, IPoint> a(AbstractCameraUpdateMessage abstractCameraUpdateMessage, GLMapState gLMapState, MapConfig mapConfig) {
        return a(mapConfig, Math.max(abstractCameraUpdateMessage.paddingLeft, 1), Math.max(abstractCameraUpdateMessage.paddingRight, 1), Math.max(abstractCameraUpdateMessage.paddingTop, 1), Math.max(abstractCameraUpdateMessage.paddingBottom, 1), abstractCameraUpdateMessage.bounds, abstractCameraUpdateMessage.width, abstractCameraUpdateMessage.height);
    }

    public static Pair<Float, Boolean> a(MapConfig mapConfig, int i, int i2, int i3, int i4, int i5, int i6) {
        float min;
        mapConfig.getSZ();
        if (i == i3 && i2 == i4) {
            min = mapConfig.getMaxZoomLevel();
        } else {
            float a2 = (float) a(mapConfig.getMapZoomScale(), i6, Math.abs(i4 - i2));
            float a3 = (float) a(mapConfig.getMapZoomScale(), i5, Math.abs(i3 - i));
            float min2 = Math.min(a3, a2);
            r0 = min2 == a3;
            min = Math.min(mapConfig.getMaxZoomLevel(), Math.max(mapConfig.getMinZoomLevel(), min2));
        }
        return new Pair<>(Float.valueOf(min), Boolean.valueOf(r0));
    }

    public static Pair<Float, IPoint> a(MapConfig mapConfig, int i, int i2, int i3, int i4, LatLngBounds latLngBounds, int i5, int i6) {
        int i7;
        int i8;
        if (latLngBounds == null || latLngBounds.northeast == null || latLngBounds.southwest == null || mapConfig == null) {
            return null;
        }
        Point latLongToPixels = VirtualEarthProjection.latLongToPixels(latLngBounds.northeast.latitude, latLngBounds.northeast.longitude, 20);
        Point latLongToPixels2 = VirtualEarthProjection.latLongToPixels(latLngBounds.southwest.latitude, latLngBounds.southwest.longitude, 20);
        int i9 = latLongToPixels.x - latLongToPixels2.x;
        int i10 = latLongToPixels2.y - latLongToPixels.y;
        int i11 = i5 - (i + i2);
        int i12 = i6 - (i3 + i4);
        if (i9 < 0 && i10 < 0) {
            return null;
        }
        int i13 = i9 <= 0 ? 1 : i9;
        int i14 = i10 <= 0 ? 1 : i10;
        int i15 = i13;
        Pair<Float, Boolean> a2 = a(mapConfig, latLongToPixels.x, latLongToPixels.y, latLongToPixels2.x, latLongToPixels2.y, i11 <= 0 ? 1 : i11, i12 <= 0 ? 1 : i12);
        float floatValue = ((Float) a2.first).floatValue();
        boolean booleanValue = ((Boolean) a2.second).booleanValue();
        float a3 = a(mapConfig.getMapZoomScale(), floatValue, i15);
        float a4 = a(mapConfig.getMapZoomScale(), floatValue, i14);
        if (floatValue >= mapConfig.maxZoomLevel) {
            i7 = (int) (latLongToPixels2.x + ((((i2 - i) + a3) * i15) / (a3 * 2.0f)));
            i8 = (int) (latLongToPixels.y + ((((i4 - i3) + a4) * i14) / (a4 * 2.0f)));
        } else if (booleanValue) {
            i7 = (int) (latLongToPixels2.x + ((((i5 / 2) - i) / a3) * i15));
            i8 = (int) (latLongToPixels.y + ((((i4 - i3) + a4) * i14) / (a4 * 2.0f)));
        } else {
            i7 = (int) (latLongToPixels2.x + ((((i2 - i) + a3) * i15) / (a3 * 2.0f)));
            i8 = (int) (latLongToPixels.y + ((((i6 / 2) - i3) / a4) * i14));
        }
        return new Pair<>(Float.valueOf(floatValue), IPoint.obtain((int) (i7 + a(mapConfig.getMapZoomScale(), floatValue, mapConfig.getAnchorX() - (mapConfig.getMapWidth() >> 1))), (int) (i8 + a(mapConfig.getMapZoomScale(), floatValue, mapConfig.getAnchorY() - (mapConfig.getMapHeight() >> 1)))));
    }

    public static DPoint a(LatLng latLng) {
        double d = (latLng.longitude / 360.0d) + 0.5d;
        double sin = Math.sin(Math.toRadians(latLng.latitude));
        return DPoint.obtain(d * 1.0d, (((Math.log((sin + 1.0d) / (1.0d - sin)) * 0.5d) / (-6.283185307179586d)) + 0.5d) * 1.0d);
    }

    private static FPoint3 a(FPoint fPoint, FPoint fPoint2, FPoint3 fPoint3, FPoint3 fPoint32, int i) {
        FPoint3 fPoint33 = new FPoint3(0.0f, 0.0f, i);
        double d = ((fPoint2.y - fPoint.y) * (fPoint.x - fPoint3.x)) - ((fPoint2.x - fPoint.x) * (fPoint.y - fPoint3.y));
        double d2 = ((fPoint2.y - fPoint.y) * (fPoint32.x - fPoint3.x)) - ((fPoint2.x - fPoint.x) * (fPoint32.y - fPoint3.y));
        double d3 = fPoint3.x;
        double d4 = fPoint32.x - fPoint3.x;
        Double.isNaN(d4);
        Double.isNaN(d);
        Double.isNaN(d2);
        Double.isNaN(d3);
        fPoint33.x = (float) (d3 + ((d4 * d) / d2));
        double d5 = fPoint3.y;
        double d6 = fPoint32.y - fPoint3.y;
        Double.isNaN(d6);
        Double.isNaN(d);
        Double.isNaN(d2);
        Double.isNaN(d5);
        fPoint33.y = (float) (d5 + ((d6 * d) / d2));
        return fPoint33;
    }

    private static FPoint a(FPoint fPoint, FPoint fPoint2, FPoint fPoint3, FPoint fPoint4) {
        FPoint obtain = FPoint.obtain(0.0f, 0.0f);
        double d = ((fPoint2.y - fPoint.y) * (fPoint.x - fPoint3.x)) - ((fPoint2.x - fPoint.x) * (fPoint.y - fPoint3.y));
        double d2 = ((fPoint2.y - fPoint.y) * (fPoint4.x - fPoint3.x)) - ((fPoint2.x - fPoint.x) * (fPoint4.y - fPoint3.y));
        double d3 = fPoint3.x;
        double d4 = fPoint4.x - fPoint3.x;
        Double.isNaN(d4);
        Double.isNaN(d);
        Double.isNaN(d2);
        Double.isNaN(d3);
        obtain.x = (float) (d3 + ((d4 * d) / d2));
        double d5 = fPoint3.y;
        double d6 = fPoint4.y - fPoint3.y;
        Double.isNaN(d6);
        Double.isNaN(d);
        Double.isNaN(d2);
        Double.isNaN(d5);
        obtain.y = (float) (d5 + ((d6 * d) / d2));
        return obtain;
    }

    private static IPoint a(IPoint iPoint, IPoint iPoint2, IPoint iPoint3, IPoint iPoint4) {
        IPoint obtain = IPoint.obtain(0, 0);
        double d = iPoint2.y - iPoint.y;
        double d2 = iPoint.x - iPoint3.x;
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = d * d2;
        double d4 = iPoint2.x - iPoint.x;
        double d5 = iPoint.y - iPoint3.y;
        Double.isNaN(d4);
        Double.isNaN(d5);
        double d6 = d3 - (d4 * d5);
        double d7 = iPoint2.y - iPoint.y;
        double d8 = iPoint4.x - iPoint3.x;
        Double.isNaN(d7);
        Double.isNaN(d8);
        double d9 = d7 * d8;
        double d10 = iPoint2.x - iPoint.x;
        double d11 = iPoint4.y - iPoint3.y;
        Double.isNaN(d10);
        Double.isNaN(d11);
        double d12 = d9 - (d10 * d11);
        double d13 = iPoint3.x;
        double d14 = iPoint4.x - iPoint3.x;
        Double.isNaN(d14);
        Double.isNaN(d13);
        obtain.x = (int) (d13 + ((d14 * d6) / d12));
        double d15 = iPoint3.y;
        double d16 = iPoint4.y - iPoint3.y;
        Double.isNaN(d16);
        Double.isNaN(d15);
        obtain.y = (int) (d15 + ((d16 * d6) / d12));
        return obtain;
    }

    public static String a(int i) {
        if (i < 1000) {
            return i + "m";
        }
        return (i / 1000) + "km";
    }

    public static String a(Context context) {
        File file = new File(FileUtil.getMapBaseStorage(context), AeUtil.ROOT_DATA_PATH_NAME);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(file.toString() + File.separator);
        if (!file2.exists()) {
            file2.mkdir();
        }
        return file.toString() + File.separator;
    }

    public static String a(File file) {
        BufferedReader bufferedReader;
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2;
        StringBuffer stringBuffer = new StringBuffer();
        FileInputStream fileInputStream3 = null;
        try {
            try {
                try {
                    FileInputStream fileInputStream4 = new FileInputStream(file);
                    try {
                        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(fileInputStream4, "utf-8"));
                        while (true) {
                            try {
                                String readLine = bufferedReader2.readLine();
                                if (readLine != null) {
                                    stringBuffer.append(readLine);
                                } else {
                                    try {
                                        try {
                                            break;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            bufferedReader2.close();
                                        }
                                    } catch (Throwable th) {
                                        try {
                                            bufferedReader2.close();
                                        } catch (IOException e2) {
                                            e2.printStackTrace();
                                        }
                                        throw th;
                                    }
                                }
                            } catch (FileNotFoundException e3) {
                                fileInputStream2 = fileInputStream4;
                                bufferedReader = bufferedReader2;
                                e = e3;
                                fileInputStream3 = fileInputStream2;
                                ic.c(e, "Util", "readFile fileNotFound");
                                e.printStackTrace();
                                try {
                                    if (fileInputStream3 != null) {
                                        try {
                                            fileInputStream3.close();
                                        } catch (IOException e4) {
                                            e4.printStackTrace();
                                            if (bufferedReader != null) {
                                                bufferedReader.close();
                                            }
                                            return stringBuffer.toString();
                                        }
                                    }
                                    if (bufferedReader != null) {
                                        bufferedReader.close();
                                    }
                                    return stringBuffer.toString();
                                } finally {
                                }
                            } catch (IOException e5) {
                                fileInputStream = fileInputStream4;
                                bufferedReader = bufferedReader2;
                                e = e5;
                                fileInputStream3 = fileInputStream;
                                ic.c(e, "Util", "readFile io");
                                e.printStackTrace();
                                if (fileInputStream3 != null) {
                                    try {
                                        try {
                                            fileInputStream3.close();
                                        } catch (IOException e6) {
                                            e6.printStackTrace();
                                            if (bufferedReader != null) {
                                                bufferedReader.close();
                                            }
                                            return stringBuffer.toString();
                                        }
                                    } finally {
                                    }
                                }
                                if (bufferedReader != null) {
                                    bufferedReader.close();
                                }
                                return stringBuffer.toString();
                            } catch (Throwable th2) {
                                th = th2;
                                fileInputStream3 = fileInputStream4;
                                bufferedReader = bufferedReader2;
                                try {
                                    if (fileInputStream3 != null) {
                                        try {
                                            try {
                                                fileInputStream3.close();
                                            } catch (IOException e7) {
                                                e7.printStackTrace();
                                                if (bufferedReader != null) {
                                                    bufferedReader.close();
                                                }
                                                throw th;
                                            }
                                        } finally {
                                            if (bufferedReader != null) {
                                                try {
                                                    bufferedReader.close();
                                                } catch (IOException e8) {
                                                    e8.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                    if (bufferedReader != null) {
                                        bufferedReader.close();
                                    }
                                } catch (IOException e9) {
                                    e9.printStackTrace();
                                }
                                throw th;
                            }
                        }
                        fileInputStream4.close();
                        bufferedReader2.close();
                    } catch (FileNotFoundException e10) {
                        e = e10;
                        fileInputStream2 = fileInputStream4;
                        bufferedReader = null;
                    } catch (IOException e11) {
                        e = e11;
                        fileInputStream = fileInputStream4;
                        bufferedReader = null;
                    } catch (Throwable th3) {
                        th = th3;
                        bufferedReader = null;
                        fileInputStream3 = fileInputStream4;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            } catch (FileNotFoundException e12) {
                e = e12;
                bufferedReader = null;
            } catch (IOException e13) {
                e = e13;
                bufferedReader = null;
            } catch (Throwable th5) {
                th = th5;
                bufferedReader = null;
            }
        } catch (IOException e14) {
            e14.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public static String a(InputStream inputStream) {
        try {
            return new String(b(inputStream), "utf-8");
        } catch (Throwable th) {
            ic.c(th, "Util", "decodeAssetResData");
            th.printStackTrace();
            return null;
        }
    }

    public static String a(String str, Object obj) {
        return str + "=" + String.valueOf(obj);
    }

    public static String a(String... strArr) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String str : strArr) {
            sb.append(str);
            if (i != strArr.length - 1) {
                sb.append(",");
            }
            i++;
        }
        return sb.toString();
    }

    public static FloatBuffer a(float[] fArr) {
        try {
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
            allocateDirect.order(ByteOrder.nativeOrder());
            FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
            asFloatBuffer.put(fArr);
            asFloatBuffer.position(0);
            return asFloatBuffer;
        } catch (Throwable th) {
            ic.c(th, "Util", "makeFloatBuffer1");
            th.printStackTrace();
            return null;
        }
    }

    public static FloatBuffer a(float[] fArr, FloatBuffer floatBuffer) {
        try {
            floatBuffer.clear();
            floatBuffer.put(fArr);
            floatBuffer.position(0);
            return floatBuffer;
        } catch (Throwable th) {
            ic.c(th, "Util", "makeFloatBuffer2");
            th.printStackTrace();
            return null;
        }
    }

    public static List<FPoint> a(FPoint[] fPointArr, List<FPoint> list, boolean z) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(list);
        for (byte b2 = 0; b2 < 4; b2 = (byte) (b2 + 1)) {
            arrayList.clear();
            int size = arrayList2.size();
            int i = 0;
            while (true) {
                if (i >= (z ? size : size - 1)) {
                    break;
                }
                FPoint fPoint = (FPoint) arrayList2.get(i % size);
                int i2 = i + 1;
                FPoint fPoint2 = (FPoint) arrayList2.get(i2 % size);
                if (i == 0 && a(fPoint, fPointArr[b2], fPointArr[(b2 + 1) % fPointArr.length])) {
                    arrayList.add(fPoint);
                }
                int i3 = b2 + 1;
                if (a(fPoint, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                    if (a(fPoint2, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                        arrayList.add(fPoint2);
                    } else {
                        arrayList.add(a(fPointArr[b2], fPointArr[i3 % fPointArr.length], fPoint, fPoint2));
                    }
                } else if (a(fPoint2, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                    arrayList.add(a(fPointArr[b2], fPointArr[i3 % fPointArr.length], fPoint, fPoint2));
                    arrayList.add(fPoint2);
                }
                i = i2;
            }
            arrayList2.clear();
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                arrayList2.add(arrayList.get(i4));
            }
        }
        return arrayList2;
    }

    public static List<IPoint> a(IPoint[] iPointArr, List<IPoint> list, boolean z) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(list);
        for (byte b2 = 0; b2 < 4; b2 = (byte) (b2 + 1)) {
            arrayList.clear();
            int size = arrayList2.size();
            int i = 0;
            while (true) {
                if (i >= (z ? size : size - 1)) {
                    break;
                }
                IPoint iPoint = (IPoint) arrayList2.get(i % size);
                int i2 = i + 1;
                IPoint iPoint2 = (IPoint) arrayList2.get(i2 % size);
                if (i == 0 && a(iPoint, iPointArr[b2], iPointArr[(b2 + 1) % iPointArr.length])) {
                    arrayList.add(iPoint);
                }
                int i3 = b2 + 1;
                if (a(iPoint, iPointArr[b2], iPointArr[i3 % iPointArr.length])) {
                    if (a(iPoint2, iPointArr[b2], iPointArr[i3 % iPointArr.length])) {
                        arrayList.add(iPoint2);
                    } else {
                        arrayList.add(a(iPointArr[b2], iPointArr[i3 % iPointArr.length], iPoint, iPoint2));
                    }
                } else if (a(iPoint2, iPointArr[b2], iPointArr[i3 % iPointArr.length])) {
                    arrayList.add(a(iPointArr[b2], iPointArr[i3 % iPointArr.length], iPoint, iPoint2));
                    arrayList.add(iPoint2);
                }
                i = i2;
            }
            arrayList2.clear();
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                arrayList2.add(arrayList.get(i4));
            }
        }
        return arrayList2;
    }

    public static void a(Rect rect) {
        if (rect != null) {
            rect.set(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    public static void a(Drawable drawable) {
        if (drawable != null) {
            drawable.setCallback(null);
        }
    }

    public static boolean a() {
        return Build.VERSION.SDK_INT >= 8;
    }

    public static boolean a(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        double d9 = d3 - d;
        double d10 = d8 - d6;
        double d11 = d4 - d2;
        double d12 = d7 - d5;
        double d13 = (d9 * d10) - (d11 * d12);
        if (d13 != 0.0d) {
            double d14 = d2 - d6;
            double d15 = d - d5;
            double d16 = ((d12 * d14) - (d10 * d15)) / d13;
            double d17 = ((d14 * d9) - (d15 * d11)) / d13;
            if (d16 >= 0.0d && d16 <= 1.0d && d17 >= 0.0d && d17 <= 1.0d) {
                return true;
            }
        }
        return false;
    }

    private static boolean a(int i, int i2, IPoint iPoint, IPoint iPoint2) {
        double d = iPoint2.x - iPoint.x;
        double d2 = i2 - iPoint.y;
        double d3 = i - iPoint.x;
        double d4 = iPoint2.y - iPoint.y;
        Double.isNaN(d);
        Double.isNaN(d2);
        Double.isNaN(d3);
        Double.isNaN(d4);
        return (d * d2) - (d3 * d4) >= 0.0d;
    }

    public static boolean a(Rect rect, int i, int i2) {
        return rect.contains(i, i2);
    }

    public static boolean a(BaseHoleOptions baseHoleOptions, LatLng latLng) {
        if (baseHoleOptions instanceof CircleHoleOptions) {
            CircleHoleOptions circleHoleOptions = (CircleHoleOptions) baseHoleOptions;
            LatLng center = circleHoleOptions.getCenter();
            return center != null && ((double) AMapUtils.calculateLineDistance(center, latLng)) <= circleHoleOptions.getRadius();
        }
        List<LatLng> points = ((PolygonHoleOptions) baseHoleOptions).getPoints();
        if (points == null || points.size() == 0) {
            return false;
        }
        return a(latLng, points);
    }

    public static boolean a(CircleHoleOptions circleHoleOptions, CircleHoleOptions circleHoleOptions2) {
        try {
            return ((double) AMapUtils.calculateLineDistance(circleHoleOptions2.getCenter(), circleHoleOptions.getCenter())) < circleHoleOptions.getRadius() + circleHoleOptions2.getRadius();
        } catch (Throwable th) {
            ic.c(th, "Util", "isPolygon2CircleIntersect");
            th.printStackTrace();
            return false;
        }
    }

    public static boolean a(LatLng latLng, List<LatLng> list) {
        double d;
        List<LatLng> list2 = list;
        double d2 = latLng.longitude;
        double d3 = latLng.latitude;
        double d4 = latLng.latitude;
        if (list.size() < 3) {
            return false;
        }
        if (!list2.get(0).equals(list2.get(list.size() - 1))) {
            list2.add(list2.get(0));
        }
        int i = 0;
        int i2 = 0;
        while (i < list.size() - 1) {
            double d5 = list2.get(i).longitude;
            double d6 = list2.get(i).latitude;
            int i3 = i + 1;
            double d7 = list2.get(i3).longitude;
            double d8 = list2.get(i3).latitude;
            double d9 = d4;
            double d10 = d3;
            if (b(d2, d3, d5, d6, d7, d8)) {
                return true;
            }
            if (Math.abs(d8 - d6) >= 1.0E-9d) {
                if (b(d5, d6, d2, d10, 180.0d, d9)) {
                    if (d6 > d8) {
                        i2++;
                    }
                } else if (!b(d7, d8, d2, d10, 180.0d, d9)) {
                    d = d2;
                    if (a(d5, d6, d7, d8, d2, d10, 180.0d, d9)) {
                        i2++;
                    }
                    d2 = d;
                    i = i3;
                    d4 = d9;
                    d3 = d10;
                    list2 = list;
                } else if (d8 > d6) {
                    i2++;
                }
            }
            d = d2;
            d2 = d;
            i = i3;
            d4 = d9;
            d3 = d10;
            list2 = list;
        }
        return i2 % 2 != 0;
    }

    private static boolean a(FPoint fPoint, FPoint fPoint2, FPoint fPoint3) {
        return ((double) (((fPoint3.x - fPoint2.x) * (fPoint.y - fPoint2.y)) - ((fPoint.x - fPoint2.x) * (fPoint3.y - fPoint2.y)))) >= 0.0d;
    }

    public static boolean a(FPoint fPoint, FPoint[] fPointArr) {
        if (fPointArr == null) {
            return false;
        }
        byte b2 = 0;
        while (b2 < fPointArr.length) {
            FPoint fPoint2 = fPointArr[b2];
            int i = b2 + 1;
            if (!a(fPoint, fPoint2, fPointArr[i % fPointArr.length])) {
                return false;
            }
            b2 = (byte) i;
        }
        return true;
    }

    private static boolean a(IPoint iPoint, IPoint iPoint2, IPoint iPoint3) {
        return a(iPoint.x, iPoint.y, iPoint2, iPoint3);
    }

    public static boolean a(List<IPoint> list, int i, int i2) {
        if (i2 < 3) {
            return false;
        }
        int i3 = i2 - 1;
        double d = 0.0d;
        for (int i4 = 0; i4 < i2; i4++) {
            IPoint iPoint = list.get(i3);
            IPoint iPoint2 = list.get(i4);
            double d2 = iPoint.x;
            Double.isNaN(d2);
            double d3 = iPoint2.y;
            Double.isNaN(d3);
            double d4 = (d2 / 1000000.0d) * (d3 / 1000000.0d);
            double d5 = iPoint2.x;
            Double.isNaN(d5);
            double d6 = iPoint.y;
            Double.isNaN(d6);
            d += d4 - ((d5 / 1000000.0d) * (d6 / 1000000.0d));
            i3 = i4;
        }
        return d < 0.0d;
    }

    public static boolean a(List<BaseHoleOptions> list, CircleHoleOptions circleHoleOptions) {
        boolean z = false;
        for (int i = 0; i < list.size(); i++) {
            BaseHoleOptions baseHoleOptions = list.get(i);
            if (baseHoleOptions instanceof PolygonHoleOptions) {
                z = b(((PolygonHoleOptions) baseHoleOptions).getPoints(), circleHoleOptions);
                if (z) {
                    return true;
                }
            } else if ((baseHoleOptions instanceof CircleHoleOptions) && (z = a(circleHoleOptions, (CircleHoleOptions) baseHoleOptions))) {
                return true;
            }
        }
        return z;
    }

    public static boolean a(List<BaseHoleOptions> list, PolygonHoleOptions polygonHoleOptions) {
        boolean z = false;
        for (int i = 0; i < list.size(); i++) {
            BaseHoleOptions baseHoleOptions = list.get(i);
            if (baseHoleOptions instanceof PolygonHoleOptions) {
                z = a(((PolygonHoleOptions) baseHoleOptions).getPoints(), polygonHoleOptions.getPoints());
                if (z) {
                    return true;
                }
            } else if (baseHoleOptions instanceof CircleHoleOptions) {
                z = b(polygonHoleOptions.getPoints(), (CircleHoleOptions) baseHoleOptions);
                if (z) {
                    return true;
                }
            } else {
                continue;
            }
        }
        return z;
    }

    public static boolean a(List<LatLng> list, List<LatLng> list2) {
        for (int i = 0; i < list2.size(); i++) {
            try {
                if (a(list2.get(i), list)) {
                    return true;
                }
            } catch (Throwable th) {
                ic.c(th, "Util", "isPolygon2PolygonIntersect");
                th.printStackTrace();
                return false;
            }
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (a(list.get(i2), list2)) {
                return true;
            }
        }
        return b(list, list2);
    }

    public static byte[] a(byte[] bArr, int i) {
        return a(bArr, i, i, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0028, code lost:
    
        r2.setPixel(r5, r6, r9);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static byte[] a(byte[] r7, int r8, int r9, boolean r10) {
        /*
            int r0 = r7.length     // Catch: java.lang.Throwable -> L40
            r1 = 0
            android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeByteArray(r7, r1, r0)     // Catch: java.lang.Throwable -> L40
            android.graphics.Bitmap$Config r2 = r0.getConfig()     // Catch: java.lang.Throwable -> L40
            r3 = 1
            android.graphics.Bitmap r2 = r0.copy(r2, r3)     // Catch: java.lang.Throwable -> L40
            int r3 = r0.getWidth()     // Catch: java.lang.Throwable -> L40
            int r4 = r0.getHeight()     // Catch: java.lang.Throwable -> L40
            r5 = 0
        L18:
            if (r5 >= r3) goto L31
            r6 = 0
        L1b:
            if (r6 >= r4) goto L2e
            if (r5 == 0) goto L26
            if (r6 != 0) goto L22
            goto L26
        L22:
            r2.setPixel(r5, r6, r8)     // Catch: java.lang.Throwable -> L40
            goto L2b
        L26:
            if (r10 != 0) goto L2b
            r2.setPixel(r5, r6, r9)     // Catch: java.lang.Throwable -> L40
        L2b:
            int r6 = r6 + 1
            goto L1b
        L2e:
            int r5 = r5 + 1
            goto L18
        L31:
            byte[] r8 = b(r2)     // Catch: java.lang.Throwable -> L40
            if (r8 != 0) goto L38
            r8 = r7
        L38:
            r2.recycle()     // Catch: java.lang.Throwable -> L40
            r0.recycle()     // Catch: java.lang.Throwable -> L40
            r7 = r8
            goto L44
        L40:
            r8 = move-exception
            r8.printStackTrace()
        L44:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.fr.a(byte[], int, int, boolean):byte[]");
    }

    public static byte[] a(byte[] bArr, int[] iArr) {
        try {
            int i = 0;
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            int i2 = 1;
            Bitmap copy = decodeByteArray.copy(decodeByteArray.getConfig(), true);
            decodeByteArray.getWidth();
            decodeByteArray.getHeight();
            int i3 = 6;
            int i4 = 6;
            while (i2 < 72) {
                int i5 = i3;
                int i6 = i;
                for (int i7 = 8; i7 < 12; i7++) {
                    decodeByteArray.getPixel(i7, i2);
                    if (i2 < 4 * i4) {
                        copy.setPixel(i7, i2, iArr[i6]);
                    } else {
                        i6++;
                        i5--;
                        i4 += i5;
                    }
                }
                i2++;
                i = i6;
                i3 = i5;
            }
            byte[] b2 = b(copy);
            if (b2 == null) {
                b2 = bArr;
            }
            copy.recycle();
            decodeByteArray.recycle();
            return b2;
        } catch (Throwable th) {
            th.printStackTrace();
            return bArr;
        }
    }

    public static synchronized int[] a(int i, int i2, int i3, int i4, MapConfig mapConfig, GLMapState gLMapState, int i5, int i6) {
        int[] iArr;
        synchronized (fr.class) {
            int mapWidth = mapConfig.getMapWidth();
            int mapHeight = mapConfig.getMapHeight();
            iArr = new int[]{(int) Math.max(i3 + a(mapConfig.getMapZoomScale(), gLMapState.getMapZoomer(), mapConfig.getAnchorX()), Math.min(i5, i - a(mapConfig.getMapZoomScale(), gLMapState.getMapZoomer(), mapWidth - r3))), (int) Math.max(i2 + a(mapConfig.getMapZoomScale(), gLMapState.getMapZoomer(), mapConfig.getAnchorY()), Math.min(i6, i4 - a(mapConfig.getMapZoomScale(), gLMapState.getMapZoomer(), mapHeight - r4)))};
        }
        return iArr;
    }

    public static FPoint[] a(ad adVar, boolean z) {
        int i;
        int i2;
        float skyHeight = adVar.getSkyHeight();
        if (z) {
            i = 100;
            i2 = 10;
        } else {
            i = 0;
            i2 = 0;
        }
        FPoint obtain = FPoint.obtain();
        int i3 = -i;
        int i4 = (int) (skyHeight - i2);
        adVar.a(i3, i4, (PointF) obtain);
        a[0].set(obtain.x, obtain.y);
        FPoint obtain2 = FPoint.obtain();
        adVar.a(adVar.getMapWidth() + i, i4, (PointF) obtain2);
        a[1].set(obtain2.x, obtain2.y);
        FPoint obtain3 = FPoint.obtain();
        adVar.a(adVar.getMapWidth() + i, adVar.getMapHeight() + i, (PointF) obtain3);
        a[2].set(obtain3.x, obtain3.y);
        FPoint obtain4 = FPoint.obtain();
        adVar.a(i3, adVar.getMapHeight() + i, (PointF) obtain4);
        a[3].set(obtain4.x, obtain4.y);
        obtain.recycle();
        obtain2.recycle();
        obtain3.recycle();
        obtain4.recycle();
        return a;
    }

    public static float b(MapConfig mapConfig, int i, int i2, int i3, int i4, int i5, int i6) {
        float sz = mapConfig.getSZ();
        if (i == i3 || i2 == i4) {
            return sz;
        }
        return Math.max((float) a(mapConfig.getMapZoomScale(), i5, Math.abs(i3 - i)), (float) a(mapConfig.getMapZoomScale(), i6, Math.abs(i4 - i2)));
    }

    public static int b(int i, Bitmap bitmap, boolean z) {
        if (bitmap == null || bitmap.isRecycled()) {
            return 0;
        }
        if (i == 0) {
            int[] iArr = {0};
            GLES20.glGenTextures(1, iArr, 0);
            i = iArr[0];
        }
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        if (z) {
            GLES20.glTexParameterf(3553, 10242, 10497.0f);
            GLES20.glTexParameterf(3553, 10243, 10497.0f);
        } else {
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
        }
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        return i;
    }

    public static String b(Context context) {
        return FileUtil.getMapBaseStorage(context) + File.separator + AeUtil.ROOT_DATA_PATH_OLD_NAME + File.separator;
    }

    public static List<FPoint> b(FPoint[] fPointArr, List<FPoint> list, boolean z) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(list);
        for (byte b2 = 0; b2 < 4; b2 = (byte) (b2 + 1)) {
            arrayList.clear();
            int size = arrayList2.size();
            int i = 0;
            while (true) {
                if (i >= (z ? size : size - 1)) {
                    break;
                }
                FPoint3 fPoint3 = (FPoint3) arrayList2.get(i % size);
                int i2 = i + 1;
                FPoint3 fPoint32 = (FPoint3) arrayList2.get(i2 % size);
                if (i == 0 && a(fPoint3, fPointArr[b2], fPointArr[(b2 + 1) % fPointArr.length])) {
                    arrayList.add(fPoint3);
                }
                int i3 = b2 + 1;
                if (a(fPoint3, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                    if (a(fPoint32, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                        arrayList.add(fPoint32);
                    } else {
                        arrayList.add(a(fPointArr[b2], fPointArr[i3 % fPointArr.length], fPoint3, fPoint32, fPoint32.colorIndex));
                    }
                } else if (a(fPoint32, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                    arrayList.add(a(fPointArr[b2], fPointArr[i3 % fPointArr.length], fPoint3, fPoint32, fPoint3.colorIndex));
                    arrayList.add(fPoint32);
                }
                i = i2;
            }
            arrayList2.clear();
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                arrayList2.add(arrayList.get(i4));
            }
        }
        return arrayList2;
    }

    public static void b(int i) {
        GLES20.glDeleteTextures(1, new int[]{i}, 0);
    }

    public static void b(Rect rect, int i, int i2) {
        if (rect != null) {
            if (i < rect.left) {
                rect.left = i;
            }
            if (i > rect.right) {
                rect.right = i;
            }
            if (i2 > rect.top) {
                rect.top = i2;
            }
            if (i2 < rect.bottom) {
                rect.bottom = i2;
            }
        }
    }

    private static void b(View view) {
        int i = 0;
        if (!(view instanceof ViewGroup)) {
            if (view instanceof TextView) {
                ((TextView) view).setHorizontallyScrolling(false);
            }
        } else {
            while (true) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (i >= viewGroup.getChildCount()) {
                    return;
                }
                b(viewGroup.getChildAt(i));
                i++;
            }
        }
    }

    public static boolean b() {
        return Build.VERSION.SDK_INT >= 9;
    }

    public static boolean b(double d, double d2, double d3, double d4, double d5, double d6) {
        return Math.abs(a(d, d2, d3, d4, d5, d6)) < 1.0E-9d && (d - d3) * (d - d5) <= 0.0d && (d2 - d4) * (d2 - d6) <= 0.0d;
    }

    public static boolean b(int i, int i2) {
        if (i > 0 && i2 > 0) {
            return true;
        }
        Log.w("3dmap", "the map must have a size");
        return false;
    }

    public static boolean b(List<LatLng> list, CircleHoleOptions circleHoleOptions) {
        int i;
        try {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < list.size(); i2++) {
                arrayList.add(list.get(i2));
            }
            arrayList.add(list.get(0));
            ArrayList arrayList2 = new ArrayList();
            int i3 = 0;
            while (i3 < arrayList.size() && (i = i3 + 1) < arrayList.size()) {
                if (circleHoleOptions.getRadius() < AMapUtils.calculateLineDistance(circleHoleOptions.getCenter(), (LatLng) arrayList.get(i3)) && circleHoleOptions.getRadius() < AMapUtils.calculateLineDistance(circleHoleOptions.getCenter(), (LatLng) arrayList.get(i))) {
                    arrayList2.clear();
                    arrayList2.add(arrayList.get(i3));
                    arrayList2.add(arrayList.get(i));
                    if (circleHoleOptions.getRadius() >= ((double) AMapUtils.calculateLineDistance(circleHoleOptions.getCenter(), (LatLng) SpatialRelationUtil.calShortestDistancePoint(arrayList2, circleHoleOptions.getCenter()).second))) {
                        return true;
                    }
                    i3 = i;
                }
                return true;
            }
        } catch (Throwable th) {
            ic.c(th, "Util", "isPolygon2CircleIntersect");
            th.printStackTrace();
        }
        return false;
    }

    private static boolean b(List<LatLng> list, List<LatLng> list2) {
        int i;
        int i2;
        int i3 = 0;
        while (i3 < list.size() && (i = i3 + 1) < list.size()) {
            try {
                int i4 = 0;
                while (i4 < list2.size() && (i2 = i4 + 1) < list2.size()) {
                    boolean a2 = fm.a(list.get(i3), list.get(i), list2.get(i4), list2.get(i2));
                    if (a2) {
                        return a2;
                    }
                    i4 = i2;
                }
                i3 = i;
            } catch (Throwable th) {
                ic.c(th, "Util", "isSegmentsIntersect");
                th.printStackTrace();
            }
        }
        return false;
    }

    private static byte[] b(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                return byteArray;
            } catch (Throwable th2) {
                th = th2;
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th3) {
                        th3.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Throwable unused) {
            byteArrayOutputStream = null;
        }
    }

    public static byte[] b(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[2048];
        while (true) {
            int read = inputStream.read(bArr, 0, 2048);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public static String c(Context context) {
        String a2 = a(context);
        if (a2 == null) {
            return null;
        }
        File file = new File(a2, "VMAP2");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.toString() + File.separator;
    }

    public static boolean c() {
        return Build.VERSION.SDK_INT >= 11;
    }

    public static boolean d() {
        return Build.VERSION.SDK_INT >= 12;
    }

    public static boolean d(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        NetworkInfo.State state;
        return (context == null || (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || (state = activeNetworkInfo.getState()) == null || state == NetworkInfo.State.DISCONNECTED || state == NetworkInfo.State.DISCONNECTING) ? false : true;
    }

    public static ho e() {
        try {
            return w.e == null ? new ho.a("3dmap", "6.9.3", w.c).a(new String[]{"com.amap.api.maps", "com.amap.api.mapcore", "com.autonavi.amap.mapcore", "com.amap.api.3dmap.admic", "com.amap.api.trace", "com.amap.api.trace.core"}).a("6.9.3").a() : w.e;
        } catch (Throwable unused) {
            return null;
        }
    }

    public static boolean e(Context context) {
        File file = new File(b(context));
        if (file.exists()) {
            return FileUtil.deleteFile(file);
        }
        return true;
    }
}
