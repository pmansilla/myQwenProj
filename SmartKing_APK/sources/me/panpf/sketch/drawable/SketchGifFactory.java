package me.panpf.sketch.drawable;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.decode.NotFoundGifLibraryException;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public class SketchGifFactory {
    private static int existGifLibrary;

    public static void assetExistGifLibrary() throws NotFoundGifLibraryException {
        if (!isExistGifLibrary()) {
            throw new NotFoundGifLibraryException();
        }
    }

    public static SketchGifDrawable createGifDrawable(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, ContentResolver contentResolver, Uri uri) throws IOException, NotFoundGifLibraryException {
        assetExistGifLibrary();
        return new SketchGifDrawableImpl(str, str2, imageAttrs, imageFrom, bitmapPool, contentResolver, uri);
    }

    public static SketchGifDrawable createGifDrawable(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, AssetFileDescriptor assetFileDescriptor) throws IOException, NotFoundGifLibraryException {
        assetExistGifLibrary();
        return new SketchGifDrawableImpl(str, str2, imageAttrs, imageFrom, bitmapPool, assetFileDescriptor);
    }

    public static SketchGifDrawable createGifDrawable(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, AssetManager assetManager, String str3) throws IOException, NotFoundGifLibraryException {
        assetExistGifLibrary();
        return new SketchGifDrawableImpl(str, str2, imageAttrs, imageFrom, bitmapPool, assetManager, str3);
    }

    public static SketchGifDrawable createGifDrawable(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, Resources resources, int i) throws Resources.NotFoundException, IOException, NotFoundGifLibraryException {
        assetExistGifLibrary();
        return new SketchGifDrawableImpl(str, str2, imageAttrs, imageFrom, bitmapPool, resources, i);
    }

    public static SketchGifDrawable createGifDrawable(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, File file) throws IOException, NotFoundGifLibraryException {
        assetExistGifLibrary();
        return new SketchGifDrawableImpl(str, str2, imageAttrs, imageFrom, bitmapPool, file);
    }

    public static SketchGifDrawable createGifDrawable(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, FileDescriptor fileDescriptor) throws IOException, NotFoundGifLibraryException {
        assetExistGifLibrary();
        return new SketchGifDrawableImpl(str, str2, imageAttrs, imageFrom, bitmapPool, fileDescriptor);
    }

    public static SketchGifDrawable createGifDrawable(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, InputStream inputStream) throws IOException, NotFoundGifLibraryException {
        assetExistGifLibrary();
        return new SketchGifDrawableImpl(str, str2, imageAttrs, imageFrom, bitmapPool, inputStream);
    }

    public static SketchGifDrawable createGifDrawable(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, String str3) throws IOException, NotFoundGifLibraryException {
        assetExistGifLibrary();
        return new SketchGifDrawableImpl(str, str2, imageAttrs, imageFrom, bitmapPool, str3);
    }

    public static SketchGifDrawable createGifDrawable(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, ByteBuffer byteBuffer) throws IOException, NotFoundGifLibraryException {
        assetExistGifLibrary();
        return new SketchGifDrawableImpl(str, str2, imageAttrs, imageFrom, bitmapPool, byteBuffer);
    }

    public static SketchGifDrawable createGifDrawable(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, byte[] bArr) throws IOException, NotFoundGifLibraryException {
        assetExistGifLibrary();
        return new SketchGifDrawableImpl(str, str2, imageAttrs, imageFrom, bitmapPool, bArr);
    }

    public static boolean isExistGifLibrary() {
        if (existGifLibrary == 0) {
            synchronized (SketchGifFactory.class) {
                if (existGifLibrary == 0) {
                    try {
                        Class.forName("me.panpf.sketch.gif.BuildConfig");
                        Class.forName("pl.droidsonroids.gif.GifDrawable");
                        existGifLibrary = 1;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        existGifLibrary = -1;
                    }
                }
            }
        }
        return existGifLibrary == 1;
    }
}
