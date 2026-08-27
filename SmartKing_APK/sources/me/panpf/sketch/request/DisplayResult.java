package me.panpf.sketch.request;

import android.graphics.drawable.Drawable;
import me.panpf.sketch.decode.ImageAttrs;

/* loaded from: classes2.dex */
public class DisplayResult {
    private Drawable drawable;
    private ImageAttrs imageAttrs;
    private ImageFrom imageFrom;

    public DisplayResult(Drawable drawable, ImageFrom imageFrom, ImageAttrs imageAttrs) {
        this.drawable = drawable;
        this.imageFrom = imageFrom;
        this.imageAttrs = imageAttrs;
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public ImageAttrs getImageAttrs() {
        return this.imageAttrs;
    }

    public ImageFrom getImageFrom() {
        return this.imageFrom;
    }
}
