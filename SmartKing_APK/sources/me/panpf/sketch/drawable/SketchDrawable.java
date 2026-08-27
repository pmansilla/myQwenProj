package me.panpf.sketch.drawable;

import android.graphics.Bitmap;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public interface SketchDrawable {
    Bitmap.Config getBitmapConfig();

    int getByteCount();

    int getExifOrientation();

    ImageFrom getImageFrom();

    String getInfo();

    String getKey();

    String getMimeType();

    int getOriginHeight();

    int getOriginWidth();

    String getUri();
}
