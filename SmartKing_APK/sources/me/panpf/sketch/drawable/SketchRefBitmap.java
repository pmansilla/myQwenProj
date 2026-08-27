package me.panpf.sketch.drawable;

import android.graphics.Bitmap;
import me.panpf.sketch.SLog;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class SketchRefBitmap extends SketchBitmap {
    private static final String NAME = "SketchRefBitmap";
    private BitmapPool bitmapPool;
    private int displayRefCount;
    private int memoryCacheRefCount;
    private int waitingUseRefCount;

    public SketchRefBitmap(Bitmap bitmap, String str, String str2, ImageAttrs imageAttrs, BitmapPool bitmapPool) {
        super(bitmap, str, str2, imageAttrs);
        this.bitmapPool = bitmapPool;
    }

    private void referenceChanged(String str) {
        if (isRecycled()) {
            SLog.e(NAME, "Recycled. %s. %s", str, getKey());
            return;
        }
        if (this.memoryCacheRefCount != 0 || this.displayRefCount != 0 || this.waitingUseRefCount != 0) {
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Can't free. %s. references(%d,%d,%d). %s", str, Integer.valueOf(this.memoryCacheRefCount), Integer.valueOf(this.displayRefCount), Integer.valueOf(this.waitingUseRefCount), getInfo());
            }
        } else {
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Free. %s. %s", str, getInfo());
            }
            BitmapPoolUtils.freeBitmapToPool(this.bitmap, this.bitmapPool);
            this.bitmap = null;
        }
    }

    @Override // me.panpf.sketch.drawable.SketchBitmap
    public String getInfo() {
        if (isRecycled()) {
            return String.format("%s(Recycled,%s)", NAME, getKey());
        }
        ImageAttrs attrs = getAttrs();
        return SketchUtils.makeImageInfo(NAME, attrs.getWidth(), attrs.getHeight(), attrs.getMimeType(), attrs.getExifOrientation(), this.bitmap, getByteCount(), getKey());
    }

    public synchronized boolean isRecycled() {
        boolean z;
        if (this.bitmap != null) {
            z = this.bitmap.isRecycled();
        }
        return z;
    }

    public synchronized void setIsCached(String str, boolean z) {
        try {
            if (z) {
                this.memoryCacheRefCount++;
                referenceChanged(str);
            } else if (this.memoryCacheRefCount > 0) {
                this.memoryCacheRefCount--;
                referenceChanged(str);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void setIsDisplayed(String str, boolean z) {
        try {
            if (z) {
                this.displayRefCount++;
                referenceChanged(str);
            } else if (this.displayRefCount > 0) {
                this.displayRefCount--;
                referenceChanged(str);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void setIsWaitingUse(String str, boolean z) {
        try {
            if (z) {
                this.waitingUseRefCount++;
                referenceChanged(str);
            } else if (this.waitingUseRefCount > 0) {
                this.waitingUseRefCount--;
                referenceChanged(str);
            }
        } catch (Throwable th) {
            throw th;
        }
    }
}
