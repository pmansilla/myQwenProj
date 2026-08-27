package com.czw.smartkit.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import com.czw.utils.LogUtil;
import com.luck.picture.lib.config.PictureMimeType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.panpf.sketch.uri.FileVariantUriModel;

/* loaded from: classes.dex */
public class BitmapUtils {
    public static byte[] Bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] bArr) {
        if (bArr.length == 0) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
    }

    public static Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        for (int i = 100; byteArrayOutputStream.toByteArray().length / 1024 > 50 && i > 12; i -= 12) {
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, i, byteArrayOutputStream);
        }
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), null, null);
            bitmap.recycle();
            System.gc();
            return decodeStream;
        } catch (Exception unused) {
            return null;
        }
    }

    public static Bitmap getCropped2Bitmap(Bitmap bitmap) {
        if (bitmap.getWidth() != 100 || bitmap.getHeight() != 100) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    public static Bitmap getPicFromBytes(byte[] bArr, BitmapFactory.Options options) {
        if (bArr != null) {
            return options != null ? BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options) : BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        }
        return null;
    }

    public static Bitmap getimage(String str, int i, int i2) {
        int i3;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        int i4 = options.outWidth;
        int i5 = options.outHeight;
        if (i4 > i5 && i4 > i2) {
            double d = options.outWidth;
            Double.isNaN(d);
            double d2 = i2;
            Double.isNaN(d2);
            i3 = (int) Math.ceil((d * 1.0d) / d2);
        } else if (i4 >= i5 || i5 <= i) {
            i3 = 1;
        } else {
            double d3 = options.outHeight;
            Double.isNaN(d3);
            double d4 = i;
            Double.isNaN(d4);
            i3 = (int) Math.ceil((d3 * 1.0d) / d4);
        }
        if (i3 <= 0) {
            i3 = 1;
        }
        options.inSampleSize = i3;
        options.inJustDecodeBounds = false;
        return compressImage(BitmapFactory.decodeFile(str, options));
    }

    public static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] bArr = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                inputStream.close();
                return byteArray;
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public static Bitmap resizeBitmap(float f, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        LogUtil.e("width" + width + "height" + height);
        float f2 = f / ((float) width);
        float f3 = f / ((float) height);
        if (f3 >= f2) {
            f3 = f2;
        }
        Matrix matrix = new Matrix();
        if (f3 > 1.0f) {
            return bitmap;
        }
        matrix.postScale(f3, f3);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        System.gc();
        return createBitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f = i / width;
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f = i / width;
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        System.gc();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        createBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        if (byteArrayOutputStream.toByteArray().length / 1024 > 20) {
            byteArrayOutputStream.reset();
            createBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        }
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), null, null);
            createBitmap.recycle();
            System.gc();
            return decodeStream;
        } catch (Exception unused) {
            return null;
        }
    }

    public static Bitmap resizeBitmaps(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f = i / height;
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        System.gc();
        return createBitmap;
    }

    public static String saveBitmap(Bitmap bitmap, Activity activity) throws IOException {
        Date date = new Date();
        String str = "bit" + new SimpleDateFormat("yyyyMMddHHmmss").format(date) + PictureMimeType.PNG;
        File file = new File(Environment.getExternalStorageDirectory() + "/ecor");
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(file, str);
        String str2 = Environment.getExternalStorageDirectory() + "/ecor/" + str;
        Log.e("taa", "filename=" + str2.toString());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 70, fileOutputStream)) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            activity.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse(FileVariantUriModel.SCHEME + str2)));
            Log.e("taa", "����ɹ�");
            return str2;
        } catch (FileNotFoundException e) {
            Log.e("taa", "�ļ��쳣");
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            Log.e("taa", "IO�쳣");
            e2.printStackTrace();
            return null;
        }
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int i) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float f = i;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }
}
