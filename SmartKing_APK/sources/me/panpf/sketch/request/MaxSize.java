package me.panpf.sketch.request;

import android.support.annotation.NonNull;
import me.panpf.sketch.Key;

/* loaded from: classes2.dex */
public class MaxSize implements Key {
    private int height;
    private int width;

    public MaxSize(int i, int i2) {
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
        if (!(obj instanceof MaxSize)) {
            return false;
        }
        MaxSize maxSize = (MaxSize) obj;
        return this.width == maxSize.width && this.height == maxSize.height;
    }

    public int getHeight() {
        return this.height;
    }

    @Override // me.panpf.sketch.Key
    public String getKey() {
        return toString();
    }

    public int getWidth() {
        return this.width;
    }

    @NonNull
    public String toString() {
        return String.format("MaxSize(%dx%d)", Integer.valueOf(this.width), Integer.valueOf(this.height));
    }
}
