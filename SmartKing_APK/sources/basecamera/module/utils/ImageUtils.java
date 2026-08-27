package basecamera.module.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import basecamera.module.activity.model.Album;
import basecamera.module.activity.model.PhotoItem;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.wx.wheelview.common.WheelConstants;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class ImageUtils {

    /* loaded from: classes.dex */
    public interface LoadImageCallback {
        void callback(Bitmap bitmap);
    }

    /* loaded from: classes.dex */
    private static class LoadImageUriTask extends AsyncTask<Void, Void, Bitmap> {
        private LoadImageCallback callback;
        private final Context context;
        private final Uri imageUri;

        public LoadImageUriTask(Context context, Uri uri, LoadImageCallback loadImageCallback) {
            this.imageUri = uri;
            this.context = context;
            this.callback = loadImageCallback;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void... voidArr) {
            InputStream openStream;
            try {
                if (!this.imageUri.getScheme().startsWith("http") && !this.imageUri.getScheme().startsWith("https")) {
                    openStream = this.context.getContentResolver().openInputStream(this.imageUri);
                    return BitmapFactory.decodeStream(openStream);
                }
                openStream = new URL(this.imageUri.toString()).openStream();
                return BitmapFactory.decodeStream(openStream);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute((LoadImageUriTask) bitmap);
            this.callback.callback(bitmap);
        }
    }

    /* loaded from: classes.dex */
    private static class LoadSmallPicTask extends AsyncTask<Void, Void, Bitmap> {
        private LoadImageCallback callback;
        private final Context context;
        private final Uri imageUri;

        public LoadSmallPicTask(Context context, Uri uri, LoadImageCallback loadImageCallback) {
            this.imageUri = uri;
            this.context = context;
            this.callback = loadImageCallback;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void... voidArr) {
            return ImageUtils.getResizedBitmap(this.context, this.imageUri, WheelConstants.WHEEL_SCROLL_DELAY_DURATION, WheelConstants.WHEEL_SCROLL_DELAY_DURATION);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute((LoadSmallPicTask) bitmap);
            this.callback.callback(bitmap);
        }
    }

    public static void asyncLoadImage(Context context, Uri uri, LoadImageCallback loadImageCallback) {
        new LoadImageUriTask(context, uri, loadImageCallback).execute(new Void[0]);
    }

    public static void asyncLoadSmallImage(Context context, Uri uri, LoadImageCallback loadImageCallback) {
        new LoadSmallPicTask(context, uri, loadImageCallback).execute(new Void[0]);
    }

    public static Bitmap byteToBitmap(byte[] bArr) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Bitmap bitmap = (Bitmap) new SoftReference(BitmapFactory.decodeStream(byteArrayInputStream, null, options)).get();
        try {
            byteArrayInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeBitmapWithOrientation(String str, int i, int i2) {
        return decodeBitmapWithSize(str, i, i2, false);
    }

    public static Bitmap decodeBitmapWithOrientationMax(String str, int i, int i2) {
        return decodeBitmapWithSize(str, i, i2, true);
    }

    private static Bitmap decodeBitmapWithSize(String str, int i, int i2, boolean z) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inInputShareable = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFile(str, options);
        int imageDegrees = getImageDegrees(str);
        if (imageDegrees == 90 || imageDegrees == 270) {
            i2 = i;
            i = i2;
        }
        if (z) {
            options.inSampleSize = (int) Math.min(options.outWidth / i, options.outHeight / i2);
        } else {
            options.inSampleSize = (int) Math.max(options.outWidth / i, options.outHeight / i2);
        }
        options.inJustDecodeBounds = false;
        return imageWithFixedRotation(BitmapFactory.decodeFile(str, options), imageDegrees);
    }

    public static Map<String, Album> findGalleries(Context context, List<String> list, long j) {
        list.clear();
        list.add(FileUtils.getInst(context).getSystemPhotoPath());
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{FileDownloadModel.ID, "_data", "date_added"}, "_size>?", new String[]{"100000"}, "date_added desc");
        HashMap hashMap = new HashMap();
        while (query.moveToNext()) {
            String string = query.getString(1);
            if (string.lastIndexOf(FileUriModel.SCHEME) >= 1) {
                String substring = string.substring(0, string.lastIndexOf(FileUriModel.SCHEME));
                if (!hashMap.keySet().contains(substring)) {
                    String substring2 = substring.substring(substring.lastIndexOf(FileUriModel.SCHEME) + 1, substring.length());
                    if (!list.contains(substring)) {
                        list.add(substring);
                    }
                    hashMap.put(substring, new Album(substring2, substring, new ArrayList()));
                }
                ((Album) hashMap.get(substring)).getPhotos().add(new PhotoItem(string, query.getInt(2) * 1000));
            }
        }
        ArrayList<PhotoItem> findPicsInDir = FileUtils.getInst(context).findPicsInDir(FileUtils.getInst(context).getSystemPhotoPath());
        if (findPicsInDir.isEmpty()) {
            hashMap.remove(FileUtils.getInst(context).getSystemPhotoPath());
            list.remove(FileUtils.getInst(context).getSystemPhotoPath());
        } else {
            hashMap.put(FileUtils.getInst(context).getSystemPhotoPath(), new Album("胶卷相册", FileUtils.getInst(context).getSystemPhotoPath(), findPicsInDir));
        }
        return hashMap;
    }

    public static int getImageDegrees(String str) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static float getImageRadio(ContentResolver contentResolver, Uri uri) {
        InputStream inputStream;
        InputStream inputStream2 = null;
        try {
            try {
                inputStream = contentResolver.openInputStream(uri);
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
            inputStream = inputStream2;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            int i = options.outWidth;
            int i2 = options.outHeight;
            float f = i2 > i ? i2 / i : i / i2;
            IOUtil.closeStream(inputStream);
            return f;
        } catch (Exception e2) {
            e = e2;
            inputStream2 = inputStream;
            e.printStackTrace();
            IOUtil.closeStream(inputStream2);
            return 1.0f;
        } catch (Throwable th2) {
            th = th2;
            IOUtil.closeStream(inputStream);
            throw th;
        }
    }

    public static int getMiniSize(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        return Math.min(options.outHeight, options.outWidth);
    }

    public static Bitmap getResizedBitmap(Context context, Uri uri, int i, int i2) {
        InputStream inputStream;
        InputStream inputStream2;
        BitmapFactory.Options options;
        InputStream openInputStream;
        try {
            try {
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                inputStream2 = context.getContentResolver().openInputStream(uri);
                try {
                    BitmapFactory.decodeStream(inputStream2, null, options);
                    options.outWidth = i;
                    options.outHeight = i2;
                    options.inJustDecodeBounds = false;
                    IOUtil.closeStream(inputStream2);
                    openInputStream = context.getContentResolver().openInputStream(uri);
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e2) {
            e = e2;
            inputStream2 = null;
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
        }
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream, null, options);
            IOUtil.closeStream(openInputStream);
            return decodeStream;
        } catch (Exception e3) {
            e = e3;
            inputStream2 = openInputStream;
            e.printStackTrace();
            IOUtil.closeStream(inputStream2);
            return null;
        } catch (Throwable th3) {
            th = th3;
            inputStream = openInputStream;
            IOUtil.closeStream(inputStream);
            throw th;
        }
    }

    public static Bitmap imageWithFixedRotation(Bitmap bitmap, int i) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        if (i == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (createBitmap != bitmap) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static boolean isSquare(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options);
            return options.outHeight == options.outWidth;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isSquare(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        return options.outHeight == options.outWidth;
    }

    public static String saveToFile(Context context, String str, String str2, boolean z, Bitmap bitmap) throws FileNotFoundException, IOException {
        File file;
        if (z) {
            File file2 = new File(str);
            new Date();
            String str3 = str2 + ".jpg";
            if (!file2.exists()) {
                FileUtils.getInst(context).mkdir(file2);
            }
            file = new File(file2, str3);
        } else {
            File file3 = new File(str);
            if (!file3.getParentFile().exists()) {
                FileUtils.getInst(context).mkdir(file3.getParentFile());
            }
            file = file3;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
        IOUtil.closeStream(fileOutputStream);
        return file.getPath();
    }

    public static String saveToFile(Context context, String str, boolean z, Bitmap bitmap) throws FileNotFoundException, IOException {
        File file;
        if (z) {
            File file2 = new File(str);
            Date date = new Date();
            String str2 = new SimpleDateFormat("yyyyMMddHHmmss").format(date) + ".jpg";
            if (!file2.exists()) {
                FileUtils.getInst(context).mkdir(file2);
            }
            file = new File(file2, str2);
        } else {
            File file3 = new File(str);
            if (!file3.getParentFile().exists()) {
                FileUtils.getInst(context).mkdir(file3.getParentFile());
            }
            file = file3;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
        IOUtil.closeStream(fileOutputStream);
        return file.getPath();
    }
}
