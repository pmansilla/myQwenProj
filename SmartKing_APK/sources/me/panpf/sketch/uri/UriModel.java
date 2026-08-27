package me.panpf.sketch.uri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.request.DownloadResult;

/* loaded from: classes2.dex */
public abstract class UriModel {
    @Nullable
    public static UriModel match(@NonNull Context context, @NonNull String str) {
        return match(Sketch.with(context), str);
    }

    @Nullable
    public static UriModel match(@NonNull Sketch sketch, @NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return sketch.getConfiguration().getUriModelManager().match(str);
    }

    @NonNull
    public abstract DataSource getDataSource(@NonNull Context context, @NonNull String str, @Nullable DownloadResult downloadResult) throws GetDataSourceException;

    @NonNull
    public String getDiskCacheKey(@NonNull String str) {
        return str;
    }

    @NonNull
    public String getUriContent(@NonNull String str) {
        return str;
    }

    public boolean isConvertShortUriForKey() {
        return false;
    }

    public boolean isFromNet() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean match(@NonNull String str);
}
