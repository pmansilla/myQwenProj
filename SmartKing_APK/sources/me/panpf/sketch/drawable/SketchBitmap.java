package me.panpf.sketch.drawable;

import android.graphics.Bitmap;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public abstract class SketchBitmap {
    private ImageAttrs attrs;
    protected Bitmap bitmap;
    private String key;
    private String uri;

    /* JADX INFO: Access modifiers changed from: protected */
    public SketchBitmap(Bitmap bitmap, String str, String str2, ImageAttrs imageAttrs) {
        if (bitmap == null || bitmap.isRecycled()) {
            throw new IllegalArgumentException("bitmap is null or recycled");
        }
        this.bitmap = bitmap;
        this.key = str;
        this.uri = str2;
        this.attrs = imageAttrs;
    }

    public ImageAttrs getAttrs() {
        return this.attrs;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public Bitmap.Config getBitmapConfig() {
        if (this.bitmap != null) {
            return this.bitmap.getConfig();
        }
        return null;
    }

    public int getByteCount() {
        return SketchUtils.getByteCount(getBitmap());
    }

    public abstract String getInfo();

    public String getKey() {
        return this.key;
    }

    public String getUri() {
        return this.uri;
    }
}
