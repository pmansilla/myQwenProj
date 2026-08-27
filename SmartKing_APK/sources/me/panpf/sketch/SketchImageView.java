package me.panpf.sketch;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import me.panpf.sketch.request.DisplayCache;
import me.panpf.sketch.request.DisplayRequest;
import me.panpf.sketch.request.RedisplayListener;
import me.panpf.sketch.viewfun.FunctionPropertyView;

/* loaded from: classes2.dex */
public class SketchImageView extends FunctionPropertyView {
    public SketchImageView(Context context) {
        super(context);
    }

    public SketchImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SketchImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // me.panpf.sketch.SketchView
    @Nullable
    public DisplayRequest displayAssetImage(@NonNull String str) {
        return Sketch.with(getContext()).displayFromAsset(str, this).commit();
    }

    @Override // me.panpf.sketch.SketchView
    @Nullable
    public DisplayRequest displayContentImage(@NonNull String str) {
        return Sketch.with(getContext()).displayFromContent(str, this).commit();
    }

    @Override // me.panpf.sketch.SketchView
    @Nullable
    public DisplayRequest displayImage(@NonNull String str) {
        return Sketch.with(getContext()).display(str, this).commit();
    }

    @Override // me.panpf.sketch.SketchView
    @Nullable
    public DisplayRequest displayResourceImage(@DrawableRes int i) {
        return Sketch.with(getContext()).displayFromResource(i, this).commit();
    }

    @NonNull
    public String getOptionsKey() {
        DisplayCache displayCache = getDisplayCache();
        return displayCache != null ? displayCache.options.makeKey() : getOptions().makeKey();
    }

    @Override // me.panpf.sketch.SketchView
    public boolean redisplay(@Nullable RedisplayListener redisplayListener) {
        DisplayCache displayCache = getDisplayCache();
        if (displayCache == null) {
            return false;
        }
        if (redisplayListener != null) {
            redisplayListener.onPreCommit(displayCache.uri, displayCache.options);
        }
        Sketch.with(getContext()).display(displayCache.uri, this).options(displayCache.options).commit();
        return true;
    }
}
