package me.panpf.sketch.uri;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ApkIconUriModel extends AbsBitmapDiskCacheUriModel {
    private static final String NAME = "ApkIconUriModel";
    public static final String SCHEME = "apk.icon://";

    public static String makeUri(String str) {
        return SCHEME + str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // me.panpf.sketch.uri.AbsDiskCacheUriModel
    @NonNull
    public Bitmap getContent(@NonNull Context context, @NonNull String str) throws GetDataSourceException {
        Bitmap readApkIcon = SketchUtils.readApkIcon(context, getUriContent(str), false, NAME, Sketch.with(context).getConfiguration().getBitmapPool());
        if (readApkIcon != null && !readApkIcon.isRecycled()) {
            return readApkIcon;
        }
        String format = String.format("Apk icon bitmap invalid. %s", str);
        SLog.e(NAME, format);
        throw new GetDataSourceException(format);
    }

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public String getDiskCacheKey(@NonNull String str) {
        return SketchUtils.createFileUriDiskCacheKey(str, getUriContent(str));
    }

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public String getUriContent(@NonNull String str) {
        return match(str) ? str.substring(SCHEME.length()) : str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.UriModel
    public boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
