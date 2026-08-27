package com.mob.tools.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.tools.MobLog;
import com.mob.tools.network.KVPair;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;
import kotlin.text.Typography;
import me.panpf.sketch.display.ImageDisplayer;
import me.panpf.sketch.uri.FileUriModel;
import me.panpf.sketch.uri.HttpUriModel;

/* loaded from: classes2.dex */
public class ResHelper {
    private static float density;
    private static int deviceWidth;
    private static Uri mediaUri;
    private static Object rp;

    public static void clearCache(Context context) throws Throwable {
        deleteFileAndFolder(new File(getCachePath(context, null)));
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x008a A[Catch: Throwable -> 0x00b0, TryCatch #0 {Throwable -> 0x00b0, blocks: (B:10:0x0018, B:12:0x001e, B:14:0x004c, B:16:0x008a, B:18:0x0099, B:20:0x009f, B:21:0x00ab), top: B:9:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0099 A[Catch: Throwable -> 0x00b0, TryCatch #0 {Throwable -> 0x00b0, blocks: (B:10:0x0018, B:12:0x001e, B:14:0x004c, B:16:0x008a, B:18:0x0099, B:20:0x009f, B:21:0x00ab), top: B:9:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String contentUriToPath(android.content.Context r14, android.net.Uri r15) {
        /*
            r0 = 0
            if (r15 != 0) goto L4
            return r0
        L4:
            java.io.File r1 = new java.io.File
            java.lang.String r2 = r15.getPath()
            r1.<init>(r2)
            boolean r1 = r1.exists()
            if (r1 == 0) goto L18
            java.lang.String r14 = r15.getPath()
            return r14
        L18:
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Throwable -> Lb0
            r2 = 19
            if (r1 < r2) goto L87
            java.lang.String r1 = "android.provider.DocumentsContract"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r2 = "isDocumentUri"
            r3 = 2
            java.lang.Class[] r4 = new java.lang.Class[r3]     // Catch: java.lang.Throwable -> Lb0
            java.lang.Class<android.content.Context> r5 = android.content.Context.class
            r6 = 0
            r4[r6] = r5     // Catch: java.lang.Throwable -> Lb0
            java.lang.Class<android.net.Uri> r5 = android.net.Uri.class
            r7 = 1
            r4[r7] = r5     // Catch: java.lang.Throwable -> Lb0
            java.lang.reflect.Method r2 = r1.getMethod(r2, r4)     // Catch: java.lang.Throwable -> Lb0
            r2.setAccessible(r7)     // Catch: java.lang.Throwable -> Lb0
            java.lang.Boolean r4 = java.lang.Boolean.TRUE     // Catch: java.lang.Throwable -> Lb0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> Lb0
            r3[r6] = r14     // Catch: java.lang.Throwable -> Lb0
            r3[r7] = r15     // Catch: java.lang.Throwable -> Lb0
            java.lang.Object r2 = r2.invoke(r0, r3)     // Catch: java.lang.Throwable -> Lb0
            boolean r2 = r4.equals(r2)     // Catch: java.lang.Throwable -> Lb0
            if (r2 == 0) goto L87
            java.lang.String r2 = "getDocumentId"
            java.lang.Class[] r3 = new java.lang.Class[r7]     // Catch: java.lang.Throwable -> Lb0
            java.lang.Class<android.net.Uri> r4 = android.net.Uri.class
            r3[r6] = r4     // Catch: java.lang.Throwable -> Lb0
            java.lang.reflect.Method r1 = r1.getMethod(r2, r3)     // Catch: java.lang.Throwable -> Lb0
            r1.setAccessible(r7)     // Catch: java.lang.Throwable -> Lb0
            java.lang.Object[] r2 = new java.lang.Object[r7]     // Catch: java.lang.Throwable -> Lb0
            r2[r6] = r15     // Catch: java.lang.Throwable -> Lb0
            java.lang.Object r1 = r1.invoke(r0, r2)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r2 = ":"
            java.lang.String[] r1 = r1.split(r2)     // Catch: java.lang.Throwable -> Lb0
            r1 = r1[r7]     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r2 = "_data"
            java.lang.String[] r10 = new java.lang.String[]{r2}     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r11 = "_id=?"
            java.lang.String[] r12 = new java.lang.String[r7]     // Catch: java.lang.Throwable -> Lb0
            r12[r6] = r1     // Catch: java.lang.Throwable -> Lb0
            android.content.ContentResolver r8 = r14.getContentResolver()     // Catch: java.lang.Throwable -> Lb0
            android.net.Uri r9 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI     // Catch: java.lang.Throwable -> Lb0
            r13 = 0
            android.database.Cursor r1 = r8.query(r9, r10, r11, r12, r13)     // Catch: java.lang.Throwable -> Lb0
            goto L88
        L87:
            r1 = r0
        L88:
            if (r1 != 0) goto L97
            android.content.ContentResolver r2 = r14.getContentResolver()     // Catch: java.lang.Throwable -> Lb0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r3 = r15
            android.database.Cursor r1 = r2.query(r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> Lb0
        L97:
            if (r1 == 0) goto Lb8
            boolean r14 = r1.moveToFirst()     // Catch: java.lang.Throwable -> Lb0
            if (r14 == 0) goto Laa
            java.lang.String r14 = "_data"
            int r14 = r1.getColumnIndex(r14)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r14 = r1.getString(r14)     // Catch: java.lang.Throwable -> Lb0
            goto Lab
        Laa:
            r14 = r0
        Lab:
            r1.close()     // Catch: java.lang.Throwable -> Lb0
            r0 = r14
            goto Lb8
        Lb0:
            r14 = move-exception
            com.mob.tools.log.NLog r15 = com.mob.tools.MobLog.getInstance()
            r15.w(r14)
        Lb8:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.ResHelper.contentUriToPath(android.content.Context, android.net.Uri):java.lang.String");
    }

    public static void copyFile(FileInputStream fileInputStream, FileOutputStream fileOutputStream) throws Throwable {
        byte[] bArr = new byte[65536];
        int read = fileInputStream.read(bArr);
        while (read > 0) {
            fileOutputStream.write(bArr, 0, read);
            read = fileInputStream.read(bArr);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }

    public static boolean copyFile(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || !new File(str).exists()) {
            return false;
        }
        try {
            copyFile(new FileInputStream(str), new FileOutputStream(str2));
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static int[] covertTimeInYears(long j) {
        long currentTimeMillis = System.currentTimeMillis() - j;
        if (currentTimeMillis <= 0) {
            return new int[]{0, 0};
        }
        long j2 = currentTimeMillis / 1000;
        if (j2 < 60) {
            return new int[]{(int) j2, 0};
        }
        long j3 = j2 / 60;
        if (j3 < 60) {
            return new int[]{(int) j3, 1};
        }
        long j4 = j3 / 60;
        if (j4 < 24) {
            return new int[]{(int) j4, 2};
        }
        long j5 = j4 / 24;
        if (j5 < 30) {
            return new int[]{(int) j5, 3};
        }
        long j6 = j5 / 30;
        return j6 < 12 ? new int[]{(int) j6, 4} : new int[]{(int) (j6 / 12), 5};
    }

    public static long dateStrToLong(String str) {
        return new SimpleDateFormat("yyyy-MM-dd").parse(str, new ParsePosition(0)).getTime();
    }

    public static long dateToLong(String str) {
        try {
            Date date = new Date(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return 0L;
        }
    }

    public static Bundle decodeUrl(String str) {
        Bundle bundle = new Bundle();
        if (str != null) {
            for (String str2 : str.split("&")) {
                String[] split = str2.split("=");
                if (split.length < 2 || split[1] == null) {
                    bundle.putString(URLDecoder.decode(split[0]), "");
                } else {
                    bundle.putString(URLDecoder.decode(split[0]), URLDecoder.decode(split[1]));
                }
            }
        }
        return bundle;
    }

    public static void deleteFileAndFolder(File file) throws Throwable {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        String[] list = file.list();
        if (list == null || list.length <= 0) {
            file.delete();
            return;
        }
        for (String str : list) {
            File file2 = new File(file, str);
            if (file2.isDirectory()) {
                deleteFileAndFolder(file2);
            } else {
                file2.delete();
            }
        }
        file.delete();
    }

    public static void deleteFilesInFolder(File file) throws Throwable {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        String[] list = file.list();
        if (list == null || list.length <= 0) {
            return;
        }
        for (String str : list) {
            File file2 = new File(file, str);
            if (file2.isDirectory()) {
                deleteFilesInFolder(file2);
            } else {
                file2.delete();
            }
        }
    }

    public static int designToDevice(Context context, float f, int i) {
        if (density <= 0.0f) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return (int) (((i * density) / f) + 0.5f);
    }

    public static int designToDevice(Context context, int i, int i2) {
        if (deviceWidth == 0) {
            int[] screenSize = getScreenSize(context);
            deviceWidth = screenSize[0] < screenSize[1] ? screenSize[0] : screenSize[1];
        }
        return (int) (((i2 * deviceWidth) / i) + 0.5f);
    }

    public static int dipToPx(Context context, int i) {
        if (density <= 0.0f) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return (int) ((i * density) + 0.5f);
    }

    public static String encodeUrl(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            if (obj == null) {
                obj = "";
            }
            if (z) {
                z = false;
            } else {
                sb.append("&");
            }
            sb.append(Data.urlEncode(str) + "=" + Data.urlEncode(String.valueOf(obj)));
        }
        return sb.toString();
    }

    public static String encodeUrl(ArrayList<KVPair<String>> arrayList) {
        if (arrayList == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        Iterator<KVPair<String>> it = arrayList.iterator();
        while (it.hasNext()) {
            KVPair<String> next = it.next();
            if (i > 0) {
                sb.append(Typography.amp);
            }
            String str = next.name;
            String str2 = next.value;
            if (str != null) {
                if (str2 == null) {
                    str2 = "";
                }
                sb.append(Data.urlEncode(str) + "=" + Data.urlEncode(str2));
                i++;
            }
        }
        return sb.toString();
    }

    public static <T> T forceCast(Object obj) {
        return (T) forceCast(obj, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T forceCast(Object obj, T t) {
        if (obj == 0) {
            return t;
        }
        if (obj instanceof Byte) {
            byte byteValue = ((Byte) obj).byteValue();
            if (t instanceof Boolean) {
                return (T) Boolean.valueOf(byteValue != 0);
            }
            if (t instanceof Short) {
                return (T) Short.valueOf(byteValue);
            }
            if (t instanceof Character) {
                return (T) Character.valueOf((char) byteValue);
            }
            if (t instanceof Integer) {
                return (T) Integer.valueOf(byteValue);
            }
            if (t instanceof Float) {
                return (T) Float.valueOf(byteValue);
            }
            if (t instanceof Long) {
                return (T) Long.valueOf(byteValue);
            }
            if (t instanceof Double) {
                return (T) Double.valueOf(byteValue);
            }
        } else if (obj instanceof Character) {
            char charValue = ((Character) obj).charValue();
            if (t instanceof Byte) {
                return (T) Byte.valueOf((byte) charValue);
            }
            if (t instanceof Boolean) {
                return (T) Boolean.valueOf(charValue != 0);
            }
            if (t instanceof Short) {
                return (T) Short.valueOf((short) charValue);
            }
            if (t instanceof Integer) {
                return (T) Integer.valueOf(charValue);
            }
            if (t instanceof Float) {
                return (T) Float.valueOf(charValue);
            }
            if (t instanceof Long) {
                return (T) Long.valueOf(charValue);
            }
            if (t instanceof Double) {
                return (T) Double.valueOf(charValue);
            }
        } else if (obj instanceof Short) {
            short shortValue = ((Short) obj).shortValue();
            if (t instanceof Byte) {
                return (T) Byte.valueOf((byte) shortValue);
            }
            if (t instanceof Boolean) {
                return (T) Boolean.valueOf(shortValue != 0);
            }
            if (t instanceof Character) {
                return (T) Character.valueOf((char) shortValue);
            }
            if (t instanceof Integer) {
                return (T) Integer.valueOf(shortValue);
            }
            if (t instanceof Float) {
                return (T) Float.valueOf(shortValue);
            }
            if (t instanceof Long) {
                return (T) Long.valueOf(shortValue);
            }
            if (t instanceof Double) {
                return (T) Double.valueOf(shortValue);
            }
        } else if (obj instanceof Integer) {
            int intValue = ((Integer) obj).intValue();
            if (t instanceof Byte) {
                return (T) Byte.valueOf((byte) intValue);
            }
            if (t instanceof Boolean) {
                return (T) Boolean.valueOf(intValue != 0);
            }
            if (t instanceof Character) {
                return (T) Character.valueOf((char) intValue);
            }
            if (t instanceof Short) {
                return (T) Short.valueOf((short) intValue);
            }
            if (t instanceof Float) {
                return (T) Float.valueOf(intValue);
            }
            if (t instanceof Long) {
                return (T) Long.valueOf(intValue);
            }
            if (t instanceof Double) {
                return (T) Double.valueOf(intValue);
            }
        } else if (obj instanceof Float) {
            float floatValue = ((Float) obj).floatValue();
            if (t instanceof Byte) {
                return (T) Byte.valueOf((byte) floatValue);
            }
            if (t instanceof Boolean) {
                return (T) Boolean.valueOf(floatValue != 0.0f);
            }
            if (t instanceof Character) {
                return (T) Character.valueOf((char) floatValue);
            }
            if (t instanceof Short) {
                return (T) Short.valueOf((short) floatValue);
            }
            if (t instanceof Integer) {
                return (T) Integer.valueOf((int) floatValue);
            }
            if (t instanceof Long) {
                return (T) Long.valueOf(floatValue);
            }
            if (t instanceof Double) {
                return (T) Double.valueOf(floatValue);
            }
        } else if (obj instanceof Long) {
            long longValue = ((Long) obj).longValue();
            if (t instanceof Byte) {
                return (T) Byte.valueOf((byte) longValue);
            }
            if (t instanceof Boolean) {
                return (T) Boolean.valueOf(longValue != 0);
            }
            if (t instanceof Character) {
                return (T) Character.valueOf((char) longValue);
            }
            if (t instanceof Short) {
                return (T) Short.valueOf((short) longValue);
            }
            if (t instanceof Integer) {
                return (T) Integer.valueOf((int) longValue);
            }
            if (t instanceof Float) {
                return (T) Float.valueOf((float) longValue);
            }
            if (t instanceof Double) {
                return (T) Double.valueOf(longValue);
            }
        } else if (obj instanceof Double) {
            double doubleValue = ((Double) obj).doubleValue();
            if (t instanceof Byte) {
                return (T) Byte.valueOf((byte) doubleValue);
            }
            if (t instanceof Boolean) {
                return (T) Boolean.valueOf(doubleValue != 0.0d);
            }
            if (t instanceof Character) {
                return (T) Character.valueOf((char) doubleValue);
            }
            if (t instanceof Short) {
                return (T) Short.valueOf((short) doubleValue);
            }
            if (t instanceof Integer) {
                return (T) Integer.valueOf((int) doubleValue);
            }
            if (t instanceof Float) {
                return (T) Float.valueOf((float) doubleValue);
            }
            if (t instanceof Long) {
                return (T) Long.valueOf((long) doubleValue);
            }
        }
        return obj;
    }

    public static int getAnimRes(Context context, String str) {
        return getResId(context, "anim", str);
    }

    public static int getBitmapRes(Context context, String str) {
        int resId = getResId(context, "drawable", str);
        return resId <= 0 ? getResId(context, "mipmap", str) : resId;
    }

    public static String getCachePath(Context context, String str) {
        String sdcardPath;
        String str2 = context.getFilesDir().getAbsolutePath() + "/Mob/cache/";
        DeviceHelper deviceHelper = DeviceHelper.getInstance(context);
        try {
            if (deviceHelper.getSdcardState() && (sdcardPath = deviceHelper.getSdcardPath()) != null) {
                str2 = sdcardPath + "/Mob/" + deviceHelper.getPackageName() + "/cache/";
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        if (!TextUtils.isEmpty(str)) {
            str2 = str2 + str + FileUriModel.SCHEME;
        }
        File file = new File(str2);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        return str2;
    }

    public static String getCacheRoot(Context context) {
        String sdcardPath;
        try {
            String str = context.getFilesDir().getAbsolutePath() + "/Mob/";
            DeviceHelper deviceHelper = DeviceHelper.getInstance(context);
            if (deviceHelper.getSdcardState() && (sdcardPath = deviceHelper.getSdcardPath()) != null) {
                str = sdcardPath + "/Mob/";
            }
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            return str;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    public static File getCacheRootFile(Context context, String str) {
        try {
            String cacheRoot = getCacheRoot(context);
            if (cacheRoot == null) {
                return null;
            }
            File file = new File(cacheRoot, str);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            return file;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    public static int getColorRes(Context context, String str) {
        return getResId(context, "color", str);
    }

    public static String getDataCache(Context context) {
        String str = context.getFilesDir().getAbsolutePath() + "/Mob/";
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return str;
    }

    public static File getDataCacheFile(Context context, String str) {
        return new File(getDataCache(context), str);
    }

    public static float getDensity(Context context) {
        if (density <= 0.0f) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return density;
    }

    public static int getDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static float[] getDensityXYDpi(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return new float[]{displayMetrics.xdpi, displayMetrics.ydpi};
    }

    public static long getFileSize(File file) throws Throwable {
        if (!file.exists()) {
            return 0L;
        }
        if (!file.isDirectory()) {
            return file.length();
        }
        int i = 0;
        for (String str : file.list()) {
            i = (int) (i + getFileSize(new File(file, str)));
        }
        return i;
    }

    public static long getFileSize(String str) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            return 0L;
        }
        return getFileSize(new File(str));
    }

    public static int getIdRes(Context context, String str) {
        return getResId(context, ConnectionModel.ID, str);
    }

    public static String getImageCachePath(Context context) {
        return getCachePath(context, "images");
    }

    public static int getLayoutRes(Context context, String str) {
        return getResId(context, "layout", str);
    }

    public static synchronized Uri getMediaUri(Context context, String str, String str2) {
        Uri uri;
        synchronized (ResHelper.class) {
            final Object obj = new Object();
            mediaUri = null;
            MediaScannerConnection.scanFile(context, new String[]{str}, new String[]{str2}, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.mob.tools.utils.ResHelper.1
                @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                public void onScanCompleted(String str3, Uri uri2) {
                    Uri unused = ResHelper.mediaUri = uri2;
                    synchronized (obj) {
                        obj.notifyAll();
                    }
                }
            });
            try {
                if (mediaUri == null) {
                    synchronized (obj) {
                        obj.wait(10000L);
                    }
                }
            } catch (Throwable unused) {
            }
            uri = mediaUri;
            mediaUri = null;
        }
        return uri;
    }

    public static int getPluralsRes(Context context, String str) {
        return getResId(context, "plurals", str);
    }

    public static int getRawRes(Context context, String str) {
        return getResId(context, "raw", str);
    }

    public static int getResId(Context context, String str, String str2) {
        int i = 0;
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return 0;
        }
        if (rp != null) {
            try {
                Method method = rp.getClass().getMethod("getResId", Context.class, String.class, String.class);
                method.setAccessible(true);
                i = ((Integer) method.invoke(rp, context, str, str2)).intValue();
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        }
        if (i <= 0) {
            String packageName = context.getPackageName();
            if (TextUtils.isEmpty(packageName)) {
                return i;
            }
            if (i <= 0 && (i = context.getResources().getIdentifier(str2, str, packageName)) <= 0) {
                i = context.getResources().getIdentifier(str2.toLowerCase(), str, packageName);
            }
            if (i <= 0) {
                MobLog.getInstance().w("failed to parse " + str + " resource \"" + str2 + "\"");
            }
        }
        return i;
    }

    public static float getScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int getScreenHeight(Context context) {
        return getScreenSize(context)[1];
    }

    public static int[] getScreenSize(Context context) {
        WindowManager windowManager;
        Display display = null;
        try {
            windowManager = (WindowManager) DeviceHelper.getInstance(context).getSystemServiceSafe("window");
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            windowManager = null;
        }
        if (windowManager == null) {
            return new int[]{0, 0};
        }
        try {
            display = windowManager.getDefaultDisplay();
        } catch (Throwable th2) {
            MobLog.getInstance().w(th2);
        }
        if (display == null) {
            try {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
            } catch (Throwable th3) {
                MobLog.getInstance().w(th3);
                return new int[]{0, 0};
            }
        }
        if (Build.VERSION.SDK_INT < 13) {
            try {
                DisplayMetrics displayMetrics2 = new DisplayMetrics();
                display.getMetrics(displayMetrics2);
                return new int[]{displayMetrics2.widthPixels, displayMetrics2.heightPixels};
            } catch (Throwable th4) {
                MobLog.getInstance().w(th4);
                return new int[]{0, 0};
            }
        }
        try {
            Point point = new Point();
            Method method = display.getClass().getMethod("getRealSize", Point.class);
            method.setAccessible(true);
            method.invoke(display, point);
            return new int[]{point.x, point.y};
        } catch (Throwable th5) {
            MobLog.getInstance().w(th5);
            return new int[]{0, 0};
        }
    }

    public static int getScreenWidth(Context context) {
        return getScreenSize(context)[0];
    }

    public static int getStringArrayRes(Context context, String str) {
        return getResId(context, "array", str);
    }

    public static int getStringRes(Context context, String str) {
        return getResId(context, "string", str);
    }

    public static <T> T getStringValue(Context context, String str, T t) {
        try {
            String trim = context.getString(getStringRes(context, str)).trim();
            if (t == null) {
                return (T) String.valueOf(trim);
            }
            Class<?> cls = t.getClass();
            if (cls.equals(String.class)) {
                return (T) String.valueOf(trim);
            }
            if (!cls.equals(Integer.TYPE) && !cls.equals(Integer.class)) {
                if (!cls.equals(Long.TYPE) && !cls.equals(Long.class)) {
                    if (!cls.equals(Boolean.TYPE) && !cls.equals(Boolean.class)) {
                        if (!cls.equals(Float.TYPE) && !cls.equals(Float.class)) {
                            if (!cls.equals(Double.TYPE) && !cls.equals(Double.class)) {
                                if (!cls.equals(Character.TYPE) && !cls.equals(Character.class)) {
                                    if (!cls.equals(Byte.TYPE) && !cls.equals(Byte.class)) {
                                        if (!cls.equals(Short.TYPE) && !cls.equals(Short.class)) {
                                            return (T) new Hashon().fromJson(trim, cls);
                                        }
                                        return (T) Short.valueOf(String.valueOf(trim));
                                    }
                                    return (T) Byte.valueOf(String.valueOf(trim));
                                }
                                return (T) Character.valueOf(String.valueOf(trim).charAt(0));
                            }
                            return (T) Double.valueOf(String.valueOf(trim));
                        }
                        return (T) Float.valueOf(String.valueOf(trim));
                    }
                    return (T) Boolean.valueOf(trim);
                }
                return (T) Long.valueOf(String.valueOf(trim));
            }
            return (T) Integer.valueOf(String.valueOf(trim));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return t;
        }
    }

    public static int getStyleRes(Context context, String str) {
        return getResId(context, "style", str);
    }

    public static int[] getStyleableRes(Context context, String str) {
        try {
            Object staticField = ReflectHelper.getStaticField(ReflectHelper.importClass(context.getPackageName() + ".R$styleable"), str);
            return staticField == null ? new int[0] : staticField.getClass().isArray() ? (int[]) staticField : new int[]{((Integer) staticField).intValue()};
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return new int[0];
        }
    }

    public static int getTextLengthInWord(String str) {
        int i = 0;
        for (char c : str == null ? new char[0] : str.toCharArray()) {
            i += c < 256 ? 1 : 2;
        }
        return i;
    }

    public static <T> boolean isEqual(T t, T t2) {
        return !((t == null && t2 != null) || !(t == null || t.equals(t2)));
    }

    public static boolean isLeapYear(int i) {
        return i % ImageDisplayer.DEFAULT_ANIMATION_DURATION == 0 || (i % 4 == 0 && i % 100 != 0);
    }

    public static Date longToDate(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return calendar.getTime();
    }

    public static String longToTime(long j, int i) {
        String str = "yyyy-MM-dd kk:mm:ss";
        if (i == 5) {
            str = "yyyy-MM-dd";
        } else if (i == 10) {
            str = "yyyy-MM-dd kk";
        } else if (i != 12) {
            switch (i) {
                case 1:
                    str = "yyyy";
                    break;
                case 2:
                    str = "yyyy-MM";
                    break;
            }
        } else {
            str = "yyyy-MM-dd kk:mm";
        }
        return new SimpleDateFormat(str).format(Long.valueOf(j));
    }

    public static int parseInt(String str) throws Throwable {
        return parseInt(str, 10);
    }

    public static int parseInt(String str, int i) throws Throwable {
        return Integer.parseInt(str, i);
    }

    public static long parseLong(String str) throws Throwable {
        return parseLong(str, 10);
    }

    public static long parseLong(String str, int i) throws Throwable {
        return Long.parseLong(str, i);
    }

    public static Uri pathToContentUri(Context context, String str) {
        try {
            if (!DeviceHelper.getInstance(context).checkPermission("android.permission.READ_EXTERNAL_STORAGE")) {
                return null;
            }
            Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{FileDownloadModel.ID}, "_data=? ", new String[]{str}, null);
            if (query == null || !query.moveToFirst()) {
                if (!new File(str).exists()) {
                    return null;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("_data", str);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            }
            int i = query.getInt(query.getColumnIndex(FileDownloadModel.ID));
            return Uri.withAppendedPath(Uri.parse("content://media/external/images/media"), "" + i);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    public static int pxToDip(Context context, int i) {
        if (density <= 0.0f) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return (int) ((i / density) + 0.5f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0010, code lost:
    
        if (r0.exists() == false) goto L9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.Object readObjectFromFile(java.lang.String r2) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            r1 = 0
            if (r0 != 0) goto L3d
            java.io.File r0 = new java.io.File     // Catch: java.lang.Throwable -> L13
            r0.<init>(r2)     // Catch: java.lang.Throwable -> L13
            boolean r2 = r0.exists()     // Catch: java.lang.Throwable -> L13
            if (r2 != 0) goto L1c
            goto L1b
        L13:
            r2 = move-exception
            com.mob.tools.log.NLog r0 = com.mob.tools.MobLog.getInstance()
            r0.d(r2)
        L1b:
            r0 = r1
        L1c:
            if (r0 == 0) goto L3d
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L35
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L35
            java.util.zip.GZIPInputStream r0 = new java.util.zip.GZIPInputStream     // Catch: java.lang.Throwable -> L35
            r0.<init>(r2)     // Catch: java.lang.Throwable -> L35
            java.io.ObjectInputStream r2 = new java.io.ObjectInputStream     // Catch: java.lang.Throwable -> L35
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L35
            java.lang.Object r0 = r2.readObject()     // Catch: java.lang.Throwable -> L35
            r2.close()     // Catch: java.lang.Throwable -> L35
            return r0
        L35:
            r2 = move-exception
            com.mob.tools.log.NLog r0 = com.mob.tools.MobLog.getInstance()
            r0.d(r2)
        L3d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.ResHelper.readObjectFromFile(java.lang.String):java.lang.Object");
    }

    public static boolean saveObjectToFile(String str, Object obj) {
        File file;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            file = new File(str);
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            file = null;
        }
        if (file == null) {
            return false;
        }
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            objectOutputStream.close();
            return true;
        } catch (Throwable th2) {
            MobLog.getInstance().d(th2);
            return false;
        }
    }

    public static void setResourceProvider(Object obj) {
        try {
            if (obj.getClass().getMethod("getResId", Context.class, String.class, String.class) != null) {
                rp = obj;
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    public static long strToDate(String str) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str, new ParsePosition(0)).getTime();
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String toWordText(String str, int i) {
        char[] charArray = str.toCharArray();
        int i2 = i * 2;
        StringBuilder sb = new StringBuilder();
        int length = charArray.length;
        for (int i3 = 0; i3 < length; i3++) {
            char c = charArray[i3];
            i2 -= c < 256 ? 1 : 2;
            if (i2 < 0) {
                return sb.toString();
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static Bundle urlToBundle(String str) {
        String str2;
        int indexOf = str.indexOf("://");
        if (indexOf >= 0) {
            str2 = HttpUriModel.SCHEME + str.substring(indexOf + 1);
        } else {
            str2 = HttpUriModel.SCHEME + str;
        }
        try {
            URL url = new URL(str2);
            Bundle decodeUrl = decodeUrl(url.getQuery());
            decodeUrl.putAll(decodeUrl(url.getRef()));
            return decodeUrl;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return new Bundle();
        }
    }

    public static Uri videoPathToContentUri(Context context, String str) {
        try {
            if (!DeviceHelper.getInstance(context).checkPermission("android.permission.READ_EXTERNAL_STORAGE")) {
                return null;
            }
            Cursor query = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{FileDownloadModel.ID}, "_data=? ", new String[]{str}, null);
            if (query == null || !query.moveToFirst()) {
                if (!new File(str).exists()) {
                    return null;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("_data", str);
                return context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
            }
            int i = query.getInt(query.getColumnIndex(FileDownloadModel.ID));
            return Uri.withAppendedPath(Uri.parse("content://media/external/video/media"), "" + i);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }
}
