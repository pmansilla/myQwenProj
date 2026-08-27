package me.panpf.sketch.uri;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import java.io.OutputStream;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public abstract class AbsBitmapDiskCacheUriModel extends AbsDiskCacheUriModel<Bitmap> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.AbsDiskCacheUriModel
    public final void closeContent(@NonNull Bitmap bitmap, @NonNull Context context) {
        BitmapPoolUtils.freeBitmapToPool(bitmap, Sketch.with(context).getConfiguration().getBitmapPool());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.AbsDiskCacheUriModel
    public final void outContent(@NonNull Bitmap bitmap, @NonNull OutputStream outputStream) throws Exception {
        bitmap.compress(SketchUtils.bitmapConfigToCompressFormat(bitmap.getConfig()), 100, outputStream);
    }
}
