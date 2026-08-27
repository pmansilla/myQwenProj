package me.panpf.sketch.shaper;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.request.ShapeSize;

/* loaded from: classes2.dex */
public interface ImageShaper {
    void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect);

    @NonNull
    Path getPath(@NonNull Rect rect);

    void onUpdateShaderMatrix(@NonNull Matrix matrix, @NonNull Rect rect, int i, int i2, @Nullable ShapeSize shapeSize, @NonNull Rect rect2);
}
