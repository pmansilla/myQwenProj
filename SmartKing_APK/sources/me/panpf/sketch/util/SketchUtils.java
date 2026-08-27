package me.panpf.sketch.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.opengl.EGL14;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.view.View;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import kotlin.text.Typography;
import me.panpf.sketch.Initializer;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.decode.ImageDecodeUtils;
import me.panpf.sketch.decode.ImageOrientationCorrector;
import me.panpf.sketch.decode.ImageType;
import me.panpf.sketch.drawable.SketchDrawable;
import me.panpf.sketch.drawable.SketchLoadingDrawable;
import me.panpf.sketch.request.DisplayRequest;
import me.panpf.sketch.uri.UriModel;
import me.panpf.sketch.zoom.Size;
import me.panpf.sketch.zoom.block.Block;

/* loaded from: classes2.dex */
public class SketchUtils {
    private static final float[] MATRIX_VALUES = new float[9];

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: me.panpf.sketch.util.SketchUtils$1, reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Bitmap.Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ALPHA_8.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.RGB_565.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_4444.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_8888.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public static String appendProcessName(Context context, String str) {
        String simpleProcessName = getSimpleProcessName(context);
        if (simpleProcessName != null) {
            try {
                return str + URLEncoder.encode(simpleProcessName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static Bitmap.CompressFormat bitmapConfigToCompressFormat(Bitmap.Config config) {
        return config == Bitmap.Config.RGB_565 ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG;
    }

    public static String blockListToString(List<Block> list) {
        if (list == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Block block : list) {
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append("\"");
            sb.append(block.drawRect.left);
            sb.append(",");
            sb.append(block.drawRect.top);
            sb.append(",");
            sb.append(block.drawRect.right);
            sb.append(",");
            sb.append(block.drawRect.bottom);
            sb.append("\"");
        }
        sb.append("]");
        return sb.toString();
    }

    /* JADX WARN: Can't wrap try/catch for region: R(10:17|(1:19)(1:61)|20|(2:56|(1:60))(2:22|(4:26|27|28|29))|31|(3:(1:34)|35|(3:37|38|39))|41|42|44|(2:49|50)(4:46|47|48|29)) */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x012d, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x012e, code lost:
    
        r0.printStackTrace();
        r8 = r8 + 1;
        r0 = new me.panpf.sketch.util.UnableCreateFileException(r0.getClass().getSimpleName() + ": " + r0.getMessage());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.io.File buildCacheDir(android.content.Context r17, java.lang.String r18, boolean r19, long r20, boolean r22, boolean r23, int r24) throws me.panpf.sketch.util.NoSpaceException, me.panpf.sketch.util.UnableCreateDirException, me.panpf.sketch.util.UnableCreateFileException {
        /*
            Method dump skipped, instructions count: 388
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: me.panpf.sketch.util.SketchUtils.buildCacheDir(android.content.Context, java.lang.String, boolean, long, boolean, boolean, int):java.io.File");
    }

    public static int ceil(int i, float f) {
        return (int) Math.ceil(i / f);
    }

    public static boolean checkSuffix(String str, String str2) {
        int lastIndexOf;
        if (str != null && (lastIndexOf = str.lastIndexOf(".")) > -1) {
            return str2.equalsIgnoreCase(str.substring(lastIndexOf));
        }
        return false;
    }

    public static boolean cleanDir(File file) {
        boolean z = true;
        if (file == null || !file.exists() || !file.isDirectory()) {
            return true;
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    z &= cleanDir(file2);
                }
                z &= file2.delete();
            }
        }
        return z;
    }

    public static void close(AssetFileDescriptor assetFileDescriptor) {
        if (assetFileDescriptor == null) {
            return;
        }
        try {
            assetFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        if (closeable instanceof OutputStream) {
            try {
                ((OutputStream) closeable).flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            closeable.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static int computeByteCount(int i, int i2, Bitmap.Config config) {
        return i * i2 * getBytesPerPixel(config);
    }

    public static String createFileUriDiskCacheKey(String str, String str2) {
        File file = new File(str2);
        if (!file.exists()) {
            return str;
        }
        return str + "." + file.lastModified();
    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            cleanDir(file);
        }
        return file.delete();
    }

    public static int dp2px(Context context, int i) {
        double d = i * context.getResources().getDisplayMetrics().density;
        Double.isNaN(d);
        return (int) (d + 0.5d);
    }

    public static Bitmap drawableToBitmap(Drawable drawable, boolean z, BitmapPool bitmapPool) {
        if (drawable == null || drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            return null;
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap.Config config = z ? Bitmap.Config.ARGB_4444 : Bitmap.Config.ARGB_8888;
        Bitmap orMake = bitmapPool != null ? bitmapPool.getOrMake(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), config) : Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), config);
        drawable.draw(new Canvas(orMake));
        return orMake;
    }

    public static DisplayRequest findDisplayRequest(SketchView sketchView) {
        Drawable drawable;
        if (sketchView == null || (drawable = sketchView.getDrawable()) == null || !(drawable instanceof SketchLoadingDrawable)) {
            return null;
        }
        return ((SketchLoadingDrawable) drawable).getRequest();
    }

    public static Initializer findInitializer(Context context) {
        String str;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo.metaData != null) {
                Iterator<String> it = applicationInfo.metaData.keySet().iterator();
                while (it.hasNext()) {
                    str = it.next();
                    if (Sketch.META_DATA_KEY_INITIALIZER.equals(applicationInfo.metaData.get(str))) {
                        break;
                    }
                }
            }
            str = null;
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                Class<?> cls = Class.forName(str);
                if (!Initializer.class.isAssignableFrom(cls)) {
                    SLog.e("findInitializer", str + " must be implements Initializer");
                    return null;
                }
                try {
                    return (Initializer) cls.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                } catch (InstantiationException e2) {
                    e2.printStackTrace();
                    return null;
                }
            } catch (ClassNotFoundException e3) {
                e3.printStackTrace();
                return null;
            }
        } catch (PackageManager.NameNotFoundException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public static float formatFloat(float f, int i) {
        return new BigDecimal(f).setScale(i, 4).floatValue();
    }

    public static boolean formatSupportBitmapRegionDecoder(@Nullable ImageType imageType) {
        return imageType != null && (imageType == ImageType.JPEG || imageType == ImageType.PNG || (imageType == ImageType.WEBP && Build.VERSION.SDK_INT >= 14));
    }

    public static String generatorTempFileName(DataSource dataSource, String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            ImageDecodeUtils.decodeBitmap(dataSource, options);
        } catch (Throwable th) {
            th.printStackTrace();
            options = null;
        }
        String md5 = SketchMD5Utils.md5(str);
        return (options == null || options.outMimeType == null || !options.outMimeType.startsWith("image/")) ? md5 : String.format("%s.%s", md5, options.outMimeType.replace("image/", ""));
    }

    @SuppressLint({"LongLogTag"})
    @TargetApi(9)
    public static String[] getAllAvailableSdcardPath(Context context) {
        if (Build.VERSION.SDK_INT <= 10) {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                return new String[]{Environment.getExternalStorageDirectory().getPath()};
            }
            return null;
        }
        try {
            Method method = StorageManager.class.getMethod("getVolumePaths", new Class[0]);
            StorageManager storageManager = (StorageManager) context.getSystemService("storage");
            try {
                String[] strArr = (String[]) method.invoke(storageManager, new Object[0]);
                if (strArr == null || strArr.length == 0) {
                    return null;
                }
                LinkedList linkedList = new LinkedList();
                Collections.addAll(linkedList, strArr);
                Iterator it = linkedList.iterator();
                Method method2 = null;
                while (it.hasNext()) {
                    String str = (String) it.next();
                    if (method2 == null) {
                        try {
                            method2 = StorageManager.class.getMethod("getVolumeState", String.class);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                    try {
                        String str2 = (String) method2.invoke(storageManager, str);
                        if (!"mounted".equals(str2) && !"mounted_ro".equals(str2)) {
                            it.remove();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        it.remove();
                    }
                }
                return (String[]) linkedList.toArray(new String[linkedList.size()]);
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
                return null;
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
                return null;
            }
        } catch (NoSuchMethodException unused) {
            SLog.e("getAllAvailableSdcardPath", "not found StorageManager.getVolumePaths() method");
            if ("mounted".equals(Environment.getExternalStorageState())) {
                return new String[]{Environment.getExternalStorageDirectory().getPath()};
            }
            return null;
        }
    }

    public static File getAppCacheDir(Context context) {
        File externalCacheDir = "mounted".equals(Environment.getExternalStorageState()) ? context.getExternalCacheDir() : null;
        return externalCacheDir == null ? context.getCacheDir() : externalCacheDir;
    }

    public static long getAvailableBytes(File file) {
        if (!file.exists() && !file.mkdirs()) {
            return 0L;
        }
        return Build.VERSION.SDK_INT >= 18 ? new StatFs(file.getPath()).getAvailableBytes() : r0.getAvailableBlocks() * r0.getBlockSize();
    }

    public static int getByteCount(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return 0;
        }
        return Build.VERSION.SDK_INT >= 19 ? bitmap.getAllocationByteCount() : Build.VERSION.SDK_INT >= 12 ? bitmap.getByteCount() : bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static int getBytesPerPixel(Bitmap.Config config) {
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        switch (AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()]) {
            case 1:
                return 1;
            case 2:
            case 3:
                return 2;
            default:
                return 4;
        }
    }

    public static File getDefaultSketchCacheDir(Context context, String str, boolean z) {
        File appCacheDir = getAppCacheDir(context);
        if (z) {
            str = appendProcessName(context, str);
        }
        return new File(appCacheDir, str);
    }

    public static Drawable getLastDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (!(drawable instanceof LayerDrawable)) {
            return drawable;
        }
        LayerDrawable layerDrawable = (LayerDrawable) drawable;
        int numberOfLayers = layerDrawable.getNumberOfLayers();
        if (numberOfLayers <= 0) {
            return null;
        }
        return getLastDrawable(layerDrawable.getDrawable(numberOfLayers - 1));
    }

    public static int getMatrixRotateDegrees(Matrix matrix) {
        synchronized (MATRIX_VALUES) {
            matrix.getValues(MATRIX_VALUES);
            int round = (int) Math.round(Math.atan2(MATRIX_VALUES[1], MATRIX_VALUES[0]) * 57.29577951308232d);
            if (round < 0) {
                return Math.abs(round);
            }
            if (round <= 0) {
                return 0;
            }
            return 360 - round;
        }
    }

    public static float getMatrixScale(Matrix matrix) {
        float sqrt;
        synchronized (MATRIX_VALUES) {
            matrix.getValues(MATRIX_VALUES);
            sqrt = (float) Math.sqrt(((float) Math.pow(MATRIX_VALUES[0], 2.0d)) + ((float) Math.pow(MATRIX_VALUES[3], 2.0d)));
        }
        return sqrt;
    }

    public static void getMatrixTranslation(Matrix matrix, PointF pointF) {
        synchronized (MATRIX_VALUES) {
            matrix.getValues(MATRIX_VALUES);
            pointF.x = MATRIX_VALUES[2];
            pointF.y = MATRIX_VALUES[5];
        }
    }

    public static float getMatrixValue(Matrix matrix, int i) {
        float f;
        synchronized (MATRIX_VALUES) {
            matrix.getValues(MATRIX_VALUES);
            f = MATRIX_VALUES[i];
        }
        return f;
    }

    public static int getOpenGLMaxTextureSize() {
        int i;
        try {
            i = Build.VERSION.SDK_INT >= 17 ? getOpenGLMaxTextureSizeJB1() : getOpenGLMaxTextureSizeBase();
        } catch (Exception e) {
            e.printStackTrace();
            i = 0;
        }
        if (i == 0) {
            return 4096;
        }
        return i;
    }

    private static int getOpenGLMaxTextureSizeBase() {
        if (Build.VERSION.SDK_INT == 16) {
            return 0;
        }
        EGL10 egl10 = (EGL10) EGLContext.getEGL();
        EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl10.eglInitialize(eglGetDisplay, new int[2]);
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        int[] iArr = new int[1];
        egl10.eglChooseConfig(eglGetDisplay, new int[]{12351, 12430, 12329, 0, 12339, 1, 12344}, eGLConfigArr, 1, iArr);
        int i = iArr[0];
        EGLConfig eGLConfig = eGLConfigArr[0];
        EGLSurface eglCreatePbufferSurface = egl10.eglCreatePbufferSurface(eglGetDisplay, eGLConfig, new int[]{12375, 64, 12374, 64, 12344});
        EGLContext eglCreateContext = egl10.eglCreateContext(eglGetDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 1, 12344});
        egl10.eglMakeCurrent(eglGetDisplay, eglCreatePbufferSurface, eglCreatePbufferSurface, eglCreateContext);
        int[] iArr2 = new int[1];
        GLES10.glGetIntegerv(3379, iArr2, 0);
        egl10.eglMakeCurrent(eglGetDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl10.eglDestroySurface(eglGetDisplay, eglCreatePbufferSurface);
        egl10.eglDestroyContext(eglGetDisplay, eglCreateContext);
        egl10.eglTerminate(eglGetDisplay);
        return iArr2[0];
    }

    @TargetApi(17)
    private static int getOpenGLMaxTextureSizeJB1() {
        android.opengl.EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
        int[] iArr = new int[2];
        EGL14.eglInitialize(eglGetDisplay, iArr, 0, iArr, 1);
        android.opengl.EGLConfig[] eGLConfigArr = new android.opengl.EGLConfig[1];
        int[] iArr2 = new int[1];
        EGL14.eglChooseConfig(eglGetDisplay, new int[]{12351, 12430, 12329, 0, 12352, 4, 12339, 1, 12344}, 0, eGLConfigArr, 0, 1, iArr2, 0);
        int i = iArr2[0];
        android.opengl.EGLConfig eGLConfig = eGLConfigArr[0];
        android.opengl.EGLSurface eglCreatePbufferSurface = EGL14.eglCreatePbufferSurface(eglGetDisplay, eGLConfig, new int[]{12375, 64, 12374, 64, 12344}, 0);
        android.opengl.EGLContext eglCreateContext = EGL14.eglCreateContext(eglGetDisplay, eGLConfig, EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
        EGL14.eglMakeCurrent(eglGetDisplay, eglCreatePbufferSurface, eglCreatePbufferSurface, eglCreateContext);
        int[] iArr3 = new int[1];
        GLES20.glGetIntegerv(3379, iArr3, 0);
        EGL14.eglMakeCurrent(eglGetDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroySurface(eglGetDisplay, eglCreatePbufferSurface);
        EGL14.eglDestroyContext(eglGetDisplay, eglCreateContext);
        EGL14.eglTerminate(eglGetDisplay);
        return iArr3[0];
    }

    public static String getOpenGLVersion(Context context) {
        return ((ActivityManager) context.getSystemService("activity")).getDeviceConfigurationInfo().getGlEsVersion();
    }

    public static int getPointerIndex(int i) {
        return Build.VERSION.SDK_INT >= 11 ? (i & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8 : (i & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
    }

    public static String getProcessName(Context context) {
        int myPid = Process.myPid();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.pid == myPid) {
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }

    public static String getSimpleProcessName(Context context) {
        String packageName;
        int lastIndexOf;
        String processName = getProcessName(context);
        if (processName == null || (lastIndexOf = processName.lastIndexOf((packageName = context.getPackageName()))) == -1) {
            return null;
        }
        return processName.substring(lastIndexOf + packageName.length());
    }

    public static long getTotalBytes(File file) {
        if (!file.exists() && !file.mkdirs()) {
            return 0L;
        }
        return Build.VERSION.SDK_INT >= 18 ? new StatFs(file.getPath()).getTotalBytes() : r0.getBlockCount() * r0.getBlockSize();
    }

    public static String getTrimLevelName(int i) {
        return i != 5 ? i != 10 ? i != 15 ? i != 20 ? i != 40 ? i != 60 ? i != 80 ? "UNKNOWN" : "COMPLETE" : "MODERATE" : "BACKGROUND" : "UI_HIDDEN" : "RUNNING_CRITICAL" : "RUNNING_LOW" : "RUNNING_MODERATE";
    }

    public static boolean invokeIn(StackTraceElement[] stackTraceElementArr, Class<?> cls, String str) {
        if (stackTraceElementArr == null || stackTraceElementArr.length == 0) {
            return false;
        }
        String name = cls.getName();
        for (StackTraceElement stackTraceElement : stackTraceElementArr) {
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            if (name.equals(className) && str.equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCross(Rect rect, Rect rect2) {
        return rect.left < rect2.right && rect2.left < rect.right && rect.top < rect2.bottom && rect2.top < rect.bottom;
    }

    public static boolean isDisabledARGB4444() {
        return Build.VERSION.SDK_INT >= 19;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v6, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8 */
    public static boolean isGifImage(Drawable drawable) {
        if (drawable == 0) {
            return false;
        }
        while (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            drawable = layerDrawable.getNumberOfLayers() > 0 ? layerDrawable.getDrawable(layerDrawable.getNumberOfLayers() - 1) : 0;
        }
        return (drawable instanceof SketchDrawable) && ImageType.GIF.getMimeType().equals(((SketchDrawable) drawable).getMimeType());
    }

    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equalsIgnoreCase(getProcessName(context));
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static String makeImageInfo(String str, int i, int i2, String str2, int i3, Bitmap bitmap, long j, String str3) {
        if (bitmap == null) {
            return "Unknown";
        }
        if (TextUtils.isEmpty(str)) {
            str = "Bitmap";
        }
        return String.format("%s(image=%dx%d,%s,%s, bitmap=%dx%d,%s,%d,%s%s)", str, Integer.valueOf(i), Integer.valueOf(i2), str2, ImageOrientationCorrector.toName(i3), Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), bitmap.getConfig() != null ? bitmap.getConfig().name() : null, Long.valueOf(j), Integer.toHexString(bitmap.hashCode()), str3 != null ? String.format(", key=%s", str3) : "");
    }

    @NonNull
    public static String makeRequestKey(@NonNull String str, @NonNull UriModel uriModel, @NonNull String str2) {
        if (uriModel.isConvertShortUriForKey()) {
            str = SketchMD5Utils.md5(str);
        }
        if (TextUtils.isEmpty(str2)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        if (str.lastIndexOf("?") == -1) {
            sb.append('?');
        } else {
            sb.append(Typography.amp);
        }
        sb.append("options");
        sb.append("=");
        sb.append(str2);
        return sb.toString();
    }

    public static void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.postOnAnimation(runnable);
        } else {
            view.postDelayed(runnable, 16L);
        }
    }

    public static Bitmap readApkIcon(Context context, String str, boolean z, String str2, BitmapPool bitmapPool) {
        Drawable drawable;
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(str, 1);
        if (packageArchiveInfo == null) {
            SLog.w(str2, "get packageInfo is null. %s", str);
            return null;
        }
        packageArchiveInfo.applicationInfo.sourceDir = str;
        packageArchiveInfo.applicationInfo.publicSourceDir = str;
        try {
            drawable = packageManager.getApplicationIcon(packageArchiveInfo.applicationInfo);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            drawable = null;
        }
        if (drawable != null) {
            return drawableToBitmap(drawable, z, bitmapPool);
        }
        SLog.w(str2, "app icon is null. %s", str);
        return null;
    }

    public static void reverseRotateRect(Rect rect, int i, Size size) {
        if (i % 90 != 0) {
            return;
        }
        if (i == 90) {
            int i2 = rect.bottom;
            rect.bottom = rect.left;
            rect.left = rect.top;
            rect.top = rect.right;
            rect.right = i2;
            rect.top = size.getHeight() - rect.top;
            rect.bottom = size.getHeight() - rect.bottom;
            return;
        }
        if (i != 180) {
            if (i == 270) {
                int i3 = rect.bottom;
                rect.bottom = rect.right;
                rect.right = rect.top;
                rect.top = rect.left;
                rect.left = i3;
                rect.left = size.getWidth() - rect.left;
                rect.right = size.getWidth() - rect.right;
                return;
            }
            return;
        }
        int i4 = rect.right;
        rect.right = rect.left;
        rect.left = i4;
        int i5 = rect.bottom;
        rect.bottom = rect.top;
        rect.top = i5;
        rect.top = size.getHeight() - rect.top;
        rect.bottom = size.getHeight() - rect.bottom;
        rect.left = size.getWidth() - rect.left;
        rect.right = size.getWidth() - rect.right;
    }

    public static void rotatePoint(PointF pointF, int i, Size size) {
        if (i % 90 != 0) {
            return;
        }
        if (i == 90) {
            float height = size.getHeight() - pointF.y;
            float f = pointF.x;
            pointF.x = height;
            pointF.y = f;
            return;
        }
        if (i == 180) {
            float width = size.getWidth() - pointF.x;
            float height2 = size.getHeight() - pointF.y;
            pointF.x = width;
            pointF.y = height2;
            return;
        }
        if (i == 270) {
            float f2 = pointF.y;
            float width2 = size.getWidth() - pointF.x;
            pointF.x = f2;
            pointF.y = width2;
        }
    }

    public static boolean sdkSupportBitmapRegionDecoder() {
        return Build.VERSION.SDK_INT >= 10;
    }

    public static boolean testCreateFile(File file) throws Exception {
        File file2 = file;
        while (file2 != null) {
            if (file2.exists()) {
                File file3 = new File(file2, "create_test.temp");
                if (file3.exists() && !file3.delete()) {
                    throw new Exception("Delete old test file failed: " + file3.getPath());
                }
                file3.createNewFile();
                if (!file3.exists()) {
                    return false;
                }
                if (file3.delete()) {
                    return true;
                }
                throw new Exception("Delete test file failed: " + file3.getPath());
            }
            file2 = file.getParentFile();
        }
        return false;
    }

    public static String toHexString(Object obj) {
        if (obj == null) {
            return null;
        }
        return Integer.toHexString(obj.hashCode());
    }

    public static String viewLayoutFormatted(int i) {
        return i >= 0 ? String.valueOf(i) : i == -1 ? "MATCH_PARENT" : i == -2 ? "WRAP_CONTENT" : "Unknown";
    }
}
