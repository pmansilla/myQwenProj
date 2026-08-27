package me.panpf.sketch.request;

import android.support.annotation.NonNull;

/* loaded from: classes2.dex */
public class FixedSize {
    private int height;
    private int width;

    public FixedSize(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FixedSize)) {
            return false;
        }
        FixedSize fixedSize = (FixedSize) obj;
        return this.width == fixedSize.width && this.height == fixedSize.height;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    @NonNull
    public String toString() {
        return String.format("FixedSize(%dx%d)", Integer.valueOf(this.width), Integer.valueOf(this.height));
    }
}
