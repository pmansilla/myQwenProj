package me.panpf.sketch.zoom.block;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.litesuits.orm.db.assit.SQLBuilder;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.util.KeyCounter;

/* loaded from: classes2.dex */
public class Block {
    public Bitmap bitmap;
    public ImageRegionDecoder decoder;
    public int inSampleSize;
    public Rect drawRect = new Rect();
    public Rect srcRect = new Rect();
    public float scale = -1.0f;
    public Rect bitmapDrawSrcRect = new Rect();
    private KeyCounter keyCounter = new KeyCounter();

    public void clean(BitmapPool bitmapPool) {
        if (this.bitmap != null) {
            BitmapPoolUtils.freeBitmapToPoolForRegionDecoder(this.bitmap, bitmapPool);
            this.bitmap = null;
        }
        this.bitmapDrawSrcRect.setEmpty();
        this.srcRect.setEmpty();
        this.drawRect.setEmpty();
        this.inSampleSize = 0;
        this.scale = -1.0f;
        this.decoder = null;
    }

    public String getInfo() {
        return SQLBuilder.PARENTHESES_LEFT + "drawRect:" + this.drawRect.toShortString() + ",srcRect:" + this.srcRect.toShortString() + ",inSampleSize:" + this.inSampleSize + ",scale:" + this.scale + ",key:" + this.keyCounter.getKey() + ",hashCode:" + Integer.toHexString(hashCode()) + SQLBuilder.PARENTHESES_RIGHT;
    }

    public int getKey() {
        return this.keyCounter.getKey();
    }

    public boolean isDecodeParamEmpty() {
        return this.drawRect.isEmpty() || this.drawRect.isEmpty() || this.srcRect.isEmpty() || this.srcRect.isEmpty() || this.inSampleSize == 0 || this.scale == -1.0f;
    }

    public boolean isEmpty() {
        return this.bitmap == null || this.bitmap.isRecycled() || isDecodeParamEmpty();
    }

    public boolean isExpired(int i) {
        return this.keyCounter.getKey() != i;
    }

    public void refreshKey() {
        this.keyCounter.refresh();
    }
}
