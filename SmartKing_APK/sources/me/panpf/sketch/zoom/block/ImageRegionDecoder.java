package me.panpf.sketch.zoom.block;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import java.io.IOException;
import java.io.InputStream;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.decode.ImageDecodeUtils;
import me.panpf.sketch.decode.ImageOrientationCorrector;
import me.panpf.sketch.decode.ImageType;
import me.panpf.sketch.uri.GetDataSourceException;
import me.panpf.sketch.uri.UriModel;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ImageRegionDecoder {
    private final int exifOrientation;
    private Point imageSize;
    private ImageType imageType;
    private String imageUri;
    private BitmapRegionDecoder regionDecoder;

    ImageRegionDecoder(String str, Point point, ImageType imageType, int i, BitmapRegionDecoder bitmapRegionDecoder) {
        this.imageUri = str;
        this.imageSize = point;
        this.imageType = imageType;
        this.exifOrientation = i;
        this.regionDecoder = bitmapRegionDecoder;
    }

    public static ImageRegionDecoder build(Context context, String str, boolean z) throws IOException {
        InputStream inputStream;
        UriModel match = UriModel.match(context, str);
        if (match == null) {
            throw new IllegalArgumentException("Unknown scheme uri. " + str);
        }
        try {
            DataSource dataSource = match.getDataSource(context, str, null);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            ImageDecodeUtils.decodeBitmap(dataSource, options);
            Point point = new Point(options.outWidth, options.outHeight);
            ImageOrientationCorrector orientationCorrector = Sketch.with(context).getConfiguration().getOrientationCorrector();
            int readExifOrientation = !z ? orientationCorrector.readExifOrientation(options.outMimeType, dataSource) : 0;
            orientationCorrector.rotateSize(point, readExifOrientation);
            try {
                inputStream = dataSource.getInputStream();
                try {
                    BitmapRegionDecoder newInstance = BitmapRegionDecoder.newInstance(inputStream, false);
                    SketchUtils.close(inputStream);
                    return new ImageRegionDecoder(str, point, ImageType.valueOfMimeType(options.outMimeType), readExifOrientation, newInstance);
                } catch (Throwable th) {
                    th = th;
                    SketchUtils.close(inputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                inputStream = null;
            }
        } catch (GetDataSourceException e) {
            throw new IllegalArgumentException("Can not be generated DataSource.  " + str, e);
        }
    }

    @TargetApi(10)
    public Bitmap decodeRegion(Rect rect, BitmapFactory.Options options) {
        if (isReady()) {
            return this.regionDecoder.decodeRegion(rect, options);
        }
        return null;
    }

    public int getExifOrientation() {
        return this.exifOrientation;
    }

    public Point getImageSize() {
        return this.imageSize;
    }

    public ImageType getImageType() {
        return this.imageType;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    @TargetApi(10)
    public boolean isReady() {
        return (this.regionDecoder == null || this.regionDecoder.isRecycled()) ? false : true;
    }

    @TargetApi(10)
    public void recycle() {
        if (isReady()) {
            this.regionDecoder.recycle();
            this.regionDecoder = null;
        }
    }
}
