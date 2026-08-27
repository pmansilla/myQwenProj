package me.panpf.sketch.decode;

import android.graphics.Bitmap;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public enum ImageType {
    JPEG("image/jpeg", Bitmap.Config.RGB_565, Bitmap.Config.RGB_565),
    PNG("image/png", Bitmap.Config.ARGB_8888, SketchUtils.isDisabledARGB4444() ? Bitmap.Config.ARGB_8888 : Bitmap.Config.ARGB_4444),
    WEBP("image/webp", Bitmap.Config.ARGB_8888, SketchUtils.isDisabledARGB4444() ? Bitmap.Config.ARGB_8888 : Bitmap.Config.ARGB_4444),
    GIF("image/gif", Bitmap.Config.ARGB_8888, SketchUtils.isDisabledARGB4444() ? Bitmap.Config.ARGB_8888 : Bitmap.Config.ARGB_4444),
    BMP("image/bmp", Bitmap.Config.RGB_565, Bitmap.Config.RGB_565);

    Bitmap.Config bestConfig;
    Bitmap.Config lowQualityConfig;
    String mimeType;

    ImageType(String str, Bitmap.Config config, Bitmap.Config config2) {
        this.mimeType = str;
        this.bestConfig = config;
        this.lowQualityConfig = config2;
    }

    public static ImageType valueOfMimeType(String str) {
        if (JPEG.mimeType.equalsIgnoreCase(str)) {
            return JPEG;
        }
        if (PNG.mimeType.equalsIgnoreCase(str)) {
            return PNG;
        }
        if (WEBP.mimeType.equalsIgnoreCase(str)) {
            return WEBP;
        }
        if (GIF.mimeType.equalsIgnoreCase(str)) {
            return GIF;
        }
        if (BMP.mimeType.equalsIgnoreCase(str)) {
            return BMP;
        }
        return null;
    }

    public boolean equals(String str) {
        return this.mimeType.equalsIgnoreCase(str);
    }

    public Bitmap.Config getConfig(boolean z) {
        return z ? this.lowQualityConfig : this.bestConfig;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setBestConfig(Bitmap.Config config) {
        this.bestConfig = config;
    }

    public void setLowQualityConfig(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_4444 && SketchUtils.isDisabledARGB4444()) {
            config = Bitmap.Config.ARGB_8888;
        }
        this.lowQualityConfig = config;
    }
}
