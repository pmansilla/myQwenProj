package me.panpf.sketch.drawable;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.widget.MediaController;

/* loaded from: classes2.dex */
public interface SketchGifDrawable extends SketchDrawable, Animatable, MediaController.MediaPlayerControl {

    /* loaded from: classes2.dex */
    public interface AnimationListener {
        void onAnimationCompleted(int i);
    }

    void addAnimationListener(@NonNull AnimationListener animationListener);

    void followPageVisible(boolean z, boolean z2);

    long getAllocationByteCount();

    String getComment();

    Bitmap getCurrentFrame();

    int getCurrentFrameIndex();

    int getCurrentLoop();

    int getFrameByteCount();

    int getFrameDuration(@IntRange(from = 0) int i);

    long getInputSourceByteCount();

    int getLoopCount();

    long getMetadataAllocationByteCount();

    int getNumberOfFrames();

    @NonNull
    Paint getPaint();

    int getPixel(int i, int i2);

    void getPixels(@NonNull int[] iArr);

    boolean isAnimationCompleted();

    boolean isRecycled();

    void recycle();

    boolean removeAnimationListener(AnimationListener animationListener);

    void reset();

    void seekToFrame(@IntRange(from = 0, to = 2147483647L) int i);

    Bitmap seekToFrameAndGet(@IntRange(from = 0, to = 2147483647L) int i);

    Bitmap seekToPositionAndGet(@IntRange(from = 0, to = 2147483647L) int i);

    void setLoopCount(@IntRange(from = 0, to = 65535) int i);

    void setSpeed(@FloatRange(from = 0.0d, fromInclusive = false) float f);
}
