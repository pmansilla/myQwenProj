package me.panpf.sketch.drawable;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.util.SketchUtils;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;

/* loaded from: classes2.dex */
public class SketchGifDrawableImpl extends GifDrawable implements SketchGifDrawable {
    private static final String NAME = "SketchGifDrawableImpl";
    private BitmapPool bitmapPool;
    private ImageAttrs imageAttrs;
    private ImageFrom imageFrom;
    private String key;
    private Map<SketchGifDrawable.AnimationListener, AnimationListener> listenerMap;
    private String uri;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SketchGifDrawableImpl(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, ContentResolver contentResolver, Uri uri) throws IOException {
        super(contentResolver, uri);
        this.key = str;
        this.uri = str2;
        this.imageAttrs = imageAttrs;
        this.imageFrom = imageFrom;
        this.bitmapPool = bitmapPool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SketchGifDrawableImpl(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, AssetFileDescriptor assetFileDescriptor) throws IOException {
        super(assetFileDescriptor);
        this.key = str;
        this.uri = str2;
        this.imageAttrs = imageAttrs;
        this.imageFrom = imageFrom;
        this.bitmapPool = bitmapPool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SketchGifDrawableImpl(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, AssetManager assetManager, String str3) throws IOException {
        super(assetManager, str3);
        this.key = str;
        this.uri = str2;
        this.imageAttrs = imageAttrs;
        this.imageFrom = imageFrom;
        this.bitmapPool = bitmapPool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SketchGifDrawableImpl(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, Resources resources, int i) throws Resources.NotFoundException, IOException {
        super(resources, i);
        this.key = str;
        this.uri = str2;
        this.imageAttrs = imageAttrs;
        this.imageFrom = imageFrom;
        this.bitmapPool = bitmapPool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SketchGifDrawableImpl(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, File file) throws IOException {
        super(file);
        this.key = str;
        this.uri = str2;
        this.imageAttrs = imageAttrs;
        this.imageFrom = imageFrom;
        this.bitmapPool = bitmapPool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SketchGifDrawableImpl(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, FileDescriptor fileDescriptor) throws IOException {
        super(fileDescriptor);
        this.key = str;
        this.uri = str2;
        this.imageAttrs = imageAttrs;
        this.imageFrom = imageFrom;
        this.bitmapPool = bitmapPool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SketchGifDrawableImpl(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, InputStream inputStream) throws IOException {
        super(inputStream);
        this.key = str;
        this.uri = str2;
        this.imageAttrs = imageAttrs;
        this.imageFrom = imageFrom;
        this.bitmapPool = bitmapPool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SketchGifDrawableImpl(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, String str3) throws IOException {
        super(str3);
        this.key = str;
        this.uri = str2;
        this.imageAttrs = imageAttrs;
        this.imageFrom = imageFrom;
        this.bitmapPool = bitmapPool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SketchGifDrawableImpl(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, ByteBuffer byteBuffer) throws IOException {
        super(byteBuffer);
        this.key = str;
        this.uri = str2;
        this.imageAttrs = imageAttrs;
        this.imageFrom = imageFrom;
        this.bitmapPool = bitmapPool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SketchGifDrawableImpl(String str, String str2, ImageAttrs imageAttrs, ImageFrom imageFrom, BitmapPool bitmapPool, byte[] bArr) throws IOException {
        super(bArr);
        this.key = str;
        this.uri = str2;
        this.imageAttrs = imageAttrs;
        this.imageFrom = imageFrom;
        this.bitmapPool = bitmapPool;
    }

    @Override // me.panpf.sketch.drawable.SketchGifDrawable
    public void addAnimationListener(@NonNull final SketchGifDrawable.AnimationListener animationListener) {
        if (this.listenerMap == null) {
            this.listenerMap = new HashMap();
        }
        AnimationListener animationListener2 = new AnimationListener() { // from class: me.panpf.sketch.drawable.SketchGifDrawableImpl.1
            public void onAnimationCompleted(int i) {
                animationListener.onAnimationCompleted(i);
            }
        };
        addAnimationListener(animationListener2);
        this.listenerMap.put(animationListener, animationListener2);
    }

    @Override // me.panpf.sketch.drawable.SketchGifDrawable
    public void followPageVisible(boolean z, boolean z2) {
        if (z) {
            start();
        } else if (!z2) {
            stop();
        } else {
            seekToFrame(0);
            stop();
        }
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public Bitmap.Config getBitmapConfig() {
        if (this.mBuffer != null) {
            return this.mBuffer.getConfig();
        }
        return null;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getByteCount() {
        return (int) getAllocationByteCount();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getExifOrientation() {
        return this.imageAttrs.getExifOrientation();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public ImageFrom getImageFrom() {
        return this.imageFrom;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getInfo() {
        return SketchUtils.makeImageInfo(NAME, getOriginWidth(), getOriginHeight(), getMimeType(), getExifOrientation(), this.mBuffer, getAllocationByteCount(), null);
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getKey() {
        return this.key;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getMimeType() {
        return this.imageAttrs.getMimeType();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getOriginHeight() {
        return this.imageAttrs.getHeight();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getOriginWidth() {
        return this.imageAttrs.getWidth();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getUri() {
        return this.uri;
    }

    protected Bitmap makeBitmap(int i, int i2, Bitmap.Config config) {
        return this.bitmapPool != null ? this.bitmapPool.getOrMake(i, i2, config) : super.makeBitmap(i, i2, config);
    }

    protected void recycleBitmap() {
        if (this.mBuffer == null) {
            return;
        }
        if (this.bitmapPool != null) {
            BitmapPoolUtils.freeBitmapToPool(this.mBuffer, this.bitmapPool);
        } else {
            super.recycleBitmap();
        }
    }

    @Override // me.panpf.sketch.drawable.SketchGifDrawable
    public boolean removeAnimationListener(SketchGifDrawable.AnimationListener animationListener) {
        AnimationListener remove;
        return (this.listenerMap == null || this.listenerMap.isEmpty() || (remove = this.listenerMap.remove(animationListener)) == null || !removeAnimationListener(remove)) ? false : true;
    }
}
