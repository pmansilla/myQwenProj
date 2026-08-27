package me.panpf.sketch.zoom;

import com.autonavi.amap.mapcore.tools.GlMapUtil;

/* loaded from: classes2.dex */
public class Size {
    private int mHeight;
    private int mWidth;

    public Size() {
    }

    public Size(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    private static NumberFormatException invalidSize(String str) {
        throw new NumberFormatException("Invalid Size: \"" + str + "\"");
    }

    public static Size parseSize(String str) throws NumberFormatException {
        int indexOf = str.indexOf(42);
        if (indexOf < 0) {
            indexOf = str.indexOf(GlMapUtil.DEVICE_DISPLAY_DPI_LOW);
        }
        if (indexOf < 0) {
            throw invalidSize(str);
        }
        try {
            return new Size(Integer.parseInt(str.substring(0, indexOf)), Integer.parseInt(str.substring(indexOf + 1)));
        } catch (NumberFormatException unused) {
            throw invalidSize(str);
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Size)) {
            return false;
        }
        Size size = (Size) obj;
        return this.mWidth == size.mWidth && this.mHeight == size.mHeight;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int hashCode() {
        return this.mHeight ^ ((this.mWidth << 16) | (this.mWidth >>> 16));
    }

    public boolean isEmpty() {
        return this.mWidth == 0 || this.mHeight == 0;
    }

    public void set(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    public String toString() {
        return this.mWidth + "x" + this.mHeight;
    }
}
