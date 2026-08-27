package me.panpf.sketch;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import me.panpf.sketch.request.DisplayCache;
import me.panpf.sketch.request.DisplayListener;
import me.panpf.sketch.request.DisplayOptions;
import me.panpf.sketch.request.DisplayRequest;
import me.panpf.sketch.request.DownloadProgressListener;
import me.panpf.sketch.request.RedisplayListener;
import me.panpf.sketch.uri.UriModel;

/* loaded from: classes2.dex */
public interface SketchView {
    void clearAnimation();

    @Nullable
    DisplayRequest displayAssetImage(@NonNull String str);

    @Nullable
    DisplayRequest displayContentImage(@NonNull String str);

    @Nullable
    DisplayRequest displayImage(@NonNull String str);

    @Nullable
    DisplayRequest displayResourceImage(@DrawableRes int i);

    @Nullable
    DisplayCache getDisplayCache();

    @Nullable
    DisplayListener getDisplayListener();

    @Nullable
    DownloadProgressListener getDownloadProgressListener();

    @Nullable
    Drawable getDrawable();

    @Nullable
    ViewGroup.LayoutParams getLayoutParams();

    @NonNull
    DisplayOptions getOptions();

    int getPaddingBottom();

    int getPaddingLeft();

    int getPaddingRight();

    int getPaddingTop();

    @NonNull
    Resources getResources();

    @Nullable
    ImageView.ScaleType getScaleType();

    boolean isUseSmallerThumbnails();

    boolean isZoomEnabled();

    void onReadyDisplay(@Nullable UriModel uriModel);

    boolean redisplay(@Nullable RedisplayListener redisplayListener);

    void setDisplayCache(@NonNull DisplayCache displayCache);

    void setDisplayListener(@Nullable DisplayListener displayListener);

    void setDownloadProgressListener(@Nullable DownloadProgressListener downloadProgressListener);

    void setImageDrawable(@Nullable Drawable drawable);

    void setOptions(@Nullable DisplayOptions displayOptions);

    void startAnimation(@Nullable Animation animation);
}
