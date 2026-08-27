package me.panpf.sketch.request;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/* loaded from: classes2.dex */
public class ShapeSize {
    private int height;
    private ImageView.ScaleType scaleType;
    private int width;

    /* loaded from: classes2.dex */
    static class ByViewFixedSizeShapeSize extends ShapeSize {
        static final ByViewFixedSizeShapeSize INSTANCE = new ByViewFixedSizeShapeSize();

        ByViewFixedSizeShapeSize() {
            super();
        }
    }

    private ShapeSize() {
    }

    public ShapeSize(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public ShapeSize(int i, int i2, ImageView.ScaleType scaleType) {
        this.width = i;
        this.height = i2;
        this.scaleType = scaleType;
    }

    public static ShapeSize byViewFixedSize() {
        return ByViewFixedSizeShapeSize.INSTANCE;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ShapeSize)) {
            return false;
        }
        ShapeSize shapeSize = (ShapeSize) obj;
        return this.width == shapeSize.width && this.height == shapeSize.height;
    }

    public int getHeight() {
        return this.height;
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
        return String.format("ShapeSize(%dx%d)", Integer.valueOf(this.width), Integer.valueOf(this.height));
    }
}
