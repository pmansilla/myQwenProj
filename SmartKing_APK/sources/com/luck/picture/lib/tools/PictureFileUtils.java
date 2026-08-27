package com.luck.picture.lib.tools;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.amap.location.common.model.AmapLoc;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class PictureFileUtils {
    public static final String APP_NAME = "PictureSelector";
    public static final String CAMERA_AUDIO_PATH = "/PictureSelector/CameraAudio/";
    public static final String CAMERA_PATH = "/PictureSelector/CameraImage/";
    public static final String CROP_PATH = "/PictureSelector/CropImage/";
    private static String DEFAULT_CACHE_DIR = "picture_cache";
    public static final String POSTFIX = ".JPEG";
    public static final String POST_AUDIO = ".mp3";
    public static final String POST_VIDEO = ".mp4";
    static final String TAG = "PictureFileUtils";

    private PictureFileUtils() {
    }

    public static void copyAudioFile(@NonNull String str, @NonNull String str2) throws IOException {
        FileChannel fileChannel;
        if (str.equalsIgnoreCase(str2)) {
            return;
        }
        FileChannel fileChannel2 = null;
        try {
            fileChannel = new FileInputStream(new File(str)).getChannel();
            try {
                FileChannel channel = new FileOutputStream(new File(str2)).getChannel();
                try {
                    fileChannel.transferTo(0L, fileChannel.size(), channel);
                    fileChannel.close();
                    if (fileChannel != null) {
                        fileChannel.close();
                    }
                    if (channel != null) {
                        channel.close();
                    }
                    deleteFile(str);
                } catch (Throwable th) {
                    fileChannel2 = channel;
                    th = th;
                    if (fileChannel != null) {
                        fileChannel.close();
                    }
                    if (fileChannel2 != null) {
                        fileChannel2.close();
                    }
                    deleteFile(str);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
            fileChannel = null;
        }
    }

    public static void copyFile(@NonNull String str, @NonNull String str2) throws IOException {
        FileChannel fileChannel;
        FileChannel channel;
        if (str.equalsIgnoreCase(str2)) {
            return;
        }
        FileChannel fileChannel2 = null;
        try {
            channel = new FileInputStream(new File(str)).getChannel();
            try {
                fileChannel = new FileOutputStream(new File(str2)).getChannel();
            } catch (Throwable th) {
                fileChannel2 = channel;
                th = th;
                fileChannel = null;
            }
        } catch (Throwable th2) {
            th = th2;
            fileChannel = null;
        }
        try {
            channel.transferTo(0L, channel.size(), fileChannel);
            channel.close();
            if (channel != null) {
                channel.close();
            }
            if (fileChannel != null) {
                fileChannel.close();
            }
        } catch (Throwable th3) {
            fileChannel2 = channel;
            th = th3;
            if (fileChannel2 != null) {
                fileChannel2.close();
            }
            if (fileChannel != null) {
                fileChannel.close();
            }
            throw th;
        }
    }

    public static File createCameraFile(Context context, int i, String str, String str2) {
        if (i == 3) {
            if (TextUtils.isEmpty(str)) {
                str = CAMERA_AUDIO_PATH;
            }
        } else if (TextUtils.isEmpty(str)) {
            str = CAMERA_PATH;
        }
        return i == 3 ? createMediaFile(context, str, i, str2) : createMediaFile(context, str, i, str2);
    }

    public static File createCropFile(Context context, int i, String str) {
        return createMediaFile(context, CROP_PATH, i, str);
    }

    public static String createDir(Context context, String str, String str2) {
        File file;
        File externalStorageDirectory = Environment.getExternalStorageState().equals("mounted") ? Environment.getExternalStorageDirectory() : context.getCacheDir();
        if (TextUtils.isEmpty(str2)) {
            file = new File(externalStorageDirectory.getAbsolutePath() + "/PictureSelector");
        } else {
            file = new File(externalStorageDirectory.getAbsolutePath() + str2);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file + FileUriModel.SCHEME + str;
    }

    private static File createMediaFile(Context context, String str, int i, String str2) {
        File file = new File((Environment.getExternalStorageState().equals("mounted") ? Environment.getExternalStorageDirectory() : context.getCacheDir()).getAbsolutePath() + str);
        if (!file.exists()) {
            file.mkdirs();
        }
        String str3 = "PictureSelector_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date()) + "";
        switch (i) {
            case 1:
                if (TextUtils.isEmpty(str2)) {
                    str2 = ".JPEG";
                }
                return new File(file, str3 + str2);
            case 2:
                return new File(file, str3 + POST_VIDEO);
            case 3:
                return new File(file, str3 + POST_AUDIO);
            default:
                return null;
        }
    }

    public static void deleteCacheDirFile(Context context) {
        File cacheDir = context.getCacheDir();
        File file = new File(context.getCacheDir() + "/picture_cache");
        File file2 = new File(context.getCacheDir() + "/luban_disk_cache");
        if (cacheDir != null) {
            for (File file3 : cacheDir.listFiles()) {
                if (file3.isFile()) {
                    file3.delete();
                }
            }
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file4 : listFiles) {
                if (file4.isFile()) {
                    file4.delete();
                }
            }
        }
        File[] listFiles2 = file2.listFiles();
        if (listFiles2 != null) {
            for (File file5 : listFiles2) {
                if (file5.isFile()) {
                    file5.delete();
                }
            }
        }
    }

    public static void deleteFile(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            new File(str).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDCIMCameraPath() {
        try {
            return "%" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0051, code lost:
    
        if (r7 == null) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x002d, code lost:
    
        if (r7 != null) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x002f, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0054, code lost:
    
        return null;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0058  */
    /* JADX WARN: Type inference failed for: r7v0, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v3, types: [android.database.Cursor] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getDataColumn(android.content.Context r7, android.net.Uri r8, java.lang.String r9, java.lang.String[] r10) {
        /*
            java.lang.String r0 = "_data"
            java.lang.String[] r3 = new java.lang.String[]{r0}
            r0 = 0
            android.content.ContentResolver r1 = r7.getContentResolver()     // Catch: java.lang.Throwable -> L33 java.lang.IllegalArgumentException -> L36
            r6 = 0
            r2 = r8
            r4 = r9
            r5 = r10
            android.database.Cursor r7 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L33 java.lang.IllegalArgumentException -> L36
            if (r7 == 0) goto L2d
            boolean r8 = r7.moveToFirst()     // Catch: java.lang.IllegalArgumentException -> L2b java.lang.Throwable -> L55
            if (r8 == 0) goto L2d
            java.lang.String r8 = "_data"
            int r8 = r7.getColumnIndexOrThrow(r8)     // Catch: java.lang.IllegalArgumentException -> L2b java.lang.Throwable -> L55
            java.lang.String r8 = r7.getString(r8)     // Catch: java.lang.IllegalArgumentException -> L2b java.lang.Throwable -> L55
            if (r7 == 0) goto L2a
            r7.close()
        L2a:
            return r8
        L2b:
            r8 = move-exception
            goto L38
        L2d:
            if (r7 == 0) goto L54
        L2f:
            r7.close()
            goto L54
        L33:
            r8 = move-exception
            r7 = r0
            goto L56
        L36:
            r8 = move-exception
            r7 = r0
        L38:
            java.lang.String r9 = "PictureFileUtils"
            java.util.Locale r10 = java.util.Locale.getDefault()     // Catch: java.lang.Throwable -> L55
            java.lang.String r1 = "getDataColumn: _data - [%s]"
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L55
            r3 = 0
            java.lang.String r8 = r8.getMessage()     // Catch: java.lang.Throwable -> L55
            r2[r3] = r8     // Catch: java.lang.Throwable -> L55
            java.lang.String r8 = java.lang.String.format(r10, r1, r2)     // Catch: java.lang.Throwable -> L55
            android.util.Log.i(r9, r8)     // Catch: java.lang.Throwable -> L55
            if (r7 == 0) goto L54
            goto L2f
        L54:
            return r0
        L55:
            r8 = move-exception
        L56:
            if (r7 == 0) goto L5b
            r7.close()
        L5b:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.luck.picture.lib.tools.PictureFileUtils.getDataColumn(android.content.Context, android.net.Uri, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public static List<String> getDirFiles(String str) {
        File file = new File(str);
        ArrayList arrayList = new ArrayList();
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                String absolutePath = file2.getAbsolutePath();
                if (absolutePath.endsWith(".jpg") || absolutePath.endsWith(".jpeg") || absolutePath.endsWith(PictureMimeType.PNG) || absolutePath.endsWith(".gif") || absolutePath.endsWith(".webp")) {
                    arrayList.add(absolutePath);
                }
            }
        }
        return arrayList;
    }

    public static String getDiskCacheDir(Context context) {
        return ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
    }

    @SuppressLint({"NewApi"})
    public static String getPath(Context context, Uri uri) {
        Uri uri2 = null;
        if ((Build.VERSION.SDK_INT >= 19) && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + FileUriModel.SCHEME + split[1];
                }
            } else {
                if (isDownloadsDocument(uri)) {
                    return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
                }
                if (isMediaDocument(uri)) {
                    String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                    String str = split2[0];
                    if (PictureConfig.IMAGE.equals(str)) {
                        uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if (PictureConfig.VIDEO.equals(str)) {
                        uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(str)) {
                        uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
                }
            }
        } else {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                return isGooglePhotosUri(uri) ? uri.getLastPathSegment() : getDataColumn(context, uri, null, null);
            }
            if (AmapLoc.TYPE_OFFLINE_CELL.equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    public static File getPhotoCacheDir(Context context, File file) {
        String str;
        File cacheDir = context.getCacheDir();
        String name = file.getName();
        if (cacheDir == null) {
            if (Log.isLoggable(TAG, 6)) {
                Log.e(TAG, "default disk cache dir is null");
            }
            return file;
        }
        File file2 = new File(cacheDir, DEFAULT_CACHE_DIR);
        if (!file2.mkdirs() && (!file2.exists() || !file2.isDirectory())) {
            return file;
        }
        if (name.endsWith(".webp")) {
            str = System.currentTimeMillis() + ".webp";
        } else {
            str = System.currentTimeMillis() + PictureMimeType.PNG;
        }
        return new File(file2, str);
    }

    public static int isDamage(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        return (options.mCancel || options.outWidth == -1 || options.outHeight == -1) ? -1 : 0;
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static int readPictureDegree(String str) {
        try {
            int attributeInt = new ExifInterface(str).getAttributeInt(me.panpf.sketch.util.ExifInterface.TAG_ORIENTATION, 1);
            if (attributeInt == 3) {
                return 180;
            }
            if (attributeInt == 6) {
                return 90;
            }
            if (attributeInt != 8) {
                return 0;
            }
            return SubsamplingScaleImageView.ORIENTATION_270;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Bitmap rotaingImageView(int i, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        System.out.println("angle2=" + i);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static void saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        float f;
        float f2;
        float f3;
        float f4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= height) {
            f4 = width / 2;
            f3 = width;
            f2 = f3;
            f = 0.0f;
        } else {
            f = (width - height) / 2;
            f2 = height;
            f3 = width - f;
            width = height;
            f4 = height / 2;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect((int) f, (int) 0.0f, (int) f3, (int) f2);
        Rect rect2 = new Rect((int) 0.0f, (int) 0.0f, (int) f2, (int) f2);
        RectF rectF = new RectF(rect2);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, f4, f4, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect2, paint);
        return createBitmap;
    }
}
