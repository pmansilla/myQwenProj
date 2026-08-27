package me.panpf.sketch.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import java.io.IOException;
import java.io.InputStream;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.util.ExifInterface;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ImageOrientationCorrector {
    public static final int PAINT_FLAGS = 6;

    public static int getExifOrientationDegrees(int i) {
        switch (i) {
            case 3:
            case 4:
                return 180;
            case 5:
            case 6:
                return 90;
            case 7:
            case 8:
                return SubsamplingScaleImageView.ORIENTATION_270;
            default:
                return 0;
        }
    }

    public static int getExifOrientationTranslation(int i) {
        if (i != 2 && i != 7) {
            switch (i) {
                case 4:
                case 5:
                    break;
                default:
                    return 1;
            }
        }
        return -1;
    }

    public static void initializeMatrixForExifRotation(int i, Matrix matrix) {
        switch (i) {
            case 2:
                matrix.setScale(-1.0f, 1.0f);
                return;
            case 3:
                matrix.setRotate(180.0f);
                return;
            case 4:
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 5:
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 6:
                matrix.setRotate(90.0f);
                return;
            case 7:
                matrix.setRotate(270.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 8:
                matrix.setRotate(270.0f);
                return;
            default:
                return;
        }
    }

    public static String toName(int i) {
        switch (i) {
            case 0:
                return "UNDEFINED";
            case 1:
                return "NORMAL";
            case 2:
                return "FLIP_HORIZONTAL";
            case 3:
                return "ROTATE_180";
            case 4:
                return "FLIP_VERTICAL";
            case 5:
                return "TRANSPOSE";
            case 6:
                return "ROTATE_90";
            case 7:
                return "TRANSVERSE";
            case 8:
                return "ROTATE_270";
            default:
                return String.valueOf(i);
        }
    }

    public boolean hasRotate(int i) {
        return (i == 0 || i == 1) ? false : true;
    }

    public int readExifOrientation(InputStream inputStream) throws IOException {
        return new ExifInterface(inputStream).getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
    }

    public int readExifOrientation(String str, InputStream inputStream) throws IOException {
        if (support(str)) {
            return readExifOrientation(inputStream);
        }
        return 0;
    }

    public int readExifOrientation(String str, DataSource dataSource) {
        InputStream inputStream;
        Throwable th;
        IOException e;
        if (!support(str)) {
            return 0;
        }
        try {
            inputStream = dataSource.getInputStream();
            try {
                try {
                    int readExifOrientation = readExifOrientation(inputStream);
                    SketchUtils.close(inputStream);
                    return readExifOrientation;
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    SketchUtils.close(inputStream);
                    return 0;
                }
            } catch (Throwable th2) {
                th = th2;
                SketchUtils.close(inputStream);
                throw th;
            }
        } catch (IOException e3) {
            inputStream = null;
            e = e3;
        } catch (Throwable th3) {
            inputStream = null;
            th = th3;
            SketchUtils.close(inputStream);
            throw th;
        }
    }

    public void reverseRotate(Rect rect, int i, int i2, int i3) {
        if (hasRotate(i3)) {
            int exifOrientationDegrees = 360 - getExifOrientationDegrees(i3);
            if (exifOrientationDegrees == 90) {
                int i4 = rect.top;
                rect.top = rect.left;
                rect.left = i2 - rect.bottom;
                rect.bottom = rect.right;
                rect.right = i2 - i4;
                return;
            }
            if (exifOrientationDegrees == 180) {
                int i5 = rect.left;
                int i6 = rect.top;
                rect.left = i - rect.right;
                rect.right = i - i5;
                rect.top = i2 - rect.bottom;
                rect.bottom = i2 - i6;
                return;
            }
            if (exifOrientationDegrees == 270) {
                int i7 = rect.left;
                rect.left = rect.top;
                rect.top = i - rect.right;
                rect.right = rect.bottom;
                rect.bottom = i - i7;
            }
        }
    }

    public Bitmap rotate(Bitmap bitmap, int i, BitmapPool bitmapPool) {
        if (!hasRotate(i)) {
            return null;
        }
        Matrix matrix = new Matrix();
        initializeMatrixForExifRotation(i, matrix);
        RectF rectF = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        matrix.mapRect(rectF);
        int width = (int) rectF.width();
        int height = (int) rectF.height();
        int exifOrientationDegrees = getExifOrientationDegrees(i);
        Bitmap.Config config = bitmap.getConfig() != null ? bitmap.getConfig() : null;
        if (exifOrientationDegrees % 90 != 0 && config != Bitmap.Config.ARGB_8888) {
            config = Bitmap.Config.ARGB_8888;
        }
        Bitmap orMake = bitmapPool.getOrMake(width, height, config);
        matrix.postTranslate(-rectF.left, -rectF.top);
        new Canvas(orMake).drawBitmap(bitmap, matrix, new Paint(6));
        return orMake;
    }

    public void rotateSize(BitmapFactory.Options options, int i) {
        if (hasRotate(i)) {
            Matrix matrix = new Matrix();
            initializeMatrixForExifRotation(i, matrix);
            RectF rectF = new RectF(0.0f, 0.0f, options.outWidth, options.outHeight);
            matrix.mapRect(rectF);
            options.outWidth = (int) rectF.width();
            options.outHeight = (int) rectF.height();
        }
    }

    public void rotateSize(Point point, int i) {
        if (hasRotate(i)) {
            Matrix matrix = new Matrix();
            initializeMatrixForExifRotation(i, matrix);
            RectF rectF = new RectF(0.0f, 0.0f, point.x, point.y);
            matrix.mapRect(rectF);
            point.x = (int) rectF.width();
            point.y = (int) rectF.height();
        }
    }

    public void rotateSize(ImageAttrs imageAttrs, int i) {
        if (hasRotate(i)) {
            Matrix matrix = new Matrix();
            initializeMatrixForExifRotation(i, matrix);
            RectF rectF = new RectF(0.0f, 0.0f, imageAttrs.getWidth(), imageAttrs.getHeight());
            matrix.mapRect(rectF);
            imageAttrs.resetSize((int) rectF.width(), (int) rectF.height());
        }
    }

    public boolean support(String str) {
        return ImageType.JPEG.getMimeType().equalsIgnoreCase(str);
    }

    @NonNull
    public String toString() {
        return "ImageOrientationCorrector";
    }
}
