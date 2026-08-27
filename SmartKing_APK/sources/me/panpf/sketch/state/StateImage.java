package me.panpf.sketch.state;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.request.DisplayOptions;

/* loaded from: classes2.dex */
public interface StateImage {
    @Nullable
    Drawable getDrawable(@NonNull Context context, @NonNull SketchView sketchView, @NonNull DisplayOptions displayOptions);
}
