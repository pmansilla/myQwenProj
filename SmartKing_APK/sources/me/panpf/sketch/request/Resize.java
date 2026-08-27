package me.panpf.sketch.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import me.panpf.sketch.Key;

/* loaded from: classes2.dex */
public class Resize implements Key {
    private int height;
    private Mode mode;
    private ImageView.ScaleType scaleType;
    private int width;

    /* loaded from: classes2.dex */
    static class ByViewFixedSizeResize extends Resize {
        static ByViewFixedSizeResize INSTANCE = new ByViewFixedSizeResize();
        static ByViewFixedSizeResize INSTANCE_EXACTLY_SAME = new ByViewFixedSizeResize(Mode.EXACTLY_SAME);

        private ByViewFixedSizeResize() {
            super();
        }

        private ByViewFixedSizeResize(@NonNull Mode mode) {
            super(0, 0, null, mode);
        }
    }

    /* loaded from: classes2.dex */
    public enum Mode {
        ASPECT_RATIO_SAME,
        EXACTLY_SAME
    }

    private Resize() {
        this.mode = Mode.ASPECT_RATIO_SAME;
    }

    public Resize(int i, int i2) {
        this.mode = Mode.ASPECT_RATIO_SAME;
        this.width = i;
        this.height = i2;
    }

    public Resize(int i, int i2, ImageView.ScaleType scaleType) {
        this.mode = Mode.ASPECT_RATIO_SAME;
        this.width = i;
        this.height = i2;
        this.scaleType = scaleType;
    }

    public Resize(int i, int i2, ImageView.ScaleType scaleType, Mode mode) {
        this.mode = Mode.ASPECT_RATIO_SAME;
        this.width = i;
        this.height = i2;
        this.scaleType = scaleType;
        if (mode != null) {
            this.mode = mode;
        }
    }

    public Resize(int i, int i2, Mode mode) {
        this.mode = Mode.ASPECT_RATIO_SAME;
        this.width = i;
        this.height = i2;
        if (mode != null) {
            this.mode = mode;
        }
    }

    public Resize(Resize resize) {
        this.mode = Mode.ASPECT_RATIO_SAME;
        this.width = resize.width;
        this.height = resize.height;
        this.scaleType = resize.scaleType;
    }

    public static Resize byViewFixedSize() {
        return ByViewFixedSizeResize.INSTANCE;
    }

    public static Resize byViewFixedSize(Mode mode) {
        return mode == Mode.EXACTLY_SAME ? ByViewFixedSizeResize.INSTANCE_EXACTLY_SAME : ByViewFixedSizeResize.INSTANCE;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Resize)) {
            return false;
        }
        Resize resize = (Resize) obj;
        return this.width == resize.width && this.height == resize.height && this.scaleType == resize.scaleType;
    }

    public int getHeight() {
        return this.height;
    }

    @Override // me.panpf.sketch.Key
    @Nullable
    public String getKey() {
        return toString();
    }

    @NonNull
    public Mode getMode() {
        return this.mode;
    }

    public ImageView.ScaleType getScaleType() {
        return this.scaleType;
    }

    public int getWidth() {
        return this.width;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    @NonNull
    public String toString() {
        Object[] objArr = new Object[4];
        objArr[0] = Integer.valueOf(this.width);
        objArr[1] = Integer.valueOf(this.height);
        objArr[2] = this.scaleType != null ? this.scaleType.name() : "null";
        objArr[3] = this.mode.name();
        return String.format("Resize(%dx%d-%s-%s)", objArr);
    }
}
