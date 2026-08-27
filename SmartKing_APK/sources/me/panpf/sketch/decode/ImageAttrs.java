package me.panpf.sketch.decode;

/* loaded from: classes2.dex */
public class ImageAttrs {
    private int exifOrientation;
    private int height;
    private String mimeType;
    private int width;

    public ImageAttrs(String str, int i, int i2, int i3) {
        this.mimeType = str;
        this.width = i;
        this.height = i2;
        this.exifOrientation = i3;
    }

    public int getExifOrientation() {
        return this.exifOrientation;
    }

    public int getHeight() {
        return this.height;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public int getWidth() {
        return this.width;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetSize(int i, int i2) {
        this.width = i;
        this.height = i2;
    }
}
