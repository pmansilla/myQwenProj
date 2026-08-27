package me.panpf.sketch.uri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.io.File;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.datasource.FileDataSource;
import me.panpf.sketch.request.DownloadResult;

/* loaded from: classes2.dex */
public class FileVariantUriModel extends FileUriModel {
    public static final String SCHEME = "file://";

    public static String makeUri(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.startsWith(SCHEME)) {
            return str;
        }
        return SCHEME + str;
    }

    @Override // me.panpf.sketch.uri.FileUriModel, me.panpf.sketch.uri.UriModel
    @NonNull
    public DataSource getDataSource(@NonNull Context context, @NonNull String str, DownloadResult downloadResult) {
        return new FileDataSource(new File(getUriContent(str)));
    }

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public String getDiskCacheKey(@NonNull String str) {
        return getUriContent(str);
    }

    @Override // me.panpf.sketch.uri.UriModel
    @NonNull
    public String getUriContent(@NonNull String str) {
        return match(str) ? str.substring(SCHEME.length()) : str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.uri.FileUriModel, me.panpf.sketch.uri.UriModel
    public boolean match(@NonNull String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(SCHEME);
    }
}
