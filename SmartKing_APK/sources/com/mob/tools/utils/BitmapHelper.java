package com.mob.tools.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.opengl.GLES10;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.luck.picture.lib.config.PictureMimeType;
import com.mob.tools.MobLog;
import com.mob.tools.network.HttpConnection;
import com.mob.tools.network.HttpResponseCallback;
import com.mob.tools.network.NetworkHelper;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class BitmapHelper {
    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;
    private static int maxBitmapHeight;
    private static int maxBitmapWidth;

    static {
        int[] iArr = new int[1];
        GLES10.glGetIntegerv(3379, iArr, 0);
        int max = Math.max(iArr[0], 2048);
        maxBitmapWidth = max;
        maxBitmapHeight = max;
    }

    public static Bitmap blur(Bitmap bitmap, int i, int i2) {
        float f = i2;
        int i3 = (int) ((i / f) + 0.5f);
        Bitmap createBitmap = Bitmap.createBitmap((int) ((bitmap.getWidth() / f) + 0.5f), (int) ((bitmap.getHeight() / f) + 0.5f), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        float f2 = 1.0f / f;
        canvas.scale(f2, f2);
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        blur(createBitmap, i3, true);
        return createBitmap;
    }

    private static Bitmap blur(Bitmap bitmap, int i, boolean z) {
        int[] iArr;
        int i2 = i;
        Bitmap copy = z ? bitmap : bitmap.copy(bitmap.getConfig(), true);
        if (i2 < 1) {
            return null;
        }
        int width = copy.getWidth();
        int height = copy.getHeight();
        int i3 = width * height;
        int[] iArr2 = new int[i3];
        copy.getPixels(iArr2, 0, width, 0, 0, width, height);
        int i4 = width - 1;
        int i5 = height - 1;
        int i6 = i2 + i2 + 1;
        int[] iArr3 = new int[i3];
        int[] iArr4 = new int[i3];
        int[] iArr5 = new int[i3];
        int[] iArr6 = new int[Math.max(width, height)];
        int i7 = (i6 + 1) >> 1;
        int i8 = i7 * i7;
        int i9 = i8 * 256;
        int[] iArr7 = new int[i9];
        for (int i10 = 0; i10 < i9; i10++) {
            iArr7[i10] = i10 / i8;
        }
        int[][] iArr8 = (int[][]) Array.newInstance((Class<?>) int.class, i6, 3);
        int i11 = i2 + 1;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        while (i12 < height) {
            Bitmap bitmap2 = copy;
            int i15 = -i2;
            int i16 = 0;
            int i17 = 0;
            int i18 = 0;
            int i19 = 0;
            int i20 = 0;
            int i21 = 0;
            int i22 = 0;
            int i23 = 0;
            int i24 = 0;
            while (i15 <= i2) {
                int i25 = i5;
                int i26 = height;
                int i27 = iArr2[i13 + Math.min(i4, Math.max(i15, 0))];
                int[] iArr9 = iArr8[i15 + i2];
                iArr9[0] = (i27 & 16711680) >> 16;
                iArr9[1] = (i27 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr9[2] = i27 & 255;
                int abs = i11 - Math.abs(i15);
                i16 += iArr9[0] * abs;
                i17 += iArr9[1] * abs;
                i18 += iArr9[2] * abs;
                if (i15 > 0) {
                    i19 += iArr9[0];
                    i20 += iArr9[1];
                    i21 += iArr9[2];
                } else {
                    i22 += iArr9[0];
                    i23 += iArr9[1];
                    i24 += iArr9[2];
                }
                i15++;
                height = i26;
                i5 = i25;
            }
            int i28 = i5;
            int i29 = height;
            int i30 = i2;
            int i31 = 0;
            while (i31 < width) {
                iArr3[i13] = iArr7[i16];
                iArr4[i13] = iArr7[i17];
                iArr5[i13] = iArr7[i18];
                int i32 = i16 - i22;
                int i33 = i17 - i23;
                int i34 = i18 - i24;
                int[] iArr10 = iArr8[((i30 - i2) + i6) % i6];
                int i35 = i22 - iArr10[0];
                int i36 = i23 - iArr10[1];
                int i37 = i24 - iArr10[2];
                if (i12 == 0) {
                    iArr = iArr7;
                    iArr6[i31] = Math.min(i31 + i2 + 1, i4);
                } else {
                    iArr = iArr7;
                }
                int i38 = iArr2[i14 + iArr6[i31]];
                iArr10[0] = (i38 & 16711680) >> 16;
                iArr10[1] = (i38 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr10[2] = i38 & 255;
                int i39 = i19 + iArr10[0];
                int i40 = i20 + iArr10[1];
                int i41 = i21 + iArr10[2];
                i16 = i32 + i39;
                i17 = i33 + i40;
                i18 = i34 + i41;
                i30 = (i30 + 1) % i6;
                int[] iArr11 = iArr8[i30 % i6];
                i22 = i35 + iArr11[0];
                i23 = i36 + iArr11[1];
                i24 = i37 + iArr11[2];
                i19 = i39 - iArr11[0];
                i20 = i40 - iArr11[1];
                i21 = i41 - iArr11[2];
                i13++;
                i31++;
                iArr7 = iArr;
            }
            i14 += width;
            i12++;
            copy = bitmap2;
            height = i29;
            i5 = i28;
        }
        Bitmap bitmap3 = copy;
        int i42 = i5;
        int i43 = height;
        int[] iArr12 = iArr7;
        int i44 = 0;
        while (i44 < width) {
            int i45 = -i2;
            int i46 = i45 * width;
            int i47 = 0;
            int i48 = 0;
            int i49 = 0;
            int i50 = 0;
            int i51 = 0;
            int i52 = 0;
            int i53 = 0;
            int i54 = 0;
            int i55 = 0;
            while (i45 <= i2) {
                int[] iArr13 = iArr6;
                int max = Math.max(0, i46) + i44;
                int[] iArr14 = iArr8[i45 + i2];
                iArr14[0] = iArr3[max];
                iArr14[1] = iArr4[max];
                iArr14[2] = iArr5[max];
                int abs2 = i11 - Math.abs(i45);
                i47 += iArr3[max] * abs2;
                i48 += iArr4[max] * abs2;
                i49 += iArr5[max] * abs2;
                if (i45 > 0) {
                    i50 += iArr14[0];
                    i51 += iArr14[1];
                    i52 += iArr14[2];
                } else {
                    i53 += iArr14[0];
                    i54 += iArr14[1];
                    i55 += iArr14[2];
                }
                int i56 = i42;
                if (i45 < i56) {
                    i46 += width;
                }
                i45++;
                i42 = i56;
                iArr6 = iArr13;
            }
            int[] iArr15 = iArr6;
            int i57 = i42;
            int i58 = i44;
            int i59 = i51;
            int i60 = i52;
            int i61 = 0;
            int i62 = i2;
            int i63 = i50;
            int i64 = i49;
            int i65 = i48;
            int i66 = i47;
            int i67 = i43;
            while (i61 < i67) {
                iArr2[i58] = (iArr2[i58] & (-16777216)) | (iArr12[i66] << 16) | (iArr12[i65] << 8) | iArr12[i64];
                int i68 = i66 - i53;
                int i69 = i65 - i54;
                int i70 = i64 - i55;
                int[] iArr16 = iArr8[((i62 - i2) + i6) % i6];
                int i71 = i53 - iArr16[0];
                int i72 = i54 - iArr16[1];
                int i73 = i55 - iArr16[2];
                if (i44 == 0) {
                    iArr15[i61] = Math.min(i61 + i11, i57) * width;
                }
                int i74 = iArr15[i61] + i44;
                iArr16[0] = iArr3[i74];
                iArr16[1] = iArr4[i74];
                iArr16[2] = iArr5[i74];
                int i75 = i63 + iArr16[0];
                int i76 = i59 + iArr16[1];
                int i77 = i60 + iArr16[2];
                i66 = i68 + i75;
                i65 = i69 + i76;
                i64 = i70 + i77;
                i62 = (i62 + 1) % i6;
                int[] iArr17 = iArr8[i62];
                i53 = i71 + iArr17[0];
                i54 = i72 + iArr17[1];
                i55 = i73 + iArr17[2];
                i63 = i75 - iArr17[0];
                i59 = i76 - iArr17[1];
                i60 = i77 - iArr17[2];
                i58 += width;
                i61++;
                i2 = i;
            }
            i44++;
            i42 = i57;
            i43 = i67;
            iArr6 = iArr15;
            i2 = i;
        }
        bitmap3.setPixels(iArr2, 0, width, 0, 0, width, i43);
        return bitmap3;
    }

    private static boolean bytesStartWith(byte[] bArr, byte[] bArr2) {
        if (bArr == bArr2) {
            return true;
        }
        if (bArr == null || bArr2 == null || bArr.length < bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr2.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static Bitmap captureView(View view, int i, int i2) throws Throwable {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public static Bitmap compressByQuality(Bitmap bitmap, int i) {
        return compressByQuality(bitmap, i, false);
    }

    public static Bitmap compressByQuality(Bitmap bitmap, int i, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, i, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Bitmap compressByQuality(Bitmap bitmap, long j) {
        return compressByQuality(bitmap, j, false);
    }

    public static Bitmap compressByQuality(Bitmap bitmap, long j, boolean z) {
        byte[] byteArray;
        if (isEmptyBitmap(bitmap) || j <= 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        if (byteArrayOutputStream.size() <= j) {
            byteArray = byteArrayOutputStream.toByteArray();
        } else {
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream);
            if (byteArrayOutputStream.size() >= j) {
                byteArray = byteArrayOutputStream.toByteArray();
            } else {
                int i = 0;
                int i2 = 100;
                int i3 = 0;
                while (i < i2) {
                    i3 = (i + i2) / 2;
                    byteArrayOutputStream.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, i3, byteArrayOutputStream);
                    long size = byteArrayOutputStream.size();
                    if (size == j) {
                        break;
                    }
                    if (size > j) {
                        i2 = i3 - 1;
                    } else {
                        i = i3 + 1;
                    }
                }
                if (i2 == i3 - 1) {
                    byteArrayOutputStream.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, i, byteArrayOutputStream);
                }
                byteArray = byteArrayOutputStream.toByteArray();
            }
        }
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Bitmap cropBitmap(Bitmap bitmap, int i, int i2, int i3, int i4) throws Throwable {
        int width = (bitmap.getWidth() - i) - i3;
        int height = (bitmap.getHeight() - i2) - i4;
        if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
            return bitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, -i, -i2, new Paint());
        return createBitmap;
    }

    public static String downloadBitmap(Context context, final String str) throws Throwable {
        final String cachePath = ResHelper.getCachePath(context, "images");
        File file = new File(cachePath, Data.MD5(str));
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        final HashMap hashMap = new HashMap();
        new NetworkHelper().rawGet(str, new HttpResponseCallback() { // from class: com.mob.tools.utils.BitmapHelper.1
            @Override // com.mob.tools.network.HttpResponseCallback
            public void onResponse(HttpConnection httpConnection) throws Throwable {
                int responseCode = httpConnection.getResponseCode();
                if (responseCode != 200) {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream(), Charset.forName("utf-8")));
                    for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                        if (sb.length() > 0) {
                            sb.append('\n');
                        }
                        sb.append(readLine);
                    }
                    bufferedReader.close();
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("error", sb.toString());
                    hashMap2.put("status", Integer.valueOf(responseCode));
                    throw new Throwable(new Hashon().fromHashMap(hashMap2));
                }
                String fileName = BitmapHelper.getFileName(httpConnection, str);
                File file2 = new File(cachePath, fileName);
                if (!file2.getParentFile().exists()) {
                    file2.getParentFile().mkdirs();
                }
                if (file2.exists()) {
                    file2.delete();
                }
                try {
                    Bitmap bitmap = BitmapHelper.getBitmap(new FilterInputStream(httpConnection.getInputStream()) { // from class: com.mob.tools.utils.BitmapHelper.1.1
                        @Override // java.io.FilterInputStream, java.io.InputStream
                        public long skip(long j) throws IOException {
                            long j2 = 0;
                            while (j2 < j) {
                                long skip = this.in.skip(j - j2);
                                if (skip == 0) {
                                    break;
                                }
                                j2 += skip;
                            }
                            return j2;
                        }
                    }, 1);
                    if (bitmap == null || bitmap.isRecycled()) {
                        return;
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    if (fileName.toLowerCase().endsWith(".gif") || fileName.toLowerCase().endsWith(PictureMimeType.PNG)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    } else {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    hashMap.put("bitmap", file2.getAbsolutePath());
                } catch (Throwable th) {
                    if (file2.exists()) {
                        file2.delete();
                    }
                    throw th;
                }
            }
        }, (NetworkHelper.NetworkTimeOut) null);
        return (String) hashMap.get("bitmap");
    }

    public static int[] fixRect(int[] iArr, int[] iArr2) {
        int[] iArr3 = new int[2];
        if (iArr[0] / iArr[1] > iArr2[0] / iArr2[1]) {
            iArr3[0] = iArr2[0];
            iArr3[1] = (int) (((iArr[1] * iArr2[0]) / iArr[0]) + 0.5f);
        } else {
            iArr3[1] = iArr2[1];
            iArr3[0] = (int) (((iArr[0] * iArr2[1]) / iArr[1]) + 0.5f);
        }
        return iArr3;
    }

    public static int[] fixRect_2(int[] iArr, int[] iArr2) {
        int[] iArr3 = new int[2];
        if (iArr[0] / iArr[1] > iArr2[0] / iArr2[1]) {
            iArr3[1] = iArr2[1];
            iArr3[0] = (int) (((iArr[0] * iArr2[1]) / iArr[1]) + 0.5f);
        } else {
            iArr3[0] = iArr2[0];
            iArr3[1] = (int) (((iArr[1] * iArr2[0]) / iArr[0]) + 0.5f);
        }
        return iArr3;
    }

    public static Bitmap getBitmap(Context context, String str) throws Throwable {
        return getBitmap(downloadBitmap(context, str));
    }

    public static Bitmap getBitmap(File file, int i) throws Throwable {
        if (file == null || !file.exists()) {
            return null;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        Bitmap bitmap = getBitmap(fileInputStream, i);
        fileInputStream.close();
        return bitmap;
    }

    public static Bitmap getBitmap(InputStream inputStream, int i) {
        if (inputStream == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = i;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static Bitmap getBitmap(String str) throws Throwable {
        return getBitmap(str, 1);
    }

    public static Bitmap getBitmap(String str, int i) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return getBitmap(new File(str), i);
    }

    public static Bitmap getBitmapByCompressQuality(String str, int i, int i2, int i3, long j) throws Throwable {
        Bitmap bitmapByCompressSize = getBitmapByCompressSize(str, i, i2);
        if (i3 < 10 || i3 > 100) {
            i3 = 100;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap.CompressFormat bmpFormat = getBmpFormat(str);
        bitmapByCompressSize.compress(bmpFormat, i3, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        if (j < 10240) {
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            try {
                byteArrayOutputStream.close();
            } catch (Throwable unused) {
            }
            return decodeByteArray;
        }
        while (byteArray.length > j && i3 >= 11) {
            byteArrayOutputStream.reset();
            i3 -= 6;
            bitmapByCompressSize.compress(bmpFormat, i3, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
        }
        if (i3 != 100) {
            bitmapByCompressSize = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        try {
            byteArrayOutputStream.close();
        } catch (Throwable unused2) {
        }
        return bitmapByCompressSize;
    }

    public static Bitmap getBitmapByCompressSize(String str, int i, int i2) throws Throwable {
        int i3;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        int i4 = options.outWidth;
        int i5 = options.outHeight;
        if (i <= 1 || i2 <= 1) {
            i3 = 1;
        } else {
            float f = 1.0f;
            float min = (Math.min(i4, i5) * 1.0f) / Math.min(i, i2);
            float max = (Math.max(i4, i5) * 1.0f) / Math.max(i, i2);
            float f2 = i4 / i5;
            if (f2 > 2.0f || f2 < 0.5d) {
                while (true) {
                    float f3 = f * 2.0f;
                    if (f3 > min) {
                        break;
                    }
                    f = f3;
                }
                i3 = (int) f;
            } else {
                float min2 = Math.min(min, max);
                while (true) {
                    float f4 = f * 2.0f;
                    if (f4 > min2) {
                        break;
                    }
                    f = f4;
                }
                i3 = (int) f;
            }
        }
        if (i3 < 1) {
            i3 = 1;
        }
        while (true) {
            if (i4 / i3 <= maxBitmapWidth && i5 / i3 <= maxBitmapHeight) {
                BitmapFactory.Options options2 = new BitmapFactory.Options();
                options2.inPreferredConfig = Bitmap.Config.RGB_565;
                options2.inSampleSize = i3;
                return BitmapFactory.decodeFile(str, options2);
            }
            i3++;
        }
    }

    public static Bitmap.CompressFormat getBmpFormat(String str) {
        String lowerCase = str.toLowerCase();
        if (lowerCase.endsWith("png") || lowerCase.endsWith("gif")) {
            return Bitmap.CompressFormat.PNG;
        }
        if (lowerCase.endsWith("jpg") || lowerCase.endsWith("jpeg") || lowerCase.endsWith("bmp") || lowerCase.endsWith("tif")) {
            return Bitmap.CompressFormat.JPEG;
        }
        String mime = getMime(str);
        return (mime.endsWith("png") || mime.endsWith("gif")) ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
    }

    public static Bitmap.CompressFormat getBmpFormat(byte[] bArr) {
        String mime = getMime(bArr);
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        return mime != null ? (mime.endsWith("png") || mime.endsWith("gif")) ? Bitmap.CompressFormat.PNG : compressFormat : compressFormat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getFileName(HttpConnection httpConnection, String str) throws Throwable {
        String str2;
        List<String> list;
        int lastIndexOf;
        List<String> list2;
        Map<String, List<String>> headerFields = httpConnection.getHeaderFields();
        if (headerFields == null || (list2 = headerFields.get("Content-Disposition")) == null || list2.size() <= 0) {
            str2 = null;
        } else {
            str2 = null;
            for (String str3 : list2.get(0).split(";")) {
                if (str3.trim().startsWith(FileDownloadModel.FILENAME)) {
                    String[] split = str3.split("=");
                    if (split.length >= 2) {
                        str2 = split[1];
                        if (!TextUtils.isEmpty(str2) && str2.startsWith("\"") && str2.endsWith("\"")) {
                            str2 = str2.substring(1, str2.length() - 1);
                        }
                    }
                }
            }
        }
        if (str2 != null) {
            return str2;
        }
        String MD5 = Data.MD5(str);
        if (headerFields == null || (list = headerFields.get("Content-Type")) == null || list.size() <= 0) {
            return MD5;
        }
        String str4 = list.get(0);
        String trim = str4 == null ? "" : str4.trim();
        if (trim.startsWith("image/")) {
            String substring = trim.substring("image/".length());
            StringBuilder sb = new StringBuilder();
            sb.append(MD5);
            sb.append(".");
            if ("jpeg".equals(substring)) {
                substring = "jpg";
            }
            sb.append(substring);
            return sb.toString();
        }
        int lastIndexOf2 = str.lastIndexOf(47);
        String substring2 = lastIndexOf2 > 0 ? str.substring(lastIndexOf2 + 1) : null;
        if (substring2 == null || substring2.length() <= 0 || (lastIndexOf = substring2.lastIndexOf(46)) <= 0 || substring2.length() - lastIndexOf >= 10) {
            return MD5;
        }
        return MD5 + substring2.substring(lastIndexOf);
    }

    public static String getMime(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            byte[] bArr = new byte[8];
            fileInputStream.read(bArr);
            fileInputStream.close();
            return getMime(bArr);
        } catch (Exception e) {
            MobLog.getInstance().w(e);
            return "";
        }
    }

    private static String getMime(byte[] bArr) {
        byte[] bArr2 = {-1, -40, -1, -31};
        if (bytesStartWith(bArr, new byte[]{-1, -40, -1, -32}) || bytesStartWith(bArr, bArr2)) {
            return "jpg";
        }
        if (bytesStartWith(bArr, new byte[]{-119, 80, 78, 71})) {
            return "png";
        }
        if (bytesStartWith(bArr, "GIF".getBytes())) {
            return "gif";
        }
        if (bytesStartWith(bArr, "BM".getBytes())) {
            return "bmp";
        }
        return (bytesStartWith(bArr, new byte[]{73, 73, 42}) || bytesStartWith(bArr, new byte[]{77, 77, 42})) ? "tif" : "";
    }

    public static boolean isBlackBitmap(Bitmap bitmap) throws Throwable {
        if (bitmap == null || bitmap.isRecycled()) {
            return true;
        }
        int[] iArr = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(iArr, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= iArr.length) {
                break;
            }
            if ((iArr[i] & ViewCompat.MEASURED_SIZE_MASK) != 0) {
                z = true;
                break;
            }
            i++;
        }
        return !z;
    }

    private static boolean isEmptyBitmap(Bitmap bitmap) {
        return bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }

    public static int mixAlpha(int i, int i2) {
        int i3 = i >>> 24;
        int i4 = 255 - i3;
        return ((((((i & 16711680) >>> 16) * i3) + (((16711680 & i2) >>> 16) * i4)) / 255) << 16) | (-16777216) | ((((((i & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>> 8) * i3) + (((65280 & i2) >>> 8) * i4)) / 255) << 8) | (((i3 * (i & 255)) + (i4 * (i2 & 255))) / 255);
    }

    public static Bitmap roundBitmap(Bitmap bitmap, int i, int i2, float f, float f2, float f3, float f4) throws Throwable {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Rect rect = new Rect(0, 0, width, height);
        Bitmap createBitmap = (width == i && height == i2) ? Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888) : Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect2 = new Rect(0, 0, i, i2);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        float[] fArr = {f, f, f2, f2, f3, f3, f4, f4};
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(fArr, new RectF(0.0f, 0.0f, 0.0f, 0.0f), fArr));
        shapeDrawable.setBounds(rect2);
        shapeDrawable.draw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect2, paint);
        return createBitmap;
    }

    public static boolean save(Bitmap bitmap, File file, Bitmap.CompressFormat compressFormat, boolean z) {
        boolean z2;
        Closeable[] closeableArr;
        BufferedOutputStream bufferedOutputStream;
        if (isEmptyBitmap(bitmap) || !FileUtils.createFileByDeleteOldFile(file)) {
            return false;
        }
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            } catch (Throwable th) {
                th = th;
            }
            try {
                try {
                    z2 = bitmap.compress(compressFormat, 100, bufferedOutputStream);
                    if (z) {
                        try {
                            if (!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            bufferedOutputStream2 = bufferedOutputStream;
                            MobLog.getInstance().d(th);
                            closeableArr = new Closeable[]{bufferedOutputStream2};
                            FileUtils.closeIO(closeableArr);
                            return z2;
                        }
                    }
                    closeableArr = new Closeable[]{bufferedOutputStream};
                } catch (Throwable th3) {
                    th = th3;
                    bufferedOutputStream2 = bufferedOutputStream;
                    FileUtils.closeIO(bufferedOutputStream2);
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                bufferedOutputStream2 = bufferedOutputStream;
                z2 = false;
                MobLog.getInstance().d(th);
                closeableArr = new Closeable[]{bufferedOutputStream2};
                FileUtils.closeIO(closeableArr);
                return z2;
            }
            FileUtils.closeIO(closeableArr);
            return z2;
        } catch (Throwable th5) {
            th = th5;
        }
    }

    public static boolean save(Bitmap bitmap, String str, Bitmap.CompressFormat compressFormat) {
        return save(bitmap, FileUtils.getFileByPath(str), compressFormat, false);
    }

    public static String saveBitmap(Context context, Bitmap bitmap) throws Throwable {
        return saveBitmap(context, bitmap, Bitmap.CompressFormat.JPEG, 80);
    }

    public static String saveBitmap(Context context, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int i) throws Throwable {
        File file = new File(ResHelper.getCachePath(context, "images"), String.valueOf(System.currentTimeMillis()) + (compressFormat == Bitmap.CompressFormat.PNG ? PictureMimeType.PNG : ".jpg"));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmap.compress(compressFormat, i, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return file.getAbsolutePath();
    }

    public static String saveBitmapByCompress(String str, int i, int i2, int i3) throws Throwable {
        Bitmap bitmapByCompressSize = getBitmapByCompressSize(str, i, i2);
        int i4 = 10;
        if (i3 > 100) {
            i4 = 100;
        } else if (i3 >= 10) {
            i4 = i3;
        }
        Bitmap.CompressFormat bmpFormat = getBmpFormat(str);
        String str2 = bmpFormat == Bitmap.CompressFormat.PNG ? PictureMimeType.PNG : ".jpg";
        File file = new File(new File(str).getParent(), String.valueOf(System.currentTimeMillis()) + str2);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmapByCompressSize.compress(bmpFormat, i4, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return file.getAbsolutePath();
    }

    public static String saveViewToImage(View view) throws Throwable {
        if (view == null) {
            return null;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }
        return saveViewToImage(view, width, height);
    }

    public static String saveViewToImage(View view, int i, int i2) throws Throwable {
        Bitmap captureView = captureView(view, i, i2);
        if (captureView == null || captureView.isRecycled()) {
            return null;
        }
        File file = new File(ResHelper.getCachePath(view.getContext(), "screenshot"), String.valueOf(System.currentTimeMillis()) + ".jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        captureView.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return file.getAbsolutePath();
    }

    public static Bitmap scaleBitmapByHeight(Context context, int i, int i2) throws Throwable {
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i);
        boolean z = i2 != decodeResource.getHeight();
        Bitmap scaleBitmapByHeight = scaleBitmapByHeight(decodeResource, i2);
        if (z) {
            decodeResource.recycle();
        }
        return scaleBitmapByHeight;
    }

    public static Bitmap scaleBitmapByHeight(Bitmap bitmap, int i) throws Throwable {
        return Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() * i) / bitmap.getHeight(), i, true);
    }
}
