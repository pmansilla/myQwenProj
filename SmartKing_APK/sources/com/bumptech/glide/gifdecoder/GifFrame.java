package com.bumptech.glide.gifdecoder;

import android.support.annotation.ColorInt;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
class GifFrame {
    public static final int DISPOSAL_BACKGROUND = 2;
    public static final int DISPOSAL_NONE = 1;
    public static final int DISPOSAL_PREVIOUS = 3;
    public static final int DISPOSAL_UNSPECIFIED = 0;
    int bufferFrameStart;
    int delay;
    int dispose;
    int ih;
    boolean interlace;
    int iw;
    int ix;
    int iy;

    @ColorInt
    int[] lct;
    int transIndex;
    boolean transparency;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    @interface GifDisposalMethod {
    }
}
